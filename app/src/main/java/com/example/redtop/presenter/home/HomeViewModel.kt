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
            if(validatePublications(jsonArray.getJSONObject(i))){
                val publication = Publication(
                    id = i+1,
                    author = jsonArray
                        .getJSONObject(i)
                        .getJSONObject("data")
                        .getString("subreddit_name_prefixed"),
                    title = jsonArray
                        .getJSONObject(i)
                        .getJSONObject("data")
                        .getString("title"),
                    selftext = jsonArray
                        .getJSONObject(i)
                        .getJSONObject("data")
                        .getString("selftext"),
                    timeStamp = getPublicationTimeDifference(
                        jsonArray
                            .getJSONObject(i)
                            .getJSONObject("data")
                            .getString("created")
                            .toDouble()
                            .toLong()
                    ),
                    media = jsonArray
                        .getJSONObject(i)
                        .getJSONObject("data")
                        .getString("url"),
                    commentsCount = jsonArray
                        .getJSONObject(i)
                        .getJSONObject("data")
                        .getString("num_comments")
                )
            publicationRepository.update(publication)
            invalidateViewItems()
            }
        }
    }

    private fun validatePublications(jsonObject:JSONObject):Boolean{
        try {
            return jsonObject.getJSONObject("data").getString("is_gallery") != "true"
        }
        catch (e: Exception){
            Log.d("TAG_", e.toString())
            return jsonObject.getJSONObject("data").getString("is_video") != "true"
        }
    }

    private fun getPublicationTimeDifference(timeStamp:Long):String{
        val currentTimeStamp:Long = System.currentTimeMillis()/1000
        val differenceTimeStamp = currentTimeStamp.minus(timeStamp)

        val differenceInMinutes = differenceTimeStamp / 60
        val differenceInHours = differenceTimeStamp / 3600
        val differenceInDays = differenceTimeStamp / 86400

        return if(differenceInDays == 0L){
            if (differenceInHours == 0L){
                if(differenceInMinutes == 0L){
                    "a few moment ago"
                }
                else{
                    "$differenceInMinutes min. ago"
                }
            } else{
                "$differenceInHours hr. ago"
            }
        } else{
            "$differenceInDays days ago"
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
            selftext = selftext,
            timeStamp = timeStamp,
            media = media,
            commentsCount = commentsCount
        )
    }

    //endregion

}