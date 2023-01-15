package com.chylex.intellij.inspectionlens.settings

import com.chylex.intellij.inspectionlens.settings.ui.JPanelSettings
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class Configurable : Configurable {
    private val settings = Settings.instance
    private var component: JPanelSettings = JPanelSettings(settings)

    override fun createComponent(): JComponent {
        component = JPanelSettings(settings)
        return component
    }

    override fun apply() {
        settings.isOnlyVcs = component.onlyVcs!!.isSelected
        settings.showError = component.showError!!.isSelected
        settings.showWarning = component.showWarning!!.isSelected
        settings.showWeakWarning = component.showWeakWarning!!.isSelected
        settings.showTypo = component.showTypo!!.isSelected
        settings.showOther = component.showOther!!.isSelected
//on apply reinstall all current editors
//        when installing editors - check if only VCS is selected
    }

    override fun reset() {
        component.onlyVcs!!.isSelected = Settings.instance.isOnlyVcs
        component.showError!!.isSelected = Settings.instance.showError
        component.showWarning!!.isSelected = Settings.instance.showWarning
        component.showWeakWarning!!.isSelected = Settings.instance.showWeakWarning
        component.showTypo!!.isSelected = Settings.instance.showTypo
        component.showOther!!.isSelected = Settings.instance.showOther
    }

    override fun isModified(): Boolean {
        return true
    }

    override fun getDisplayName(): String {
        return "Inspection Lens"
    }

}