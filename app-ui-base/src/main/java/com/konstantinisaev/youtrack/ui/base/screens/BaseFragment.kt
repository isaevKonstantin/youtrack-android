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
import androidx.lifecycle.ViewModelProviders
import com.konstantinisaev.youtrack.core.api.HttpProtocolException
import com.konstantinisaev.youtrack.ui.base.R
import com.konstantinisaev.youtrack.ui.base.utils.toast
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelFactory
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject
import javax.inject.Named

abstract class BaseFragment : Fragment()  {

    @get:LayoutRes
    protected abstract val layoutId: Int

    @field:[Inject Named("baseFactory")]
    protected lateinit var baseViewModelFactory: ViewModelFactory

    @Inject
    @Named("featureFactory")
    protected lateinit var featureViewModelFactory: ViewModelFactory

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

    @Suppress("UNCHECKED_CAST")
    protected fun <T : ViewState> registerHandler(viewStateClazz: Class<out T>, baseViewModel: BaseViewModel<*>, handler: (T) -> Unit){
        if(!handlers.containsKey(viewStateClazz)){
            handlers[viewStateClazz] = hashMapOf()
        }
        handlers.getValue(viewStateClazz)[baseViewModel.javaClass] = handler as (ViewState) -> Unit
        if(!observersClass.contains(baseViewModel.javaClass)){
            baseViewModel.observe(this, Observer {
                observe(it)
            })
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

    fun <T : BaseViewModel<*>> getViewModel(clazz: Class<T>) : T {
        return when {
            baseViewModelFactory.contains(clazz) -> ViewModelProviders.of(this,baseViewModelFactory)[clazz]
            featureViewModelFactory.contains(clazz) -> ViewModelProviders.of(this,featureViewModelFactory)[clazz]
            else -> throw IllegalArgumentException("factory doesn't contain $clazz")
        }
    }

}