package com.ytone.longcare.common.utils

import kotlinx.datetime.*
import kotlin.time.Duration.Companion.days

object TimeUtils {

    /**
     * 获取当前 Instant (通常是 UTC 时间点)
     */
    fun getCurrentInstant(): Instant = Clock.System.now()

    /**
     * 获取当前毫秒级时间戳
     */
    fun getCurrentEpochMilliseconds(): Long = getCurrentInstant().toEpochMilliseconds()

    /**
     * 获取当前系统默认时区的 LocalDateTime
     */
    fun getCurrentLocalDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
        getCurrentInstant().toLocalDateTime(timeZone)

    /**
     * 获取当前系统默认时区的 LocalDate
     */
    fun getCurrentLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate =
        getCurrentInstant().toLocalDateTime(timeZone).date


    fun epochMillisToInstant(epochMillis: Long): Instant = Instant.fromEpochMilliseconds(epochMillis)

    fun epochMillisToLocalDateTime(epochMillis: Long, timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
        epochMillisToInstant(epochMillis).toLocalDateTime(timeZone)

    fun epochMillisToLocalDate(epochMillis: Long, timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate =
        epochMillisToLocalDateTime(epochMillis, timeZone).date

    fun localDateTimeToEpochMillis(localDateTime: LocalDateTime, timeZone: TimeZone): Long {
        // LocalDateTime 需要一个 TimeZone 才能明确地转换为一个 Instant (时间点)
        return localDateTime.toInstant(timeZone).toEpochMilliseconds()
    }

    fun localDateToEpochMillis(localDate: LocalDate, timeZone: TimeZone, atTime: LocalTime = LocalTime(0,0,0)): Long {
        // LocalDate 需要时间 和 TimeZone 才能明确地转换为一个 Instant
        return localDate.atTime(atTime).toInstant(timeZone).toEpochMilliseconds()
    }

    /**
     * 获取几天后的 Instant
     * @param instant 起始 Instant
     * @param daysToAdd 天数 (负数为几天前)
     */
    fun instantPlusDays(instant: Instant, daysToAdd: Int): Instant =
        instant.plus(daysToAdd.days) // Uses kotlin.time.Duration

    /**
     * 获取几年后的 Instant (注意闰年等复杂性，kotlinx-datetime 会处理)
     * @param instant 起始 Instant
     * @param yearsToAdd 年数
     * @param timeZone 用于计算的日历时区，因为“年”的长度依赖于日历系统
     */
    fun instantPlusYears(instant: Instant, yearsToAdd: Int, timeZone: TimeZone = TimeZone.currentSystemDefault()): Instant =
        instant.plus(yearsToAdd, DateTimeUnit.YEAR, timeZone)


    /**
     * 判断给定的 Instant 是否是今天 (基于指定时区)
     */
    fun isToday(instant: Instant, timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        val today = getCurrentLocalDate(timeZone)
        return instant.toLocalDateTime(timeZone).date == today
    }

    /**
     * 判断给定的 LocalDateTime 是否是今天 (假设 LocalDateTime 已经是目标时区的)
     * 如果需要和当前时区的“今天”比较，确保 `localDateTime` 也是当前时区的。
     */
    fun isToday(localDateTime: LocalDateTime, referenceTimeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        val todayInReferenceZone = getCurrentLocalDate(referenceTimeZone)
        return localDateTime.date == todayInReferenceZone
    }

    /**
     * 判断给定的 LocalDate 是否是今天
     */
    fun isToday(localDate: LocalDate, timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        return localDate == getCurrentLocalDate(timeZone)
    }


    /**
     * 判断给定的 Instant 是否是当月 (基于指定时区)
     */
    fun isThisMonth(instant: Instant, timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        val today = getCurrentLocalDate(timeZone)
        val dateFromInstant = instant.toLocalDateTime(timeZone).date
        return dateFromInstant.year == today.year && dateFromInstant.monthNumber == today.monthNumber
    }

    /**
     * 判断给定的 LocalDateTime 是否是当月
     */
    fun isThisMonth(localDateTime: LocalDateTime, referenceTimeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        val todayInReferenceZone = getCurrentLocalDate(referenceTimeZone)
        return localDateTime.year == todayInReferenceZone.year && localDateTime.monthNumber == todayInReferenceZone.monthNumber
    }

    /**
     * 判断给定的 Instant 是否是当年 (基于指定时区)
     */
    fun isThisYear(instant: Instant, timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        val today = getCurrentLocalDate(timeZone)
        return instant.toLocalDateTime(timeZone).date.year == today.year
    }

    /**
     * 判断给定的 LocalDateTime 是否是当年
     */
    fun isThisYear(localDateTime: LocalDateTime, referenceTimeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        val todayInReferenceZone = getCurrentLocalDate(referenceTimeZone)
        return localDateTime.year == todayInReferenceZone.year
    }

    /**
     * 判断两个 Instant 是否是同一天 (基于指定时区)
     */
    fun isSameDay(instant1: Instant, instant2: Instant, timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        return instant1.toLocalDateTime(timeZone).date == instant2.toLocalDateTime(timeZone).date
    }

}