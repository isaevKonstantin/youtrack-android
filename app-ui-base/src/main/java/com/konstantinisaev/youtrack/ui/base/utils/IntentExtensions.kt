package com.konstantinisaev.youtrack.ui.base.utils

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable

fun <T: Parcelable> Bundle?.getParcelableOrNull() : T? {
    return this?.getParcelable(Extra.PARCELABLE_KEY)
}

fun <T: Parcelable> Intent.getParcelableOrNull() : T? {
    return this.getParcelableExtra(Extra.PARCELABLE_KEY)
}

fun Intent.putParcelable(data: Parcelable){
    this.putExtra(Extra.PARCELABLE_KEY,data)
}

fun Intent.putParcelableList(data: ArrayList<Parcelable>){
    this.putExtra(Extra.PARCELABLE_LIST_KEY,data)
}

class Extra {

    companion object {

        const val ISSUE_COUNT = "issue_count"
        const val PARCELABLE_KEY = "parcelable"
        const val PARCELABLE_LIST_KEY = "parcelable_list"

    }
}
