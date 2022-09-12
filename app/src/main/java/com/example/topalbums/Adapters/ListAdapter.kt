package com.example.topalbums.Adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.delivery.Request.Items.AlbumConstants
import com.example.delivery.Request.Items.AlbumItem
import com.example.delivery.Request.Items.Genre
import com.example.topalbums.R
import com.squareup.picasso.Picasso

class ListAdapter(val context: Context) {

    fun populate(items: ArrayList<AlbumItem?>?, layout: LinearLayout, copyright: String?, fragmentManager: FragmentManager) {
        if (items!!.size == 0) return

        items!!.forEach {
            val view = LayoutInflater.from(context).inflate(R.layout.album_list_item, null)
            Picasso.get().load(it!!.albumCoverURI)
                .into(view.findViewById<ImageView>(R.id.album_cover))
            view.findViewById<TextView>(R.id.album_name).text = it!!.albumName
            view.findViewById<TextView>(R.id.artist_name).text = it!!.artist

            view.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    val albumDetailsDialog = AlbumDetailsDialog(it, copyright ,context)
                    albumDetailsDialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog)
                    albumDetailsDialog.show(fragmentManager, null)
                }
            })
            layout.addView(view)
        }
    }
}

/*
* This class should be moved to it's own file but because of
* time constraints a nested class will do.
* */

class AlbumDetailsDialog(val albumItem: AlbumItem, val copyright: String?, context: Context): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.album_details, null)
    }

    /*
    * Because this API from apple is not under our control we should check
    * each value populated for nulls, this way our placeholders seen in the
    * layout @here are not disturbed.
    * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ConstraintLayout>(R.id.album_details_root).children.forEach {
            when(it.id) {
                R.id.album_cover -> {
                    if (!albumItem.albumCoverURI.isNullOrEmpty()) Picasso.get().load(albumItem.albumCoverURI)
                        .into(view.findViewById<ImageView>(R.id.album_cover))
                }
                R.id.artist_name -> {
                    if (!albumItem.artist.isNullOrEmpty()) view.findViewById<TextView>(R.id.artist_name)!!
                        .text = albumItem.artist
                }
                R.id.album_name -> {
                    if (!albumItem.albumName.isNullOrEmpty()) view.findViewById<TextView>(R.id.album_name)!!
                        .text = albumItem.albumName
                }
                R.id.genre -> {
                    val genere = getGenre(albumItem.genres)
                    if (!genere.isNullOrEmpty()) view.findViewById<TextView>(R.id.genre)!!
                        .text = genere
                }
                R.id.release_date -> {
                    if (!albumItem.releaseDate.isNullOrEmpty()) view.findViewById<TextView>(R.id.release_date)!!
                        .text = albumItem.releaseDate
                }
                R.id.copyright -> {
                    if (!copyright.isNullOrEmpty()) view.findViewById<TextView>(R.id.copyright)!!
                        .text = copyright
                }
                R.id.visit_album_btn -> {
                    val btn = view.findViewById<Button>(R.id.visit_album_btn)!!

                    if(albumItem.albumURL.isNullOrEmpty()) {
                        btn.isEnabled = false
                        return
                    }
                    btn.setOnClickListener(object: View.OnClickListener {
                        override fun onClick(v: View?) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(albumItem.albumURL))
                            try {
                                startActivity(intent)
                            } catch (e: Exception) {
                                //Catch any exceptions from this action here and log them.
                                Toast.makeText(requireActivity(), "Unable to open link.", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
            }
        }
    }

    fun getGenre(genres: List<Genre>?): String? {
        if (genres == null) return null
        genres.forEach() {
            if(it.genre != null && it.genre != AlbumConstants.GENRENAME_KEY) {
                return it.genre!!
            }
        }
        return null
    }
}