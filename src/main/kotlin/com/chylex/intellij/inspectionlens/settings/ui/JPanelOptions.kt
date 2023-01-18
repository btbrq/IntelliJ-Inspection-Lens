package com.chylex.intellij.inspectionlens.settings.ui

import com.chylex.intellij.inspectionlens.settings.Settings
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBCheckBox
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.border.TitledBorder

class JPanelOptions(settings: Settings) : JPanel() {
    var onlyVcs: JBCheckBox? = null
    var showError: JBCheckBox? = null
    var showWarning: JBCheckBox? = null
    var showWeakWarning: JBCheckBox? = null
    var showTypo: JBCheckBox? = null
    var showOther: JBCheckBox? = null

    init {
        layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
        val titledBorder: TitledBorder = IdeBorderFactory.createTitledBorder("Options")
        border = titledBorder

        onlyVcs = JBCheckBox("Use only for changed code (only if VCS configured)", settings.isOnlyVcs)
        showError = JBCheckBox("Show ERROR level problems", settings.showError)
        showWarning = JBCheckBox("Show WARNING level problems", settings.showWarning)
        showWeakWarning = JBCheckBox("Show WEAK-WARNING level problems", settings.showWeakWarning)
        showOther = JBCheckBox("Show other level problems", settings.showOther)
        showTypo = JBCheckBox("Show TYPO level problems", settings.showTypo)
        add(onlyVcs)
        add(showError)
        add(showWarning)
        add(showWeakWarning)
        add(showTypo)
        add(showOther)
    }
}