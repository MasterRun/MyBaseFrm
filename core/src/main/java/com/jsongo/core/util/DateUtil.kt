package com.jsongo.core.util

import com.jsongo.core.constant.DATE_STR_FORMAT_STR
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author ： jsongo
 * @date ： 2019/11/23 10:44
 * @desc :
 */
object DateUtil {

    /**
     * 一天的毫秒数
     */
    val ONE_DAY_MILLISECOND = TimeUnit.DAYS.toMillis(1)
    /**
     * 两天的毫秒数
     */
    val TWO_DAY_MILLISECOND = TimeUnit.DAYS.toMillis(2)

    /**
     * 获取当前时间的str
     */
    fun getCurrentTimeStr(formatStr: String = DATE_STR_FORMAT_STR) =
        SimpleDateFormat(formatStr, Locale.CHINA).format(Date())!!

    /**
     * 显示相对时间
     */
    fun relativeTime(date: Date?): String {
        if (date == null) {
            return ""
        }
        val calendar = GregorianCalendar()
        calendar.timeInMillis = date.time
        val now = Calendar.getInstance()

        val aimYear = calendar[Calendar.YEAR]
        val aimMonth = calendar[Calendar.MONTH]
        val aimDate = calendar[Calendar.DATE]
        val aimHour = calendar[Calendar.HOUR_OF_DAY]
        val aimMinute = calendar[Calendar.MINUTE]
        val monthStr = "${aimMonth + 1}"
        val dateStr = "$aimDate"//keepLengthOf2(aimDate)
        val hourStr = keepLengthOf2(aimHour)
        val minuteStr = keepLengthOf2(aimMinute)
        val timeStr = "$hourStr:$minuteStr"

        //如果大于当前时间，表示在未来
        if (calendar > now) {
            if (aimYear == now[Calendar.YEAR]) {
                if (aimMonth == now[Calendar.MONTH]) {
                    if (aimDate == now[Calendar.DATE]) {
                        return timeStr
                    }
                }
            }
            return "未来某天"
        }

        val milliSecondsOfDay = now.getMilliSecondsOfDay()
        val diffMillisecond = now.timeInMillis - calendar.timeInMillis
        if (diffMillisecond < milliSecondsOfDay) {
            return timeStr
        } else if (diffMillisecond < (ONE_DAY_MILLISECOND + milliSecondsOfDay)) {
            return "昨天$timeStr"
        } else if (diffMillisecond < (TWO_DAY_MILLISECOND + milliSecondsOfDay)) {
            return "前天$timeStr"
        }
        val sb = StringBuilder()

        if (aimYear < now[Calendar.YEAR]) {
            sb.append(aimYear).append("年")
        }
        sb.append(monthStr).append("月")
            .append(dateStr).append("日")
            .append(timeStr)

        return sb.toString()
    }

    fun keepLengthOf2(num: Int): String {
        if (num in 0..9) {
            return "0$num"
        }
        return num.toString()
    }

    //计算当前时间是当天的多少毫秒
    private fun Calendar.getMilliSecondsOfDay() =
        1000 * (((this[Calendar.HOUR_OF_DAY] * 60) + this[Calendar.MINUTE]) * 60 + this[Calendar.SECOND])

}