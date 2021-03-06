package com.konstantinisaev.youtrack.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.konstantinisaev.youtrack.ui.auth.di.AuthDiProvider
import com.konstantinisaev.youtrack.ui.auth.viewmodels.AuthByLoginPasswordParam
import com.konstantinisaev.youtrack.ui.auth.viewmodels.AuthByLoginPasswordValidator
import com.konstantinisaev.youtrack.ui.auth.viewmodels.AuthByLoginPasswordViewModel
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.AuthRouter
import com.konstantinisaev.youtrack.ui.base.utils.Settings
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import com.konstantinisaev.youtrack.ui.base.widget.afterTextChanged
import kotlinx.android.synthetic.main.fragment_auth.*
import javax.inject.Inject

class AuthFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_auth

    private lateinit var authByLoginPasswordViewModel: AuthByLoginPasswordViewModel
    @Inject
    lateinit var authRouter: AuthRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthDiProvider.getInstance().injectFragment(this)
        authByLoginPasswordViewModel = getViewModel(AuthByLoginPasswordViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarWithBackNavigation(toolbar,getString(R.string.auth_login_toolbar))
        fillDebugCredentialsIfAvailable(edtLogin,Settings.debugLogin)
        fillDebugCredentialsIfAvailable(edtPassword,Settings.debugPassword)

        edtLogin.afterTextChanged { bLogin.isEnabled = AuthByLoginPasswordValidator.validate(AuthByLoginPasswordParam(it,edtPassword.text.toString())) }
        edtPassword.afterTextChanged { bLogin.isEnabled = AuthByLoginPasswordValidator.validate(AuthByLoginPasswordParam(edtLogin.text.toString(),it)) }
        bLogin.isEnabled = AuthByLoginPasswordValidator.validate(AuthByLoginPasswordParam(edtLogin.text.toString(),edtPassword.text.toString()))

        bLogin.setOnClickListener { authByLoginPasswordViewModel.doAsyncRequest(AuthByLoginPasswordParam(edtLogin.text.toString(),edtPassword.text.toString())) }

        registerHandler(ViewState.Error::class.java,authByLoginPasswordViewModel) {
            showError(it)
        }
        registerHandler(ViewState.ValidationError::class.java,authByLoginPasswordViewModel) {
            toast(it.msgId)
        }
        registerHandler(ViewState.Success::class.java,authByLoginPasswordViewModel) {
            authRouter.showMain()
        }
    }

    private fun fillDebugCredentialsIfAvailable(edt: EditText, credentials: String){
        edt.setOnLongClickListener {
            return@setOnLongClickListener if(credentials.isNotEmpty()){
                edt.setText(credentials)
                edt.setSelection(edt.text.length)
                true
            }else{
                false
            }
        }
    }
}