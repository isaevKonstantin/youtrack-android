package com.konstantinisaev.youtrack.ui.base.utils

import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT = "dd.MM.yyyy"
private const val DATE_FORMAT_WITH_HOURS = "dd.MM.yyyy hh:mm"
private const val LOCALIZED_DATE_FORMAT = "d MMMM"
private const val LOCALIZED_WITH_YEAR_DATE_FORMAT = "d MMMM, yyyy"

fun Date.toFormattedString(): String {
	return formatDate(DATE_FORMAT, this)
}

fun Date.toHourAndMinutesString(): String{
	val calendar = Calendar.getInstance(TimeZone.getDefault())
	calendar.time = this
	val hour = calendar.get(Calendar.HOUR_OF_DAY)
	val minutes = calendar.get(Calendar.MINUTE)
	return String.format(Locale.getDefault(),
		" %02d:%02d", hour, minutes)
}

fun Date.toFullDate(): String{
	return formatDate(DATE_FORMAT_WITH_HOURS,this)
}

fun Date.toLocalizedString(): String{
	return formatDate(LOCALIZED_DATE_FORMAT,this)
}

fun Date.toLocalizedWithYearString(): String{
	return formatDate(LOCALIZED_WITH_YEAR_DATE_FORMAT,this)
}

private fun formatDate(dateFormatStr: String,date: Date): String{
	val format = SimpleDateFormat(dateFormatStr, Locale.getDefault())
	format.timeZone = TimeZone.getDefault()
	return format.format(date)
}

class DateUtils {

	companion object {

		fun toDate(time: Long?): Date? {
			if(time == null){
				return null
			}
			val calendar = Calendar.getInstance(TimeZone.getDefault())
			calendar.timeInMillis = time
			return calendar.time
		}


	}
}