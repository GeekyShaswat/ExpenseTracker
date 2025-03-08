package com.example.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun formatDateToHumanReadable(dateMillis: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(dateMillis)
    }
    fun formatDateForChart(dateMillis: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        return dateFormat.format(dateMillis)
    }
    fun formatDecimalTo2Digit(amount: Double): String {
        return String.format("%.2f", amount)
    }
    fun getMilliFromDate(dateFormat: String?): Long {
        // If the provided date string is null or blank, return the current time
        if (dateFormat.isNullOrBlank()) {
            return Date().time
        }

        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            // Safely parse the string and if null, fallback to current date
            formatter.parse(dateFormat)?.time ?: Date().time
        } catch (e: ParseException) {
            // In case of a parsing error, fallback to current date
            Date().time
        }
    }

}