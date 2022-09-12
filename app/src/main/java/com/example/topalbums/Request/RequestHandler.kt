/*
* Basic request handler, this class would become specified to
* request type as types of request expand.
* */

package com.example.topalbums.RequestHandler

import com.example.delivery.Request.Items.AlbumConstants
import com.example.delivery.Request.Items.Albums
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class RequestHandler {

    interface AlbumsService {
        @GET("api/v2/us/music/most-played/100/albums.json")
        fun getRequest(): Call<JsonObject?>
    }

    companion object {
        fun makeRequest(requestCallback: RequestCallback) {
            val retrofit: Retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://rss.applemarketingtools.com/")
                .build()

            val albumService = retrofit.create(AlbumsService::class.java)
            albumService.getRequest()!!.enqueue(object : Callback<JsonObject?> {

                override fun onResponse(
                    call: Call<JsonObject?>,
                    response: Response<JsonObject?>
                ) {
                    var gson = GsonBuilder().create()
                    val result = gson.fromJson(response.body()!!.asJsonObject.get(AlbumConstants.TOPLEVELID_KEY), Albums::class.java)
                    requestCallback.onResult (if(result != null) result else null, result.copyright)
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable?) {
                    requestCallback.onResult (null, null)
                }
            })
        }
    }

    interface RequestCallback {
        fun onResult(result: Albums?, copyright: String?)
    }
}