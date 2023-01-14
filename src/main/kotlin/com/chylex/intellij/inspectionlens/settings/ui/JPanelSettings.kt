package com.chylex.intellij.inspectionlens.settings.ui

import com.chylex.intellij.inspectionlens.settings.Settings
import com.intellij.ui.components.JBCheckBox
import javax.swing.BoxLayout
import javax.swing.JPanel

class JPanelSettings(settings: Settings) : JPanel() {
    var onlyVcs: JBCheckBox? = null

    init {
        layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
        onlyVcs = JBCheckBox("Use only for changed code (only if VCS configured)", settings.isOnlyVcs)
        add(onlyVcs)
    }

}