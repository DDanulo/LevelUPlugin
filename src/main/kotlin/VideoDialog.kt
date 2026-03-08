package org.example

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.jcef.JBCefBrowser
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import java.io.File
import java.net.JarURLConnection
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import javax.swing.JComponent

class VideoDialog(project: Project) : DialogWrapper(project, false) {
    private val browser = JBCefBrowser()

    private val videoFiles: List<String> by lazy { loadVideoFiles() }

    init {
        init()
        title = "Video Player"
        isModal = false

        try {
            val tempDir = Files.createTempDirectory("plugin_video").toFile()
            tempDir.deleteOnExit()

            if (videoFiles.isEmpty()) {
                println("No videos found in the /videos directory!")
            } else {
                val randomVideoPath = videoFiles.random()
                println("Playing video: $randomVideoPath")

                val videoStream = VideoDialog::class.java.getResourceAsStream(randomVideoPath)
                if (videoStream != null) {
                    val videoFile = File(tempDir, "current_video.webm")
                    Files.copy(videoStream, videoFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                    videoFile.deleteOnExit()
                }
            }

            val htmlStream = VideoDialog::class.java.getResourceAsStream("/player.html")
            if (htmlStream != null) {
                val htmlFile = File(tempDir, "player.html")
                Files.copy(htmlStream, htmlFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                htmlFile.deleteOnExit()

                browser.loadURL(htmlFile.toURI().toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun createCenterPanel(): JComponent {
        browser.component.preferredSize = Dimension(400, 225)
        return browser.component
    }

    override fun dispose() {
        browser.dispose()
        super.dispose()
    }

    override fun show() {
        super.show()
        val env = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val bounds = env.maximumWindowBounds
        window.setLocation(bounds.width - window.width - 50, 50)
    }

    private fun loadVideoFiles(): List<String> {
        val folderPath = "/videos"
        val url = VideoDialog::class.java.getResource(folderPath) ?: return emptyList()

        val files = mutableListOf<String>()

        try {
            if (url.protocol == "jar") {
                val connection = url.openConnection() as JarURLConnection
                connection.jarFile.use { jar ->
                    val entries = jar.entries()
                    while (entries.hasMoreElements()) {
                        val name = entries.nextElement().name
                        if (name.startsWith("videos/") && !name.endsWith("/")) {
                            files.add("/$name")
                        }
                    }
                }
            } else {
                val folder = File(url.toURI())
                folder.listFiles()?.forEach { file ->
                    if (file.isFile && file.name.endsWith(".webm")) {
                        files.add("$folderPath/${file.name}")
                    }
                }
            }
        } catch (e: Exception) {
            println("Error reading video directory: ${e.message}")
            e.printStackTrace()
        }

        return files
    }
}