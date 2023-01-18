package com.chylex.intellij.inspectionlens.settings.ui.table

import com.intellij.ui.AnActionButtonRunnable
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.table.TableView
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import java.util.function.Consumer
import javax.swing.JPanel
import javax.swing.border.TitledBorder

class TableStringList(items: List<String>, title: String, tooltipText: String) {
    private val model: ListTableModel<TableListElement> = ListTableModel<TableListElement>(columns, items.map { TableListElement(it) })
    private val table: TableView<TableListElement> = TableView(model)
    private var panel: JPanel? = null

    init {
        table.setShowGrid(false)
        panel = ToolbarDecorator.createDecorator(table)
                .setAddAction(addAction())
                .disableUpDownActions()
                .createPanel()
        val titledBorder: TitledBorder = IdeBorderFactory.createTitledBorder(title)
        panel!!.border = titledBorder
        panel!!.toolTipText = tooltipText
    }

    fun getPanel(): JPanel? {
        return panel
    }

    fun getItems(): List<String> {
        return model.items.map { it.value}
    }

    fun reset(items: List<String>) {
        clearTable()
        fillTable(items)
    }

    private fun clearTable() {
        while (model.rowCount > 0) {
            model.removeRow(0)
        }
    }

    private fun fillTable(items: List<String>) {
        items.forEach(Consumer { e: String -> model.addRow(TableListElement(e)) })
    }

    private fun addAction(): AnActionButtonRunnable {
        return AnActionButtonRunnable {
            val row = TableListElement("")
            val index: Int = model.rowCount
            model.addRow(row)
            table.setRowSelectionInterval(index, index)
        }
    }

    private val columns: Array<ColumnInfo<*, *>>
        get() = arrayOf(
                EditableTextColumn()
        )

}