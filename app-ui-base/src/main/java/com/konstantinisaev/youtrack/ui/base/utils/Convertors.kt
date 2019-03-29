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