package com.konstantinisaev.youtrack.ui.auth.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.UrlFormatter
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.Base64Converter
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class RefreshTokenViewModel @Inject constructor(
    private val apiProvider: ApiProvider,
    private val basePreferencesAdapter: BasePreferencesAdapter,
    private val base64Converter: Base64Converter,
    coroutineHolder: CoroutineContextHolder
) : BaseViewModel<String>(coroutineHolder) {

    override suspend fun execute(params: String?): ViewState {
        val configDTO = basePreferencesAdapter.getServerConfig()
        val authTokenDTO = basePreferencesAdapter.getAuthToken()
        if(configDTO == null || authTokenDTO == null ){
            return ViewState.Empty(this::class.java)
        }
        apiProvider.enableAppCredentialsInHeader(base64Converter.convertToBase64("${configDTO.mobile.serviceId}:${configDTO.mobile.serviceSecret}"))
        val url = UrlFormatter.formatToLoginUrl(basePreferencesAdapter.getUrl(), configDTO.ring.url)
        val newTokenDTO = apiProvider.refreshToken(url,authTokenDTO.refreshToken).await()
        apiProvider.enableUserCredentialsInHeader(newTokenDTO.accessToken,newTokenDTO.tokenType)
        basePreferencesAdapter.setAuthToken(newTokenDTO)
        return ViewState.Success(this::class.java,"")
    }

}