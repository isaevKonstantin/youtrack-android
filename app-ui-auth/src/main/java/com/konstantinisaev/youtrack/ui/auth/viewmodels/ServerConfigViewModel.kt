package com.konstantinisaev.youtrack.ui.auth.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.UrlFormatter
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class ServerConfigViewModel @Inject constructor(
    private val apiProvider: ApiProvider,
    private val basePreferencesAdapter: BasePreferencesAdapter,
    coroutineHolder: CoroutineContextHolder
) : BaseViewModel<String>(coroutineHolder){

    override suspend fun execute(params: String?) : ViewState {
        val url: String = params.takeIf { !it.isNullOrEmpty() } ?: basePreferencesAdapter.getUrl()
        val formattedUrl = url.takeIf { it.isNotEmpty() } ?: return ViewState.Empty()
        val urls = UrlFormatter.formatToHostUrls(formattedUrl)
        var viewState: ViewState = ViewState.Empty()
        if(!apiProvider.isInitialized()){
            apiProvider.init("http://default.com", arrayOf())
        }
        for (serverUrl in urls) {
            try {
                val configDTO = apiProvider.getServerConfig(UrlFormatter.formatToServerConfigUrl(serverUrl)).await()
                if(configDTO.mobile.serviceId.isEmpty() || configDTO.mobile.serviceSecret.isEmpty()){
                    return ViewState.Error(ownerClass = this::class.java)
                }
                val savedUrl = if(serverUrl.endsWith("/")){
                    serverUrl
                }else{
                    "$serverUrl/"
                }
                basePreferencesAdapter.setUrl(savedUrl)
                basePreferencesAdapter.setServerConfig(configDTO)
                apiProvider.enableAppCredentialsInHeader(configDTO.mobile.serviceId,configDTO.mobile.serviceSecret)
                viewState = ViewState.Success(ownerClass = this::class.java,data = "")
                break
            }catch (ex: Exception){
                ex.printStackTrace()
                viewState = ViewState.Error(ownerClass = this::class.java)
            }
        }
        return viewState
    }
}
