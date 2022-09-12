/*
* Not sure why we would not use RecyclerView but
* this adapter is used to populate the views needed
* into the AlbumViewItems
* */

package com.example.topalbums.Views
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import com.example.delivery.Request.Items.AlbumItem
import com.example.topalbums.R

class AlbumsListView(context: Context, attrs: AttributeSet): RelativeLayout(context, attrs) {

    private var adapter0: BaseAdapter? = null
    private var adapter1: BaseAdapter? = null
    private var gridLayout0: LinearLayout
    private var gridLayout1: LinearLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.album_list_view, this, true)
        gridLayout0 = findViewById(R.id.list_0)
        gridLayout1 = findViewById(R.id.list_1)
//        gridLayout.layoutParams
//        gridLayout.stretchMode = GridView.NO_STRETCH
    }

    fun populate(items: ArrayList<ArrayList<AlbumItem?>?>) {
//        adapter0 = GridAdapter(context, items.get(0), true)
//        adapter1 = GridAdapter(context, items.get(1), false)
//        gridLayout0.adapter = adapter0
//        gridLayout1.adapter = adapter1
    }
}