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
import androidx.lifecycle.ViewModelProvider
import com.konstantinisaev.youtrack.core.api.HttpProtocolException
import com.konstantinisaev.youtrack.ui.base.R
import com.konstantinisaev.youtrack.ui.base.utils.toast
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

abstract class BaseFragment : Fragment()  {

    @get:LayoutRes
    protected abstract val layoutId: Int

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    private val handlers = hashMapOf<Class<*>,HashMap<Class<*>,(ViewState) -> Unit>>()

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

    protected fun registerHandler(viewStateClazz: Class<*>,clazz: Class<*>,handler: (ViewState) -> Unit){
        if(handlers.containsKey(viewStateClazz)){
            handlers.getValue(viewStateClazz)[clazz] = handler
        }else{
            handlers[viewStateClazz] = hashMapOf(clazz to handler)
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

    protected fun observe(viewState: ViewState){
        val owner = viewState.owner ?: return
        if(!handlers.containsKey(viewState.javaClass)){
            return
        }
        val handlersMap = handlers.getValue(viewState.javaClass)
        handlersMap[owner]?.invoke(viewState)
    }

    protected fun showError(errorState: ViewState.Error){
        when(errorState.reasonException){
            is HttpProtocolException.Unauthorized, is HttpProtocolException.Forbidden, is HttpProtocolException.BadRequest -> toast(R.string.http_error_unauthorized)
            else -> toast(R.string.error_runtime)
        }

    }

}