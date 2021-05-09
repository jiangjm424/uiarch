package com.grank.uicommon.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.grank.logger.Log
import com.grank.uicommon.R

import android.annotation.SuppressLint
import android.content.Context
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * 字符串、数字转换工具
 */

private const val TAG = "NumberExt"
const val TEN_THS = 10000.0 //万
const val HD_THS = 100000.0 //十万
const val TEN_MM = 100000000.0 //亿
const val HD_MM = 1000000000.0 //十亿
const val DAY: Long = 24 * 60 * 60 * 1000 // 86400000
const val MONTH: Long = 30 * DAY           //2592000000
const val YEAR = MONTH * 12;

/**
 * 时间戳转日期字符串 mm月dd日
 *
 * @param time Unix时间戳
 * @return
 */
@SuppressLint("SimpleDateFormat")
fun Long.stampToChineseString(context: Context): String? {
    val format =
        SimpleDateFormat(context.getString(R.string.data_mm_dd))
    return format.format(this * 1000)
}

/**
 * 时间戳转日期字符串 yyyy年mm月
 *
 * @param time Unix时间戳
 * @return
 */
@SuppressLint("SimpleDateFormat")
fun Long.stampToChineseWithoutYearString(): String? {
    val format = SimpleDateFormat("MM月dd日 HH:mm")
    return format.format(this * 1000)
}

/**
 * 时间戳转日期字符串 yyyy年mm月
 *
 * @param pattern 格式
 * @return
 */
@SuppressLint("SimpleDateFormat")
fun Long.stampToDateStringForPattern(pattern: String): String? {
    val format = SimpleDateFormat(pattern)
    return format.format(this * 1000)
}


/**
 * 时间戳转日期字符串 yyyy-mm-dd
 *
 * @param time Unix时间戳
 * @return
 */
@SuppressLint("SimpleDateFormat")
fun Long.stampToDataString(splitString: String = "-"): String? {
    val format = SimpleDateFormat("yyyy${splitString}MM${splitString}dd")
    return format.format(this * 1000)
}

/**
 * 时间戳转日期字符串 N年前/N月前/N周前/N天前/N小时前/N分前/刚刚
 *
 * @param time Unix时间戳
 * @return
 */
fun Long.stampToTimeOffsetString(context: Context): String? {
    val current = System.currentTimeMillis() / 1000
    val second = current - this
    val minute = second / 60
    val hour = minute / 60
    val date = hour / 24
    val week = date / 7
    val month = date / 30
    val year = month / 12
    return when {
        year > 0 -> {
            year.toString() + context.getString(R.string.years_ago)
        }
        month > 0 -> {
            month.toString() + context.getString(R.string.months_ago)
        }
        week > 0 -> {
            week.toString() + context.getString(R.string.weeks_ago)
        }
        date > 0 -> {
            date.toString() + context.getString(R.string.days_ago)
        }
        hour > 0 -> {
            hour.toString() + context.getString(R.string.hours_ago)
        }
        minute > 0 -> {
            minute.toString() + context.getString(R.string.minutes_ago)
        }
        minute == 0L -> {
            context.getString(R.string.just)
        }
        else -> {
            ""
        }
    }
}

/**
 * 时间戳转日期字符串 将于N天后失效/将于N小时后失效/将于N分后失效/已失效，以后请在3天内领取
 *
 * @param time Unix时间戳
 * @return
 */
fun Long.stampToTimeExpiredString(context: Context): String? {
    val current = System.currentTimeMillis() / 1000
    val second = this - current
    var minute = second / 60
    val minuteMod = second % 60
    var hour = minute / 60
    val hourMod = minute % 60
    var date = hour / 24
    val dateMod = hour % 24
    return when {
        date > 0 -> {
            if (dateMod > 0) {
                date++
            }
            context.getString(R.string.days_expired, date)
        }
        hour > 0 -> {
            if (hourMod > 0) {
                hour++
            }
            context.getString(R.string.hours_expired, hour)
        }
        minute > 0 -> {
            if (minuteMod > 0) {
                minute++
            }
            context.getString(R.string.minutes_expired, minute)
        }
        second > 0 -> {
            context.getString(R.string.minutes_expired, 1)
        }
        else -> {
            context.getString(R.string.already_expired)
        }
    }
}

/**
 * 日期判断，为社区详情定制，三天前原样输出，三天内按今天、明天、昨天输出字符串
 * @param time
 * @return
 */
@SuppressLint("SimpleDateFormat")
fun String.stampToTimeOffsetString(context: Context): String? {
    var ret = this
    var date: Date? = null
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        date = sdf.parse(this)
        val second = (System.currentTimeMillis() - date.time) / 1000
        val days = second / (24 * 60 * 60)
        when {
            days <= 0 -> {
                ret = context.getString(R.string.today)
            }
            days == 1L -> {
                ret = context.getString(R.string.yesterday)
            }
            days == 2L -> {
                ret = context.getString(R.string.the_day_before_yesterday)
            }
            else -> {
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "stampToTimeOffsetString $e")
    } finally {
    }
    return ret
}

