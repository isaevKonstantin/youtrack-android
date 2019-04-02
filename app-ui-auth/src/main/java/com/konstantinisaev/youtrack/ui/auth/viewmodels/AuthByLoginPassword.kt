package com.konstantinisaev.youtrack.ui.auth.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.UrlFormatter
import com.konstantinisaev.youtrack.ui.auth.R
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.Validator
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class AuthByLoginPasswordViewModel @Inject constructor(
    private val apiProvider: ApiProvider,
    private val basePreferencesAdapter: BasePreferencesAdapter,
    coroutineHolder: CoroutineContextHolder
) : BaseViewModel<AuthByLoginPasswordParam>(coroutineHolder,AuthByLoginPasswordValidator){

    override suspend fun execute(params: AuthByLoginPasswordParam?): ViewState {
        val config = basePreferencesAdapter.getServerConfig() ?: return ViewState.ValidationError(ownerClass = this::class.java,msgId = R.string.auth_empty_server_config_error)
        val baseUrl = basePreferencesAdapter.getUrl()
        val loginUrl = UrlFormatter.formatToLoginUrl(baseUrl, config.ring.url)
        val authTokenDTO = apiProvider.login(loginUrl,params!!.login,params.password,"${config.ring.serviceId} ${config.mobile.serviceId}").await()
        basePreferencesAdapter.setAuthToken(authTokenDTO)
        apiProvider.enableUserCredentialsInHeader(authTokenDTO.accessToken,authTokenDTO.tokenType)
        return ViewState.Success(ownerClass = this::class.java,data = "")
    }
}

data class AuthByLoginPasswordParam(val login: String, val password: String)

object AuthByLoginPasswordValidator : Validator<AuthByLoginPasswordParam>{

    override var errorId = R.string.auth_empty_login_error

    override fun validate(params: AuthByLoginPasswordParam?): Boolean {
        if(params?.login.isNullOrBlank()){
            errorId = R.string.auth_empty_login_error
            return false
        }

        if(params?.password.isNullOrBlank()){
            errorId = R.string.auth_empty_password_error
            return false
        }

        return true
    }

}