package com.konstantinisaev.youtrack.ui.auth


import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.konstantinisaev.youtrack.ui.auth.di.SplashDiProvider
import com.konstantinisaev.youtrack.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_splash.*

private const val MAX_PROGRESS = 2000L
private const val INTERVAL = 200L

class SplashFragment : BaseFragment() {

    override var layoutId = R.layout.fragment_splash

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SplashDiProvider.injectFragment(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countDownTimer?.cancel()
        pbSplash.max = MAX_PROGRESS.toInt()
        countDownTimer = object : CountDownTimer(MAX_PROGRESS, INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = (pbSplash.max - millisUntilFinished).toInt()
                pbSplash.progress = progress
            }

            override fun onFinish() {
                pbSplash.progress = pbSplash.max
            }
        }.start()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
    }
}