/**
 * 时间戳转为日期
 * @param time
 * @return
 */
@SuppressLint("SimpleDateFormat")
fun String.stampToDateAndTimeString(): String? {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val timeStamp = this.toLong()
    return format.format(timeStamp * 1000)
}

/**
 * 时间戳转为日期
 * @param time
 * @return
 */
@SuppressLint("SimpleDateFormat")
fun String.stampToDateAndTimeStringWithoutSeconds(splitString: String = "-"): String? {
    val format = SimpleDateFormat("yyyy${splitString}MM${splitString}dd HH:mm")
    val timeStamp = this.toLong()
    return format.format(timeStamp * 1000)
}


/**
 * 将文件大小转换成KB,MB,GB格式，小数点保留后两位
 *
 * @param size 文件大小byte
 * @return
 */
fun Long.fileSizeFormat(): String? {
    val bytes = StringBuffer()
    val format = DecimalFormat("###.00")
    if (this >= 1024 * 1024 * 1024) {
        val i = this / (1024.0 * 1024.0 * 1024.0)
        bytes.append(format.format(i)).append("GB")
    } else if (this >= 1024 * 1024) {
        val i = this / (1024.0 * 1024.0)
        bytes.append(format.format(i)).append("MB")
    } else if (this >= 1024) {
        val i = this / 1024.0
        bytes.append(format.format(i)).append("KB")
    } else if (this < 1024) {
        if (this <= 0) {
            bytes.append("0B")
        } else {
            bytes.append(this.toInt()).append("B")
        }
    }
    return bytes.toString()
}

/**
 * 将下载次数转换成String格式
 *
 * @param str 文件下载次数
 * @return
 */
fun String.fileDownFormat(context: Context): String? {
    return this.chineseFormat(context) + context.getString(R.string.install_count)
}


/**
 * 将下载次数转换成String格式
 *
 * @param count 文件下载次数
 * @return
 */
fun Long.fileDownFormat(context: Context): String? {
    return this.chineseFormat(context) + context.getString(R.string.install_count)
}

fun String?.isMobileNo(): Boolean {
    if (this.isNullOrEmpty()) return false
    val p =
        Pattern.compile("^(?:\\+?86)?1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[235-8]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[0-35-9]\\d{2}|66\\d{2})\\d{6}\$");
    val m = p.matcher(this)
    return m.find()
}

/**
 * 将长整型转化成***亿 或****万形式 进行四舍五入
 *
 * @param str
 * @return
 */
fun String.chineseFormat(context: Context): String {
    if (this.isEmpty()) {
        return ""
    }
    val numbers = this.toLong()
    val result = StringBuffer()
    when {
        numbers >= HD_MM -> {
            val i = numbers / TEN_MM
            result.append(DecimalFormat("#").format(i)).append(context.getString(R.string.one_hundred_million))
        }
        numbers >= TEN_MM -> {
            val i = numbers / TEN_MM
            result.append(DecimalFormat("0.0").format(i)).append(context.getString(R.string.one_hundred_million))
        }
        numbers >= HD_THS -> {
            val i = numbers / TEN_THS
            result.append(DecimalFormat("#").format(i)).append(context.getString(R.string.ten_thousand))
        }
        numbers >= TEN_THS -> {
            val i = numbers / TEN_THS
            result.append(DecimalFormat("0.0").format(i)).append(context.getString(R.string.ten_thousand))
        }
        else -> {
            result.append(DecimalFormat("#").format(numbers))
        }
    }
    return result.toString()
}

/**
 * 将长整型转化成***亿 或****万形式 进行四舍五入
 *
 * @param numbers
 * @return
 */
fun Long.chineseFormat(context: Context): String {
    val result = StringBuffer()
    when {
        this >= HD_MM -> {
            val i = this / TEN_MM
            result.append(DecimalFormat("#").format(i)).append(context.getString(R.string.one_hundred_million))
        }
        this >= TEN_MM -> {
            val i = this / TEN_MM
            result.append(DecimalFormat("0.0").format(i)).append(context.getString(R.string.one_hundred_million))
        }
        this >= HD_THS -> {
            val i = this / TEN_THS
            result.append(DecimalFormat("#").format(i)).append(context.getString(R.string.ten_thousand))
        }
        this >= TEN_THS -> {
            val i = this / TEN_THS
            result.append(DecimalFormat("0.0").format(i)).append(context.getString(R.string.ten_thousand))
        }
        else -> {
            result.append(DecimalFormat("#").format(this))
        }
    }
    return result.toString()
}

/**
 * 将长整型转化成***亿 或****万形式 进行四舍五入
 *
 * @param numbers
 * @return
 */
