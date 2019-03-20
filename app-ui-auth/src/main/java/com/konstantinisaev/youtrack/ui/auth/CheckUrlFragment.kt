package com.konstantinisaev.youtrack.ui.auth


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.konstantinisaev.youtrack.ui.base.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_check_url.*

class CheckUrlFragment : BaseFragment() {

    override var layoutId = R.layout.fragment_check_url

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setTitle(R.string.auth_check_url_toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }
}
