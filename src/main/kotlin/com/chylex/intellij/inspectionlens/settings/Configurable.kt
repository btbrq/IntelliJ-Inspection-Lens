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
        settings.isOnlyVcs = component.isOnlyVcs()
        settings.showError = component.showError()
        settings.showWarning = component.showWarning()
        settings.showWeakWarning = component.showWeakWarning()
        settings.showTypo = component.showTypo()
        settings.showOther = component.showOther()
        settings.includedExtensions = component.getIncludedExtensions()
        settings.excludedExtensions = component.getExcludedExtensions()
        println("included ${component.getIncludedExtensions()}")
        println("excluded ${component.getExcludedExtensions()}")

//on apply reinstall all current editors
//        when installing editors - check if only VCS is selected
    }

    override fun reset() {
        component.setOnlyVcs(Settings.instance.isOnlyVcs)
        component.setShowError(Settings.instance.showError)
        component.setShowWarning(Settings.instance.showWarning)
        component.setShowWeakWarning(Settings.instance.showWeakWarning)
        component.setShowTypo(Settings.instance.showTypo)
        component.setShowOther(Settings.instance.showOther)
        component.setIncludedExtensions(Settings.instance.includedExtensions)
        component.setExcludedExtensions(Settings.instance.excludedExtensions)
    }

    override fun isModified(): Boolean {
        return true
    }

    override fun getDisplayName(): String {
        return "Inspection Lens"
    }

}