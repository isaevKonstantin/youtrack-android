package com.konstantinisaev.youtrack.ui.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.konstantinisaev.youtrack.ui.base.utils.toast
import javax.inject.Inject

abstract class BaseFragment : Fragment()  {

    @get:LayoutRes
    protected abstract val layoutId: Int

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    protected fun toast(@StringRes resId: Int){
        val context = this.context ?: return
        context.toast(resId)
    }

    protected fun toast(msg: String){
        val context = this.context ?: return
        context.toast(msg)
    }

}