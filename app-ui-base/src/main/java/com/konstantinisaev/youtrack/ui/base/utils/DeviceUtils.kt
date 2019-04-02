package com.konstantinisaev.youtrack.ui.base.utils

import android.content.Context
import android.util.DisplayMetrics

object DeviceUtils {

	fun convertDpToPixelsById(dpId: Int, context: Context): Int {
		return context.resources.getDimensionPixelSize(dpId)
	}

	fun convertDpToPixel(dp:Float,context: Context): Int{
		val metrics = context.resources.displayMetrics
		return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
	}
}