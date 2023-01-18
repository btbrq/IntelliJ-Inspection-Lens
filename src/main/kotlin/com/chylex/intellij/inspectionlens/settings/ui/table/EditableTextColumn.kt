package com.chylex.intellij.inspectionlens.settings.ui.table

import com.intellij.util.ui.table.TableModelEditor.EditableColumnInfo

class EditableTextColumn : EditableColumnInfo<TableListElement, String>() {
    override fun valueOf(element: TableListElement): String {
        return element.value
    }

    override fun setValue(element: TableListElement, value: String) {
        element.value = value
    }
}