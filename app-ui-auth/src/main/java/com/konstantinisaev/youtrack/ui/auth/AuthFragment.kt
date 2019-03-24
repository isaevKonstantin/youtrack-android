package com.konstantinisaev.youtrack.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.konstantinisaev.youtrack.ui.base.ui.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.Settings
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarWithBackNavigation(toolbar,getString(R.string.auth_login_toolbar))
        fillDebugCredentialsIfAvailable(edtLogin,Settings.debugLogin)
        fillDebugCredentialsIfAvailable(edtPassword,Settings.debugPassword)
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