fun Long.chineseFormat(): String {
    val result = StringBuffer()
    when {
        this >= HD_MM -> {
            val i = this / TEN_MM
            result.append(DecimalFormat("#").format(i))
        }
        this >= TEN_MM -> {
            val i = this / TEN_MM
            result.append(DecimalFormat("0.0").format(i))
        }
        this >= HD_THS -> {
            val i = this / TEN_THS
            result.append(DecimalFormat("#").format(i))
        }
        this >= TEN_THS -> {
            val i = this / TEN_THS
            result.append(DecimalFormat("0.0").format(i))
        }
        else -> {
            result.append(DecimalFormat("#").format(this))
        }
    }
    return result.toString()
}


/**
 * 将长整型转化成***亿 或****万形式 进行四舍五入，带空格
 *
 * @param numbers
 * @return
 */
fun Long.chineseFormatWithSpace(context: Context): String {
    val result = StringBuffer()
    when {
        this >= HD_MM -> {
            val i = this / TEN_MM
            result.append(DecimalFormat("#").format(i)).append(" ")
                .append(context.getString(R.string.one_hundred_million))
        }
        this >= TEN_MM -> {
            val i = this / TEN_MM
            result.append(DecimalFormat("0.0").format(i)).append(" ")
                .append(context.getString(R.string.one_hundred_million))
        }
        this >= HD_THS -> {
            val i = this / TEN_THS
            result.append(DecimalFormat("#").format(i)).append(" ").append(context.getString(R.string.ten_thousand))
        }
        this >= TEN_THS -> {
            val i = this / TEN_THS
            result.append(DecimalFormat("0.0").format(i)).append(" ").append(context.getString(R.string.ten_thousand))
        }
        else -> {
            result.append(DecimalFormat("#").format(this))
        }
    }
    return result.toString()
}

/**
 * 数字格式化
 *
 * @param number   要四舍五入的数字
 * @param decimal  保留小数点的位数
 * @param rounding 是否进行四舍五入
 * @return
 */
fun Double.formatNumber(decimal: Int, rounding: Boolean): String? {
    return formatNumberToDouble(this, decimal, rounding).toString()
}

private fun formatNumberToDouble(number: Double, decimal: Int, rounding: Boolean): Double {
    val bigDecimal = BigDecimal(number)
    return if (rounding) {
        bigDecimal.setScale(decimal, RoundingMode.HALF_UP).toDouble()
    } else {
        bigDecimal.setScale(decimal, RoundingMode.DOWN).toDouble()
    }
}

/**
 * 用户评论点赞数显示字串格式化
 *
 * @param count
 * @return
 */
fun Int.favorCountFormat(): String? {
    var ret = ""
    if (this >= 0 && this < TEN_THS) {
        ret = this.toString()
    } else if (this >= TEN_THS) {
        ret = "999+"
    }
    return ret
}

@RequiresApi(Build.VERSION_CODES.N)
fun Date.format(format: String): String = android.icu.text.SimpleDateFormat(format).format(this)

/**
 * 用来获取应用最后一次使用时间的
 */
@RequiresApi(Build.VERSION_CODES.N)
fun Long.getUsedTime(context: Context): String {
    val current: Long = System.currentTimeMillis()
    if (this == 0L || current < this) return context.getString(R.string.never_used)
    val lastArray = Date(this).format("yyyy-MM-dd")
    val currentArray = Date(current).format("yyyy-MM-dd")
    Log.d("uninstall1", "lastUsed = $lastArray , current = $currentArray")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = current
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    // 用一天的结束时间来算时间差，区分昨天和今天
    val endForCurrentDay = calendar.timeInMillis // 当前日期24点时间戳
    val internalTime = endForCurrentDay - this
    when {
        internalTime >= YEAR -> {
            val year = (internalTime / YEAR).toInt()
            return "${year}" + context.getString(R.string.years_ago) + context.getString(R.string.used)
        }
        internalTime >= MONTH -> {
            val month = (internalTime / MONTH).toInt()
            return "${month}" + context.getString(R.string.months_ago) + context.getString(R.string.used)
        }
        internalTime >= DAY -> {
            val day = (internalTime / DAY).toInt()
            return "${day}" + context.getString(R.string.days_ago_1) + context.getString(R.string.used)
        }
        else -> {
            return context.getString(R.string.today_used)
        }
    }
}


/**
 * 隐藏字串
 *
 * @return
 */
fun String.maskText(): String {
    var str = this
    if (!this.isEmpty() && this.length >= 2) {
        val sb = StringBuilder(this);
        sb.replace(1, this.length - 1, "*");
        str = sb.toString()
    }
    return str
}

/**
 * 长整型转时间（小时和分钟）
 *
 * @return
 */
fun Long.toHoursAndMinutes(): String? {
    if (this <= 0) {
        return null
    }

    var minutes = this / 60
    val leftoverSeconds = this % 60

    val hours = minutes / 60
    var leftoverMinutes = minutes % 60

    return if (hours > 0) {
        if (leftoverSeconds > 0) {
            leftoverMinutes += 1
        }
        "${hours}小时${leftoverMinutes}分钟"
    } else {
        if (leftoverSeconds > 0) {
            minutes += 1
        }
        "${minutes}分钟"
    }
}