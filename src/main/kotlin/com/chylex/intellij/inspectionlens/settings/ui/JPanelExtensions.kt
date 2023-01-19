package com.chylex.intellij.inspectionlens.settings.ui

import com.chylex.intellij.inspectionlens.InspectionLensBundle.message
import com.chylex.intellij.inspectionlens.settings.Settings
import com.chylex.intellij.inspectionlens.settings.ui.table.TableStringList
import com.intellij.ui.JBSplitter

class JPanelExtensions(settings: Settings) : JBSplitter(false, 0.5f) {
    var includedExtensions: TableStringList? = null
    var excludedExtensions: TableStringList? = null

    init {
        includedExtensions = TableStringList(settings.includedExtensions, message("included.extensions.title"), message("included.extensions.tooltip"))
        excludedExtensions = TableStringList(settings.excludedExtensions, message("excluded.extensions.title"), message("excluded.extensions.tooltip"))

        firstComponent = includedExtensions?.getPanel()
        secondComponent = excludedExtensions?.getPanel()
    }
}