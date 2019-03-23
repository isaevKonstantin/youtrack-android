package com.konstantinisaev.youtrack.ui.auth

import android.os.Bundle
import android.view.View
import com.konstantinisaev.youtrack.ui.base.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(toolbar,getString(R.string.auth_login_toolbar),true)
    }
}