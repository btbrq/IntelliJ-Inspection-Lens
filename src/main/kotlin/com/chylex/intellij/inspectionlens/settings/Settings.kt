package com.chylex.intellij.inspectionlens.settings

import com.chylex.intellij.inspectionlens.LensSeverity
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.util.Collections.emptyList

@State(name = "com.chylex.intellij.inspectionlens.Settings", storages = [Storage("inspection-lens.xml")])
class Settings : PersistentStateComponent<Settings> {
    var isOnlyVcs: Boolean = true
    var showError: Boolean = true
    var showWarning: Boolean = true
    var showWeakWarning: Boolean = true
    var showOther: Boolean = true
    var showTypo: Boolean = true
    var includedExtensions: List<String> = emptyList()
    var excludedExtensions: List<String> = emptyList()

    @Nullable
    override fun getState(): Settings {
        return this
    }

    override fun loadState(@NotNull state: Settings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance: Settings
            get() = ApplicationManager.getApplication().getService(Settings::class.java)
    }

    fun isFileSupported(extension: String?): Boolean {
        val included = includedExtensions.isEmpty() || includedExtensions.any { it == extension }
        val notExcluded = excludedExtensions.isEmpty() || excludedExtensions.none { it == extension }
        return included && notExcluded
    }

    fun getLevels(): List<LensSeverity> {
        val levels = mutableListOf<LensSeverity>()
        addLevelIfTrue(levels, showError, LensSeverity.ERROR)
        addLevelIfTrue(levels, showError, LensSeverity.SERVER_PROBLEM)
        addLevelIfTrue(levels, showWarning, LensSeverity.WARNING)
        addLevelIfTrue(levels, showWeakWarning, LensSeverity.WEAK_WARNING)
        addLevelIfTrue(levels, showTypo, LensSeverity.TYPO)
        addLevelIfTrue(levels, showOther, LensSeverity.OTHER)
        return levels
    }

    private fun addLevelIfTrue(levels: MutableList<LensSeverity>, levelValue: Boolean, level: LensSeverity) {
        if (levelValue) {
            levels.add(level)
        }
    }
}