package com.konstantinisaev.youtrack.ui.base.utils

import android.util.Patterns
import android.webkit.URLUtil
import com.konstantinisaev.youtrack.ui.base.R

interface Validator<P>{

    var errorId: Int

    fun validate(params: P? = null) : Boolean

}

object UrlValidator : Validator<String>{

    override var errorId: Int = R.string.auth_check_url_invalid_error

    override fun validate(params: String?): Boolean {
        return URLUtil.isNetworkUrl(params) && Patterns.WEB_URL.matcher(params).matches()
    }

}
