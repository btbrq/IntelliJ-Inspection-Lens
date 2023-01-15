package com.chylex.intellij.inspectionlens

import com.intellij.codeInsight.actions.VcsFacadeImpl
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.InlayProperties
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

/**
 * Manages visible inspection lenses for an [Editor].
 */
class EditorInlayLensManager private constructor(private val editor: Editor, private val onlyVcs: Boolean, private val levels: List<LensSeverity>) {
	companion object {
		private val KEY = Key<EditorInlayLensManager>(EditorInlayLensManager::class.java.name)

		/**
		 * Highest allowed severity for the purposes of sorting multiple highlights at the same offset.
		 * The value is a little higher than the highest [com.intellij.lang.annotation.HighlightSeverity], in case severities with higher values are introduced in the future.
		 */
		private const val MAXIMUM_SEVERITY = 500
		private const val MAXIMUM_POSITION = ((Int.MAX_VALUE / MAXIMUM_SEVERITY) * 2) - 1

		fun getOrCreate(editor: Editor, onlyVcs: Boolean, levels: List<LensSeverity>): EditorInlayLensManager {
			return editor.getUserData(KEY) ?: EditorInlayLensManager(editor, onlyVcs, levels).also { editor.putUserData(KEY, it) }
		}

		fun remove(editor: Editor) {
			val manager = editor.getUserData(KEY)
			if (manager != null) {
				manager.hideAll()
				editor.putUserData(KEY, null)
			}
		}

		fun removeInRange(editor: Editor, fullyCommitted: Boolean) {
			val manager = editor.getUserData(KEY)
			if (manager != null) {
				val psiFile = PsiDocumentManager.getInstance(editor.project!!).getPsiFile(editor.document)
				val changedRangesInfo = VcsFacadeImpl.getInstance().getChangedTextRanges(editor.project!!, psiFile!!)
				if (fullyCommitted || changedRangesInfo.isEmpty()) {
					println("is empty")
					manager.hideAll()
				} else {
					println("ranges: $changedRangesInfo")

					val iterator = manager.inlays.keys
							.filter {
								!notCommitted(it.startOffset, it.endOffset, changedRangesInfo)
							}.toMutableList().listIterator()
					while (iterator.hasNext()) {
						manager.hide(iterator.next())
					}
				}
			}
		}

		private fun notCommitted(startOffset: Int, endOffset: Int, changedRangesInfo: List<TextRange>): Boolean {
			return changedRangesInfo.any { it.startOffset <= startOffset && it.endOffset >= endOffset }
		}

		private fun getInlayHintOffset(info: HighlightInfo): Int {
			// Ensures a highlight at the end of a line does not overflow to the next line.
			return info.actualEndOffset - 1
		}

		internal fun getInlayHintPriority(position: Int, severity: Int): Int {
			// Sorts highlights first by position on the line, then by severity.
			val positionBucket = position.coerceIn(0, MAXIMUM_POSITION) * MAXIMUM_SEVERITY
			// The multiplication can overflow, but subtracting overflowed result from Int.MAX_VALUE does not break continuity.
			val positionFactor = Integer.MAX_VALUE - positionBucket
			val severityFactor = severity.coerceIn(0, MAXIMUM_SEVERITY) - MAXIMUM_SEVERITY
			return positionFactor + severityFactor
		}
	}

	private val inlays = mutableMapOf<RangeHighlighter, Inlay<LensRenderer>>()

	private fun highlighterInChangedRanges(highlighter: RangeHighlighter, changedRangesInfo: List<TextRange>): Boolean {
		return changedRangesInfo.any {
			it.startOffset <= highlighter.startOffset && it.endOffset >= highlighter.endOffset
		}
	}

	fun show(highlighterWithInfo: HighlighterWithInfo) {
		if (levels.contains(LensSeverity.from(highlighterWithInfo.severity()))) {
			val psiFile = PsiDocumentManager.getInstance(editor.project!!).getPsiFile(editor.document)
			if (onlyVcs && VcsFacadeImpl.getInstance().isFileUnderVcs(psiFile!!)) {
				showOnlyVcs(highlighterWithInfo, psiFile)
			} else {
				showAllChanges(highlighterWithInfo)
			}
		}
	}

	private fun showAllChanges(highlighterWithInfo: HighlighterWithInfo) {
		val (highlighter, info) = highlighterWithInfo
		val currentInlay = inlays[highlighter]
		if (currentInlay != null && currentInlay.isValid) {
			currentInlay.renderer.setPropertiesFrom(info)
			currentInlay.update()
		} else {
			val offset = getInlayHintOffset(info)
			val priority = getInlayHintPriority(info)
			val renderer = LensRenderer(info)
			val properties = InlayProperties().relatesToPrecedingText(true).disableSoftWrapping(true).priority(priority)

			editor.inlayModel.addAfterLineEndElement(offset, properties, renderer)?.let {
				inlays[highlighter] = it
			}
		}
	}

	private fun showOnlyVcs(highlighterWithInfo: HighlighterWithInfo, psiFile: PsiFile) {
		val (highlighter, info) = highlighterWithInfo
		val changedRangesInfo = VcsFacadeImpl.getInstance().getChangedTextRanges(editor.project!!, psiFile)
		if (highlighterInChangedRanges(highlighter, changedRangesInfo)) {
			val currentInlay = inlays[highlighter]
			if (currentInlay != null && currentInlay.isValid) {
				currentInlay.renderer.setPropertiesFrom(info)
				currentInlay.update()
			}
			else {
				val offset = getInlayHintOffset(info)
				val priority = getInlayHintPriority(info)
				val renderer = LensRenderer(info)
				val properties = InlayProperties().relatesToPrecedingText(true).disableSoftWrapping(true).priority(priority)

				editor.inlayModel.addAfterLineEndElement(offset, properties, renderer)?.let {
					inlays[highlighter] = it
				}
			}
		}
	}

	fun showAll(highlightersWithInfo: Collection<HighlighterWithInfo>) {
		executeInInlayBatchMode(highlightersWithInfo.size) { highlightersWithInfo.forEach(::show) }
	}

	fun hide(highlighter: RangeHighlighter) {
		inlays.remove(highlighter)?.dispose()
	}

	fun hideAll() {
		executeInInlayBatchMode(inlays.size) { inlays.values.forEach(Inlay<*>::dispose) }
		inlays.clear()
	}

	private fun getInlayHintPriority(info: HighlightInfo): Int {
		val startOffset = info.actualStartOffset
		val positionOnLine = startOffset - getLineStartOffset(startOffset)
		return getInlayHintPriority(positionOnLine, info.severity.myVal)
	}

	private fun getLineStartOffset(offset: Int): Int {
		val position = editor.offsetToLogicalPosition(offset)
		return editor.document.getLineStartOffset(position.line)
	}

	private fun executeInInlayBatchMode(operations: Int, block: () -> Unit) {
		editor.inlayModel.execute(operations > 1000, block)
	}
}
