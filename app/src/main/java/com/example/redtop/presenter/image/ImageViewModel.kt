package com.example.redtop.presenter.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redtop.di.ServiceLocator
import com.example.redtop.presenter.base.SingleLiveEvent
import kotlinx.coroutines.launch

class ImageViewModel: ViewModel() {

    //region Properties

    private val actionStateMutable = SingleLiveEvent<ActionState>(ActionState.None)
    val actionState: LiveData<ActionState> = actionStateMutable

    private val publicationMediaMutable = MutableLiveData("")
    val publicationMedia: LiveData<String> = publicationMediaMutable

    private var publicationID: Int = 0
    private val publicationRepository = ServiceLocator.providePublicationRepository()

    //endregion

    //region Actions

    fun onUpdateViewState(id: Int) {
        viewModelScope.launch {
            val publication = publicationRepository.get(id) ?: throw IllegalStateException()
            publicationMediaMutable.value = publication.media
            publicationID = id
            actionStateMutable.value = ActionState.SetImage(publication.media)
        }
    }

    fun onSaveImage() {
        actionStateMutable.value = ActionState.SaveImage(publicationMediaMutable.value!!)
    }


    //endregion

    //region Nested

    sealed class ActionState{
        object None: ActionState()
        object Back: ActionState()
        object ShowMessage: ActionState()
        data class SaveImage(val publicationMedia: String):ActionState()
        data class SetImage(val publicationMedia: String): ActionState()

    }

    //endregion
}