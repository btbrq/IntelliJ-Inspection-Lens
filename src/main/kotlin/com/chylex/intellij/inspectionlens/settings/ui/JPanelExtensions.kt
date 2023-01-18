package com.chylex.intellij.inspectionlens.settings.ui

import com.chylex.intellij.inspectionlens.settings.Settings
import com.chylex.intellij.inspectionlens.settings.ui.table.TableStringList
import com.intellij.ui.JBSplitter

class JPanelExtensions(settings: Settings) : JBSplitter(false, 0.5f) {
    var includedExtensions: TableStringList? = null
    var excludedExtensions: TableStringList? = null

    init {
        includedExtensions = TableStringList(settings.includedExtensions, "Include extensions", "Include ex")
        excludedExtensions = TableStringList(settings.excludedExtensions, "Exclude extensions", "Exclude ex")

        firstComponent = includedExtensions?.getPanel()
        secondComponent = excludedExtensions?.getPanel()
    }
}