package com.konstantinisaev.youtrack.ui.base.viewmodels

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.UrlFormatter
import com.konstantinisaev.youtrack.core.api.models.PermissionHolder
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import javax.inject.Inject

class GetPermissionsViewModel @Inject constructor(private val apiProvider: ApiProvider,
                                                  private val basePreferencesAdapter: BasePreferencesAdapter,
                                                  private val permissionHolder: PermissionHolder,
                                                  coroutineContextHolder: CoroutineContextHolder

) : BaseViewModel<String>(coroutineContextHolder) {

    override suspend fun execute(params: String?): ViewState {
        val config = basePreferencesAdapter.getServerConfig() ?: return ViewState.Error(this::class.java)
        val permissions = apiProvider.getPermissions(UrlFormatter.formatToPermissionUrl(basePreferencesAdapter.getUrl(),config.ring.url),config.ring.serviceId).await()
        permissionHolder.init(permissions)
        return ViewState.Success(this::class.java,permissions)
    }
}