package com.example.movie.utils

import android.util.Log
import com.example.movie.utils.Constants.TAG
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun convertDateFormat(
        date: String,
        currentDateFormat: String,
        formatToConvert: String
    ): String {
        val userDateFormat: DateFormat = SimpleDateFormat(currentDateFormat, Locale.getDefault())
        var newDate: Date? = null
        try {
            newDate = userDateFormat.parse(date)
        } catch (ex: ParseException) {
            Log.e(TAG, ex.message.toString())
        }
        val dateFormatNeeded: DateFormat = SimpleDateFormat(formatToConvert, Locale.getDefault())
        return newDate?.let { dateFormatNeeded.format(it) } ?: kotlin.run { Constants.NA }
    }
}