package com.chylex.intellij.inspectionlens.settings

import com.chylex.intellij.inspectionlens.settings.ui.JPanelSettings
import com.intellij.openapi.options.Configurable
import com.intellij.ui.layout.selected
import javax.swing.JComponent

class Configurable : Configurable {
    private var component: JPanelSettings = JPanelSettings(Settings.instance)

    override fun createComponent(): JComponent {
        component = JPanelSettings(Settings.instance)
        return component
    }

    override fun apply() {
        Settings.instance.isOnlyVcs = component.onlyVcs!!.isSelected
//on apply reinstall all current editors
//        when installing editors - check if only VCS is selected
    }

    override fun reset() {
        component.onlyVcs!!.isSelected = Settings.instance.isOnlyVcs
    }

    override fun isModified(): Boolean {
        return true
    }

    override fun getDisplayName(): String {
        return "Inspection Lens"
    }

}