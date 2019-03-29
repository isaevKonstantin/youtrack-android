package com.konstantinisaev.youtrack.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.konstantinisaev.youtrack.ui.auth.di.AuthDiProvider
import com.konstantinisaev.youtrack.ui.auth.viewmodels.AuthByLoginPasswordParam
import com.konstantinisaev.youtrack.ui.auth.viewmodels.AuthByLoginPasswordValidator
import com.konstantinisaev.youtrack.ui.auth.viewmodels.AuthByLoginPasswordViewModel
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.Routers
import com.konstantinisaev.youtrack.ui.base.utils.Settings
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import com.konstantinisaev.youtrack.ui.base.widget.afterTextChanged
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_auth

    private lateinit var authByLoginPasswordViewModel: AuthByLoginPasswordViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthDiProvider.getInstance(checkNotNull(context)).injectFragment(this)
        authByLoginPasswordViewModel = ViewModelProviders.of(this,viewModelFactory)[AuthByLoginPasswordViewModel::class.java]
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

        registerHandler(ViewState.Error::class.java,authByLoginPasswordViewModel::class.java) {
            showError(it as ViewState.Error)
        }
        registerHandler(ViewState.ValidationError::class.java,authByLoginPasswordViewModel::class.java) {
            toast((it as ViewState.ValidationError).msgId)
        }
        registerHandler(ViewState.Success::class.java,authByLoginPasswordViewModel::class.java) {
            Routers.authRouter.showMain()
        }

        authByLoginPasswordViewModel.observe(this, Observer { observe(it) })

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