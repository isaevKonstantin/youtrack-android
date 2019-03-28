package com.konstantinisaev.youtrack.ui.auth


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.konstantinisaev.youtrack.ui.auth.di.SplashDiProvider
import com.konstantinisaev.youtrack.ui.auth.viewmodels.ServerConfigViewModel
import com.konstantinisaev.youtrack.ui.base.ui.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.Routers
import com.konstantinisaev.youtrack.ui.base.utils.Settings
import com.konstantinisaev.youtrack.ui.base.utils.UrlValidator
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import com.konstantinisaev.youtrack.ui.base.widget.afterTextChanged
import kotlinx.android.synthetic.main.fragment_check_url.*

class CheckUrlFragment : BaseFragment() {

    override var layoutId = R.layout.fragment_check_url

    private lateinit var serverConfigViewModel: ServerConfigViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SplashDiProvider.getInstance(checkNotNull(context)).injectFragment(this)
        serverConfigViewModel = ViewModelProviders.of(this,viewModelFactory)[ServerConfigViewModel::class.java]

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarWithBackNavigation(toolbar,getString(R.string.auth_check_url_toolbar))

        edtUrl.afterTextChanged {
            if(UrlValidator.validate(it)){
                layEnterUrl.error = null
                bCheckUrl.isEnabled = true
            }else{
                bCheckUrl.isEnabled = false
                layEnterUrl.error = getString(R.string.auth_check_url_invalid_error)
            }
        }

        bCheckUrl.setOnClickListener {
            serverConfigViewModel.doAsyncRequest(edtUrl.text.toString())
        }

        edtUrl.setOnLongClickListener {
            if(Settings.debugUrl.isNotEmpty()){
                edtUrl.setText(Settings.debugUrl)
                edtUrl.setSelection(edtUrl.text?.length ?: 0)
                return@setOnLongClickListener true
            }else{
                return@setOnLongClickListener false
            }
        }

        registerHandler(ViewState.Error::class.java,serverConfigViewModel::class.java) {
            toast("error")
        }
        registerHandler(ViewState.ValidationError::class.java,serverConfigViewModel::class.java) {
            toast("validation")
        }
        registerHandler(ViewState.Success::class.java,serverConfigViewModel::class.java) {
            Routers.authRouter.showAuth()
        }

        serverConfigViewModel.observe(this, Observer { observe(it) })
    }
}
