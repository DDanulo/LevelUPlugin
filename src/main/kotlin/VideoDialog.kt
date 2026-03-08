package org.example

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.jcef.JBCefBrowser
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import javax.swing.JComponent

class VideoDialog(project: Project) : DialogWrapper(project, false) {
    private val browser = JBCefBrowser()

    init {
        init()
        title = "Video Player"
        isModal = false

        try {
            val tempDir = Files.createTempDirectory("plugin_video").toFile()
            tempDir.deleteOnExit()

            val videoStream = VideoDialog::class.java.getResourceAsStream("/video.webm")
            if (videoStream != null) {
                val videoFile = File(tempDir, "video.webm")
                Files.copy(videoStream, videoFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                videoFile.deleteOnExit()
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
}