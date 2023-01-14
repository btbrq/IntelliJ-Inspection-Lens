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
		for (editorWrapper in editorsWithProviders) {
			val fileEditor = editorWrapper.fileEditor
			if (fileEditor is TextEditor) {
//				todo check file extension
				LensMarkupModelListener.install(fileEditor, isVcsEnabled(fileEditor))
			}
		}
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
