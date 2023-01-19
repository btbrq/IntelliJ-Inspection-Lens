package com.chylex.intellij.inspectionlens.settings.ui

import com.chylex.intellij.inspectionlens.InspectionLensBundle.message
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
        val titledBorder: TitledBorder = IdeBorderFactory.createTitledBorder(message("options.title"))
        border = titledBorder

        onlyVcs = JBCheckBox(message("options.onlyVcs"), settings.isOnlyVcs)
        showError = JBCheckBox(message("options.showError"), settings.showError)
        showWarning = JBCheckBox(message("options.showWarning"), settings.showWarning)
        showWeakWarning = JBCheckBox(message("options.showWeakWarning"), settings.showWeakWarning)
        showTypo = JBCheckBox(message("options.showTypo"), settings.showTypo)
        showOther = JBCheckBox(message("options.showOther"), settings.showOther)
        add(onlyVcs)
        add(showError)
        add(showWarning)
        add(showWeakWarning)
        add(showTypo)
        add(showOther)
    }
}