package org.example

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction

class ToggleVideoAction : ToggleAction("Video Player", "Toggle video player", AllIcons.Actions.Execute) {
    override fun isSelected(e: AnActionEvent): Boolean {
        return VideoWindowManager.isShowing()
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        e.project?.let { VideoWindowManager.toggle(it) }
    }
}