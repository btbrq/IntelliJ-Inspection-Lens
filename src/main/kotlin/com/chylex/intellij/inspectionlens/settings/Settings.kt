package com.chylex.intellij.inspectionlens.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

@State(name = "com.chylex.intellij.inspectionlens.Settings", storages = [Storage("inspection-lens.xml")])
class Settings : PersistentStateComponent<Settings> {

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
}