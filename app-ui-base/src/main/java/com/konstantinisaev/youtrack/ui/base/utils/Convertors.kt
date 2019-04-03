package com.konstantinisaev.youtrack.ui.base.utils

import android.util.Base64

interface Base64Converter {

    fun convertToBase64(data: String) : String
}

class Base64ConverterImp : Base64Converter{

    override fun convertToBase64(data: String) : String{
        return Base64.encodeToString(data.toByteArray(), Base64.NO_WRAP)
    }
}

object InitialsConvertor {

    fun convertToInitials(title: String): String {
        if(title.isEmpty()){
            return ""
        }
        val newTitle = title.trim { it <= ' ' }
        var lastPos: Int
        if (newTitle.contains(" ")) {
            lastPos = newTitle.indexOf(" ")
            if (newTitle.length > lastPos + 1) {
                lastPos += 1
            } else {
            }
            val firstChars = newTitle[0].toString() + newTitle[lastPos].toString()
            return firstChars.toUpperCase()
        } else {
            lastPos = newTitle.length
            lastPos = if (lastPos > 1) {
                2
            } else {
                1
            }
            return newTitle.toUpperCase().substring(0, lastPos)
        }
    }

}