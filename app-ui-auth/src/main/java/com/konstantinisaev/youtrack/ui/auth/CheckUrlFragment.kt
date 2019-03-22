package com.konstantinisaev.youtrack.ui.auth


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.konstantinisaev.youtrack.ui.auth.di.SplashDiProvider
import com.konstantinisaev.youtrack.ui.auth.viewmodels.ServerConfigViewModel
import com.konstantinisaev.youtrack.ui.base.ui.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.UrlValidator
import com.konstantinisaev.youtrack.ui.base.widget.afterTextChanged
import kotlinx.android.synthetic.main.fragment_check_url.*

class CheckUrlFragment : BaseFragment() {

    override var layoutId = R.layout.fragment_check_url

    private lateinit var viewModel: ServerConfigViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SplashDiProvider.getInstance(checkNotNull(context)).injectFragment(this)
        viewModel = ViewModelProviders.of(this,viewModelFactory)[ServerConfigViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setTitle(R.string.auth_check_url_toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

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
            viewModel.doAsyncRequest(edtUrl.text.toString())
        }

        viewModel.observe(this, Observer { observe(it) })
    }
}
