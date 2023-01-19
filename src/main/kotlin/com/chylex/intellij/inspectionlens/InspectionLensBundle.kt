package com.chylex.intellij.inspectionlens

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls

object InspectionLensBundle : DynamicBundle("messages.InspectionLensBundle") {
    fun message(key: String): @Nls String {
        return if (containsKey(key)) getMessage(key) else ""
    }
}