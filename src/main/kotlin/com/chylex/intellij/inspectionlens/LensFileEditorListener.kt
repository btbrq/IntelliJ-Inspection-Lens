package com.chylex.intellij.inspectionlens

import com.chylex.intellij.inspectionlens.settings.Settings
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.ex.FileEditorWithProvider
import com.intellij.openapi.vfs.VirtualFile

/**
 * Listens for newly opened editors, and installs a [LensMarkupModelListener] on them.
 */
class LensFileEditorListener : FileEditorManagerListener {
    override fun fileOpenedSync(source: FileEditorManager, file: VirtualFile, editorsWithProviders: MutableList<FileEditorWithProvider>) {
        val settings: Settings = Settings.instance
        for (editorWrapper in editorsWithProviders) {
            val fileEditor = editorWrapper.fileEditor
            if (fileEditor is TextEditor && settings.isFileSupported(fileEditor.file.extension)) {
                LensMarkupModelListener.install(fileEditor, settings.isOnlyVcs, settings.getLevels())
            }
        }
    }

}
