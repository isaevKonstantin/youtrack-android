package com.konstantinisaev.youtrack.ui.base.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, resId, duration).also { it.show() }

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).also { it.show() }

class Extra {

    companion object {

        const val ISSUE_COUNT = "issue_count"

        const val SELECT_LIST_ITEMS = "names"
        const val SELECT_LIST_TITLE = "title"
        const val SELECT_LIST_OPTIONS = "options"
    }
}

class RequestCode{

    companion object {

        const val UPDATE_FIELD = 100
    }

}