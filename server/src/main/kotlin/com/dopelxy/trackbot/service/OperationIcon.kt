package com.dopelxy.trackbot.service

object OperationIcon {

    fun get(operation: String): String {

        val text = operation.lowercase()

        return when {

            "прием" in text -> "📥"

            "обработка" in text -> "⚙️"

            "сортиров" in text -> "📦"

            "покинул" in text -> "🚚"

            "прибыло" in text -> "🏤"

            "вручен" in text -> "✅"

            "доставка" in text -> "🚛"

            "возврат" in text -> "↩️"

            "неудач" in text -> "⚠️"

            else -> "📦"
        }
    }
}