package com.example.redtop.presenter.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redtop.di.ServiceLocator
import com.example.redtop.domain.Publication
import com.example.redtop.presenter.base.PublicationViewState
import com.example.redtop.presenter.base.SingleLiveEvent
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class HomeViewModel : ViewModel() {

    //region Properties

    private val client = OkHttpClient()
    private val publicationRepository = ServiceLocator.providePublicationRepository()

    private val actionStateMutable = SingleLiveEvent<ActionState>(ActionState.None)
    val actionState: LiveData<ActionState> = actionStateMutable

    private val viewItemsMutable = MutableLiveData<List<PublicationViewState>>(emptyList())
    val viewItems: LiveData<List<PublicationViewState>> = viewItemsMutable

    //endregion

    //region Actions

    fun onUpdateViewState(){
        viewModelScope.launch {  invalidateViewItems() }
    }

    private suspend fun invalidateViewItems() {
        val publications = publicationRepository.getAll()
        val viewItems = publications.map{
            it.toViewState()
        }
        viewItemsMutable.value = viewItems
    }

    fun responsePublications(url:String){
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TAG_", "An error happened!")
            }
            override fun onResponse(call: Call, response: Response){
                val responseData = response.body!!.string()
                viewModelScope.launch { addPublications(responseData) }
            }
        })
    }

    private suspend fun addPublications(data: String){
        val jsonObject = JSONObject(data)
        val jsonArray: JSONArray = jsonObject.getJSONObject("data").getJSONArray("children")

        for (i in 0 until jsonArray.length()) {
            val publication = Publication(
                id = i+1,
                author = jsonArray.getJSONObject(i).getJSONObject("data").getString("subreddit_name_prefixed"),
                title = jsonArray.getJSONObject(i).getJSONObject("data").getString("title"),
                timeStamp = i,
                image = jsonArray.getJSONObject(i).getJSONObject("data").getString("thumbnail"),
                commentsCount = jsonArray.getJSONObject(i).getJSONObject("data").getString("num_comments")
            )
            publicationRepository.update(publication)
            invalidateViewItems()
        }
    }

    //endregion

    //region Nested

    sealed class ActionState{
        object None: ActionState()
        object ShowMessage: ActionState()
    }

    private fun Publication.toViewState(): PublicationViewState{
        return PublicationViewState(
            id = id,
            author = author,
            title = title,
            timeStamp = timeStamp,
            image = image,
            commentsCount = commentsCount
        )
    }

    //endregion

}