package org.example

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBUI
import javax.swing.JComponent

class LevelWidget(private val project: Project) : CustomStatusBarWidget {
    private val label = JBLabel().apply {
        border = JBUI.Borders.empty(0, 5)
        toolTipText = "Twój aktualny poziom i exp"
    }

    init {
        updateText()
    }

    override fun ID(): String = "LevelWidget"

    override fun getComponent(): JComponent = label

    fun updateText() {
        val manager = service<LevelManager>()
        val level = manager.currentLevel

        val (rank, color) = getRankDetails(level)

        label.text = "<html><span style='color: $color; font-weight: bold;'>[$rank]</span> LVL: $level (${manager.currentExp}/${manager.expToNextLevel} XP)</html>"
    }

    override fun install(statusBar: StatusBar) {}
    override fun dispose() {}

    companion object {
        fun getRankDetails(level: Int): Pair<String, String> {
            return when {
                level in 1..9 -> "Novice" to "#A9A9A9"
                level in 10..19 -> "Coder" to "#4CAF50"
                level in 20..29 -> "Architect" to "#2196F3"
                else -> "Wizard" to "#9C27B0"
            }
        }
    }
}