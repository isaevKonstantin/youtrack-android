package com.konstantinisaev.youtrack.ui.auth.viewmodels

import com.konstantinisaev.youtrack.ui.base.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import javax.inject.Inject

class ServerConfigViewModel @Inject constructor(
//	private val coroutineContextHolder: CoroutineContextHolder,
//	private val apiProvider: ApiProvider,
	private val basePreferencesAdapter: BasePreferencesAdapter

) : BaseViewModel<String,String>(){

    override fun execute(params: String?) : String {
        val url = basePreferencesAdapter.getUrl() ?: ""
        return ""
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
