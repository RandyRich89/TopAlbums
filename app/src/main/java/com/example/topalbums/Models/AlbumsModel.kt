/*
* Model for albums, data for specific albums is static so no model is needed for them individually.
* */

package com.example.topalbums.Models

import com.example.topalbums.RequestHandler.RequestHandler

class AlbumsModel {

    fun requestAlbums(requestHandler: RequestHandler.RequestCallback) {
        RequestHandler.makeRequest(requestHandler)
    }
}