package com.chylex.intellij.inspectionlens.settings.ui

import com.chylex.intellij.inspectionlens.settings.Settings
import javax.swing.BoxLayout
import javax.swing.JPanel

class JPanelSettings(settings: Settings) : JPanel() {
    init {
        layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
    }

}