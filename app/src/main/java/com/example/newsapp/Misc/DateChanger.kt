package com.example.newsapp.Misc
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateChanger {

        private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        private val outputDateFormat = SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.US)

     fun getDate(originalDate:String): String? {
        //Parse the String which holds the date and time (original "2018-04-15T08:35:35Z" to
        //"2018-04-15" and "08:35:35", and from "08:35:35" to "08:35")
        try {
            val d: Date? = inputDateFormat.parse(originalDate)

            return outputDateFormat.format(d!!)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }
}