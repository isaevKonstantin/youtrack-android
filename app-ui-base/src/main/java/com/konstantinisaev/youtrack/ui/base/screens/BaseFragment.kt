package com.konstantinisaev.youtrack.ui.base.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.konstantinisaev.youtrack.core.api.HttpProtocolException
import com.konstantinisaev.youtrack.ui.base.R
import com.konstantinisaev.youtrack.ui.base.utils.toast
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

abstract class BaseFragment : Fragment()  {

    @get:LayoutRes
    protected abstract val layoutId: Int

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    private val observersClass = mutableSetOf<Class<out BaseViewModel<*>>>()

    private val handlers = hashMapOf<Class<out ViewState>,HashMap<Class<*>,(ViewState) -> Unit>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handlers.clear()
    }

    protected fun registerHandler(viewStateClazz: Class<out ViewState>, baseViewModel: BaseViewModel<*>, handler: (ViewState) -> Unit){
        if(!handlers.containsKey(viewStateClazz)){
            handlers[viewStateClazz] = hashMapOf()
        }
        handlers.getValue(viewStateClazz)[baseViewModel.javaClass] = handler
        if(!observersClass.contains(baseViewModel.javaClass)){
            baseViewModel.observe(this, Observer { observe(it) })
            observersClass.add(baseViewModel.javaClass)
        }
    }

    protected fun setToolbarWithBackNavigation(toolbar: Toolbar, title: String){
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.title = title
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        }
    }

    protected fun toast(@StringRes resId: Int){
        context?.toast(resId)
    }

    protected fun toast(msg: String){
        context?.toast(msg)
    }


    protected fun showError(errorState: ViewState.Error){
        when(errorState.reasonException){
            is HttpProtocolException.Unauthorized, is HttpProtocolException.Forbidden, is HttpProtocolException.BadRequest -> toast(R.string.http_error_unauthorized)
            else -> toast(R.string.error_runtime)
        }

    }

    private fun observe(viewState: ViewState){
        val owner = viewState.owner ?: return
        if(!handlers.containsKey(viewState.javaClass)){
            return
        }
        val handlersMap = handlers.getValue(viewState.javaClass)
        handlersMap[owner]?.invoke(viewState)
    }

}