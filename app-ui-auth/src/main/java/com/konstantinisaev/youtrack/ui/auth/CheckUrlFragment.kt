package com.konstantinisaev.youtrack.ui.auth


import android.os.Bundle
import android.view.View
import com.konstantinisaev.youtrack.ui.auth.di.AuthDiProvider
import com.konstantinisaev.youtrack.ui.auth.viewmodels.ServerConfigViewModel
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.AuthRouter
import com.konstantinisaev.youtrack.ui.base.utils.Settings
import com.konstantinisaev.youtrack.ui.base.utils.UrlValidator
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import com.konstantinisaev.youtrack.ui.base.widget.afterTextChanged
import kotlinx.android.synthetic.main.fragment_check_url.*
import javax.inject.Inject

class CheckUrlFragment : BaseFragment() {

    override var layoutId = R.layout.fragment_check_url

    private lateinit var serverConfigViewModel: ServerConfigViewModel
    @Inject
    lateinit var authRouter: AuthRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthDiProvider.getInstance().injectFragment(this)
        serverConfigViewModel = getViewModel(ServerConfigViewModel::class.java)
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

        registerHandler(ViewState.Error::class.java,serverConfigViewModel) {
            showError(it)
        }
        registerHandler(ViewState.Success::class.java,serverConfigViewModel) {
            authRouter.showAuth()
        }
    }
}
