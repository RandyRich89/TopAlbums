package com.example.topalbums.ViewModels

import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.delivery.Request.Items.AlbumItem
import com.example.delivery.Request.Items.Albums
import com.example.topalbums.MainActivity
import com.example.topalbums.Models.AlbumsModel
import com.example.topalbums.RequestHandler.RequestHandler

class AlbumsVM(): ViewModel(), MainActivity.ViewModelCallbacks {

    val albumsModel = AlbumsModel()
    var albumCopyright: String? = null

    var isActive = true

    val albums: MutableLiveData<Albums?> by lazy {
        MutableLiveData<Albums?>()
    }
    val albumsSplit: MutableLiveData<ArrayList<ArrayList<AlbumItem?>?>> by lazy {
        MutableLiveData<ArrayList<ArrayList<AlbumItem?>?>>()
    }

    init {
        requestAlbums()
    }

    fun requestAlbums() {
        albumsSplit.value = ArrayList()
        while (albumsSplit.value!!.size != 2) {
            albumsSplit.value!!.add(ArrayList())
        }
        albumsModel.requestAlbums(object: RequestHandler.RequestCallback {
            override fun onResult(result: Albums?, copyright: String?) {

                if(result != null) {
                    albumCopyright = copyright
                    albums.value = result
                    albums.value!!.albums.forEachIndexed() { index, element ->
                        if(index % 2 != 0) {
                            albumsSplit.value!!.get(1)!!.add(element)
                        } else {
                            albumsSplit.value!!.get(0)!!.add(element)
                        }
                    }
                    if(isActive) albumsSplit.postValue(albumsSplit.value)
                } else {
                    if(isActive) albumsSplit.postValue(null)
                }
            }
        })
    }

    fun getVMCallback(): MainActivity.ViewModelCallbacks {
        return this
    }

    override fun isActive(activityActive: Boolean) {
        isActive = activityActive
    }

    override fun retryRequest() {
       requestAlbums()
    }
}