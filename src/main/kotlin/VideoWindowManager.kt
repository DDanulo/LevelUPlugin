package org.example

import com.intellij.openapi.project.Project

object VideoWindowManager {
    var currentDialog: VideoDialog? = null

    fun toggle(project: Project): Boolean {
        if (currentDialog != null && currentDialog!!.isShowing) {
            currentDialog!!.close(0)
            currentDialog = null
            return false
        } else {
            currentDialog = VideoDialog(project)
            currentDialog!!.show()
            return true
        }
    }

    fun isShowing(): Boolean {
        return currentDialog != null && currentDialog!!.isShowing
    }
}