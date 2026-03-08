package org.example

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.WindowManager
import com.intellij.psi.PsiFile
import javax.swing.Timer
import javax.sound.sampled.AudioSystem
import javax.swing.JLabel
import javax.swing.SwingConstants

class TypingListener : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        val levelManager = service<LevelManager>()
        val didLevelUp = levelManager.addExp(1)

        val statusBar = WindowManager.getInstance().getStatusBar(project)
        val widget = statusBar?.getWidget("LevelWidget") as? LevelWidget

        widget?.updateText()
        statusBar?.updateWidget("LevelWidget")

        if (didLevelUp) {
            val newLevel = levelManager.currentLevel
            val (rank, color) = LevelWidget.getRankDetails(newLevel)

            playSound()

            ApplicationManager.getApplication().invokeLater {
                val label = JLabel(
                    "<html><center>" +
                            "<span style='font-size: 24px; color: #CCCCCC;'>🎉 LEVEL UP!</span><br>" +
                            "<span style='font-size: 48px; color: $color; font-weight: bold;'>$rank</span><br>" +
                            "<span style='font-size: 18px; color: #888888;'>You reached Level $newLevel</span>" +
                            "</center></html>"
                ).apply {
                    isOpaque = false
                    horizontalAlignment = SwingConstants.CENTER
                }

                val popup = JBPopupFactory.getInstance()
                    .createComponentPopupBuilder(label, null)
                    .setRequestFocus(false)
                    .setCancelOnClickOutside(false)
                    .setShowBorder(false)
                    .setShowShadow(false)
                    .setFocusable(false)
                    .createPopup()

                popup.showInCenterOf(editor.component)

                Timer(4000) {
                    if (!popup.isDisposed) {
                        popup.cancel()
                    }
                }.apply {
                    isRepeats = false
                    start()
                }
            }
        }

        return Result.CONTINUE
    }
    private fun playSound() {
        try {
            val url = TypingListener::class.java.getResource("/levelup.wav")
            if (url != null) {
                val audioIn = AudioSystem.getAudioInputStream(url)
                val clip = AudioSystem.getClip()
                clip.open(audioIn)
                clip.start()
            } else {
                println("Sound file not found! Make sure it is in src/main/resources/levelup.wav")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}