package com.konstantinisaev.youtrack.ui.auth


import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.konstantinisaev.youtrack.ui.auth.di.AuthDiProvider
import com.konstantinisaev.youtrack.ui.auth.viewmodels.RefreshTokenViewModel
import com.konstantinisaev.youtrack.ui.base.screens.BaseFragment
import com.konstantinisaev.youtrack.ui.base.utils.AuthRouter
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

private const val MAX_PROGRESS = 5000L
private const val INTERVAL = 200L

class SplashFragment : BaseFragment() {

    override var layoutId = R.layout.fragment_splash

    private var countDownTimer: CountDownTimer? = null

    private lateinit var viewModel: RefreshTokenViewModel
    @Inject
    lateinit var authRouter: AuthRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthDiProvider.getInstance().injectFragment(this)
        viewModel = ViewModelProviders.of(this,viewModelFactory)[RefreshTokenViewModel::class.java]
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
                when(viewModel.lastViewState){
                    is ViewState.Success<*> -> authRouter.showMain()
                    else -> authRouter.showServerUrl()

                }
            }

        }.start()
        registerHandler(ViewState.Error::class.java,viewModel){
            pbSplash.progress = pbSplash.max
            countDownTimer?.cancel()
            authRouter.showServerUrl()
        }
        registerHandler(ViewState.Empty::class.java,viewModel){
            pbSplash.progress = pbSplash.max
            countDownTimer?.cancel()
            authRouter.showServerUrl()
        }
        registerHandler(ViewState.Success::class.java,viewModel){
            pbSplash.progress = pbSplash.max
            countDownTimer?.cancel()
            authRouter.showMain()
        }
        viewModel.doAsyncRequest()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
    }
}
