package com.chylex.intellij.inspectionlens.settings

import com.chylex.intellij.inspectionlens.settings.ui.JPanelSettings
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class Configurable : Configurable {
    private var mySettingsComponent: JPanelSettings = JPanelSettings(Settings.instance)

    override fun createComponent(): JComponent {
        mySettingsComponent = JPanelSettings(Settings.instance)
        return mySettingsComponent
    }

    override fun apply() {

    }

    override fun reset() {

    }

    override fun isModified(): Boolean {
        return true
    }

    override fun getDisplayName(): String {
        return "Inspection Lens"
    }

}