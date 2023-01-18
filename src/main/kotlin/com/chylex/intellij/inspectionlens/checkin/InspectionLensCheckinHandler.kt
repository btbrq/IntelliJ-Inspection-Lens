package com.chylex.intellij.inspectionlens.checkin

import com.chylex.intellij.inspectionlens.EditorInlayLensManager
import com.chylex.intellij.inspectionlens.settings.Settings
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.VcsException
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.impl.PartialChangesUtil
import com.intellij.openapi.vfs.VirtualFile

class InspectionLensCheckinHandler(private val panel: CheckinProjectPanel) : CheckinHandler() {
    private var fileChanges: MutableMap<Editor, Boolean>? = null
    override fun beforeCheckin(): ReturnResult {
        val settings = Settings.instance
        if (settings.isOnlyVcs) {
            val fileEditorManager = FileEditorManager.getInstance(panel.project)

            fileChanges = mutableMapOf()
            for (editor in fileEditorManager.allEditors.filterIsInstance<TextEditor>().filter { wasCommitted(it.file) }) {
                if (settings.isFileSupported(editor.file.extension)) {
                    val fullyCommitted = isFullyCommitted(editor)
                    fileChanges?.put(editor.editor, fullyCommitted)
                }
            }
        }
        return ReturnResult.COMMIT
    }

    override fun checkinSuccessful() {
        fileChanges?.forEach {
            invokeLater {
                EditorInlayLensManager.removeInRange(it.key, it.value)
            }
        }
        fileChanges?.clear()
        fileChanges = null
    }

    override fun checkinFailed(exception: MutableList<VcsException>?) {
        fileChanges?.clear()
        fileChanges = null
    }

    private fun isFullyCommitted(editor: TextEditor): Boolean {
        val partialTracker = PartialChangesUtil.getPartialTracker(panel.project, editor.file)
        return partialTracker == null || !partialTracker.hasPartialChangesToCommit()
    }

    private fun wasCommitted(file: VirtualFile): Boolean {
        return panel.virtualFiles.any { it == file }
    }

    private fun isVcsEnabled(fileEditor: TextEditor): Boolean {
        println("settings vcs ${Settings.instance.isOnlyVcs}")
        return if (Settings.instance.isOnlyVcs) isVcsForProjectAndFile(fileEditor) else false
    }

    private fun isVcsForProjectAndFile(fileEditor: TextEditor) : Boolean {
        //todo check if project is under vcs and file is tracked
        return true
    }
}