package com.konstantinisaev.youtrack.issuelist

import com.konstantinisaev.youtrack.core.api.ApiProvider
import com.konstantinisaev.youtrack.core.api.CoroutineContextHolder
import com.konstantinisaev.youtrack.core.api.UrlFormatter
import com.konstantinisaev.youtrack.ui.base.data.BasePreferencesAdapter
import com.konstantinisaev.youtrack.ui.base.utils.InitialsConvertor
import com.konstantinisaev.youtrack.ui.base.viewmodels.BaseViewModel
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val apiProvider: ApiProvider,
                                           private val basePreferencesAdapter: BasePreferencesAdapter,
                                           coroutineHolder: CoroutineContextHolder
) : BaseViewModel<String>(coroutineHolder) {

    override suspend fun execute(params: String?): ViewState {
        val baseUrl = basePreferencesAdapter.getUrl()
        val userDTO = apiProvider.getProfile(baseUrl).await()
        val imageUrl = if(!userDTO.avatar.isNullOrEmpty()){
            userDTO.avatar
        }else{
            userDTO.avatarUrl
        }
        userDTO.formattedImageUrl = UrlFormatter.formatToImageUrl(baseUrl,imageUrl.orEmpty())
        userDTO.initials = InitialsConvertor.convertToInitials(userDTO.fullName.orEmpty())
        return ViewState.Success(this::class.java,userDTO)
    }
}
