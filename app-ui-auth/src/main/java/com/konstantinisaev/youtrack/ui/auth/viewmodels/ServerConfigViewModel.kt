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
        for (serverUrl in urls) {
            try {
                val configDTO = apiProvider.getServerConfig(UrlFormatter.formatToServerConfigUrl(serverUrl)).await()
                if(configDTO.mobile.serviceId.isEmpty() || configDTO.mobile.serviceSecret.isEmpty()){
                    return ViewState.Error()
                }
                val savedUrl = if(serverUrl.endsWith("/")){
                    serverUrl
                }else{
                    "$serverUrl/"
                }
                basePreferencesAdapter.setUrl(savedUrl)
                basePreferencesAdapter.setServerConfig(configDTO)
                apiProvider.enableAppCredentialsInHeader(configDTO.mobile.serviceId,configDTO.mobile.serviceSecret)
                viewState = ViewState.Success(data = "")
                break
            }catch (ex: Exception){
                ex.printStackTrace()
                viewState = ViewState.Error()
            }
        }
        return viewState
    }

    //	val liveData = SingleLiveEvent<ViewResponse<ServerVersionViewResult>>()

//	fun checkServerUrl(url: String){
//		launch(coroutineContextHolder.ioCoroutineContext() + job) {
//			var viewResponse = ViewResponse<ServerVersionViewResult>()
//			val urls = UrlFormatter.formatToHostUrls(url)
//			loop@ for (serverUrl in urls) {
//				try {
//					val configResp = apiProvider.getServerConfig(UrlFormatter.formatToServerConfigUrl(serverUrl)).await()
//					if(configResp.isSuccessful && configResp.body() != null && configResp.body()!!.mobile.serviceId.isNotEmpty() && configResp.body()!!.mobile.serviceSecret.isNotEmpty()){
//						val savedUrl = if(serverUrl.endsWith("/")){
//							serverUrl
//						} else {
//							"$serverUrl/"
//						}
//						basePreferencesAdapter.setUrl(savedUrl)
//						basePreferencesAdapter.serverConfig = configResp.body()
//						apiProvider.enableAppCredentialsInHeader(configResp.body()!!.mobile.serviceId,configResp.body()!!.mobile.serviceSecret)
//
//						viewResponse = viewResponse.copy(error = ViewError.EMPTY,data = ServerVersionViewResult(0,""))
//						break@loop
//					}else{
//						viewResponse = viewResponse.copy(error = ViewError.INVALID_SERVER_CONFIG,data = null)
//					}
//				}catch (ex: Exception){
//					Timber.e(ex)
//					viewResponse = viewResponse.copy(error = ViewError.HTTP_RUNTIME)
//				}
//			}
//
//			liveData.postValue(viewResponse)
//		}
//
//	}
}
