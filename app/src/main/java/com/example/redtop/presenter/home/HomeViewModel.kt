package com.example.redtop.presenter.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.redtop.presenter.base.SingleLiveEvent

class HomeViewModel : ViewModel() {

    //region Properties

    private val actionStateMutable = SingleLiveEvent<ActionState>(ActionState.None)
    val actionState: LiveData<ActionState> = actionStateMutable

    //endregion

    //region Actions

    fun onUpdateViewState(){
        //Nothing
    }

    //endregion

    //region Nested

    sealed class ActionState{
        object None: ActionState()
        object ShowMessage: ActionState()
    }

    //endregion

}