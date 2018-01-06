package com.mimi.translatereminder.utils

import android.content.Context
import com.mimi.translatereminder.R
import java.util.*

/**
 * Created by Mimi on 14/12/2017.
 * This class is used for converting the time in a readable format
 */
class TimeUtils {
    companion object {
        private val SECOND = 1000L
        val MINUTE = SECOND * 60
        val HOUR = MINUTE * 60
        val DAY = HOUR * 24
        val MONTH = DAY * 30
    }

    fun getTime(timeInMillis: Long, context: Context): String {
        return if (timeInMillis < Calendar.getInstance().timeInMillis)
            getTimeInPast(timeInMillis, context)
        else
            getTimeInFuture(timeInMillis, context)
    }

    private fun getTimeInPast(timeInMillis: Long, context: Context): String {
        val now = Calendar.getInstance()
        return when (now.timeInMillis - timeInMillis) {
            in 0..MINUTE -> context.getString(R.string.less_then_a_minute_ago)
            in MINUTE..HOUR -> context.getString(R.string.number_minutes_ago,
                    getMinutes(now.timeInMillis - timeInMillis))
            in HOUR..DAY -> context.getString(R.string.number_hours_ago,
                    getHours(now.timeInMillis - timeInMillis))
            in DAY..2 * DAY -> context.getString(R.string.yesterday)
            in DAY..MONTH -> context.getString(R.string.number_days_ago,
                    getDays(now.timeInMillis - timeInMillis))
            else -> context.getString(R.string.in_a_month)
        }

    }

    private fun getTimeInFuture(timeInMillis: Long, context: Context): String {
        val now = Calendar.getInstance()
        return when (timeInMillis - now.timeInMillis) {
            in 0..MINUTE -> context.getString(R.string.in_a_minute)
            in MINUTE..HOUR -> context.getString(R.string.in_number_minutes, getMinutes(timeInMillis - now.timeInMillis))
            in HOUR..DAY -> context.getString(R.string.in_number_hours, getHours(timeInMillis - now.timeInMillis))
            in DAY..2 * DAY -> context.getString(R.string.tomorrow)
            in DAY..MONTH -> context.getString(R.string.in_number_days, getDays(timeInMillis - now.timeInMillis))
            else -> context.getString(R.string.in_a_month)
        }

    }

    fun getDays(time: Long): Int {
        return (time / DAY).toInt()
    }
    fun getHours(time: Long): Int {
        return (time / HOUR).toInt()
    }
    fun getMinutes(time: Long): Int {
        return (time / MINUTE).toInt()
    }
}
