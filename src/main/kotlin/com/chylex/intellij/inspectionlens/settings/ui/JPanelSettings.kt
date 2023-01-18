package com.chylex.intellij.inspectionlens.settings.ui

import com.chylex.intellij.inspectionlens.settings.Settings
import java.awt.BorderLayout
import java.util.Collections.emptyList
import javax.swing.JPanel

class JPanelSettings(settings: Settings) : JPanel() {
    private var optionsPanel: JPanelOptions? = null
    private var extensionsPanel: JPanelExtensions? = null

    init {
        layout = BorderLayout()
        optionsPanel = JPanelOptions(settings)
        extensionsPanel = JPanelExtensions(settings)

        add(optionsPanel!!, BorderLayout.PAGE_START)
        add(extensionsPanel!!, BorderLayout.CENTER)
    }

    fun isOnlyVcs(): Boolean {
       return optionsPanel?.onlyVcs?.isSelected ?: false
    }

    fun setOnlyVcs(value: Boolean) {
        optionsPanel?.onlyVcs?.isSelected = value
    }

    fun showError(): Boolean {
        return optionsPanel?.showError?.isSelected ?: false
    }

    fun setShowError(value: Boolean) {
        optionsPanel?.showError?.isSelected = value
    }

    fun showWarning(): Boolean {
        return optionsPanel?.showWarning?.isSelected ?: false
    }

    fun setShowWarning(value: Boolean) {
        optionsPanel?.showWarning?.isSelected = value
    }

    fun showWeakWarning(): Boolean {
        return optionsPanel?.showWeakWarning?.isSelected ?: false
    }

    fun setShowWeakWarning(value: Boolean) {
        optionsPanel?.showWeakWarning?.isSelected = value
    }

    fun showTypo(): Boolean {
        return optionsPanel?.showTypo?.isSelected ?: false
    }

    fun setShowTypo(value: Boolean) {
        optionsPanel?.showTypo?.isSelected = value
    }

    fun showOther(): Boolean {
        return optionsPanel?.showOther?.isSelected ?: false
    }

    fun setShowOther(value: Boolean) {
        optionsPanel?.showOther?.isSelected = value
    }

    fun getIncludedExtensions(): List<String> {
        return extensionsPanel?.includedExtensions?.getItems() ?: emptyList()
    }

    fun setIncludedExtensions(extensions: List<String>) {
        extensionsPanel?.includedExtensions?.reset(extensions)
    }

    fun getExcludedExtensions(): List<String> {
        return extensionsPanel?.excludedExtensions?.getItems() ?: emptyList()
    }

    fun setExcludedExtensions(extensions: List<String>) {
        extensionsPanel?.excludedExtensions?.reset(extensions)
    }

}