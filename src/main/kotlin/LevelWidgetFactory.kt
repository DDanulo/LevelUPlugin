package org.example

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class LevelWidgetFactory : StatusBarWidgetFactory {
    override fun getId(): String = "LevelWidget"
    override fun getDisplayName(): String = "Level Tracker"
    override fun isAvailable(project: Project): Boolean = true
    override fun createWidget(project: Project): StatusBarWidget = LevelWidget(project)
    override fun disposeWidget(widget: StatusBarWidget) {
        Disposer.dispose(widget)
    }
    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true
}