package com.example.delivery.Request.Items

import com.google.gson.annotations.SerializedName

object AlbumConstants {
    const val TOPLEVELID_KEY = "feed"
    const val GENRENAME_KEY = "Music"
}

class Albums(
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("results")
    val albums: List<AlbumItem>
)

class AlbumItem(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("artistName")
    val artist: String?,
    @SerializedName("name")
    val albumName: String?,
    @SerializedName("releaseDate")
    val releaseDate: String?,
    @SerializedName("genres")
    val genres: List<Genre>?,
    @SerializedName("artworkUrl100")
    val albumCoverURI: String?,
    @SerializedName("url")
    val albumURL: String?
)

class Genre(
    @SerializedName("name")
    val genre: String?
)

/*
* The json returned from Apple is not sufficient,
* we should be able to use their variables directly with
* this deserializable, but because they are not using the right primitives
* this class will not work. I.E. strings are strings and ints are ints.
* In my experience I would send this back to backend for the right results.
* */
//class GenreDeserializer<T>: JsonDeserializer<T> {
//
//    override fun deserialize(
//        json: JsonElement,
//        typeOfT: Type?,
//        context: JsonDeserializationContext?
//    ): T? {
//        var genreObject: JsonObject? = null
//        json.asJsonArray.forEach() {
//            val name = it.asJsonObject.get(AlbumConstants.GENRENAMEID_KEY) as String
//            if(!(name.isNullOrBlank()) && (name != AlbumConstants.GENRENAME_KEY)) {
//                genreObject = it.asJsonObject
//                return Gson().fromJson(genreObject, typeOfT)
//            }
//        }
//        return null
//    }
//}
