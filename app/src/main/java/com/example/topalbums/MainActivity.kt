package com.example.topalbums

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.topalbums.Adapters.ListAdapter
import com.example.topalbums.ViewModels.AlbumsVM
import com.example.topalbums.Views.AlbumsListView

class MainActivity : AppCompatActivity(), View.OnClickListener {

//    lateinit items can never be null in their use so use this to avoid null checks.
    private lateinit var progress: ProgressBar
    private lateinit var retryBtn: Button
    private lateinit var albumsListView: AlbumsListView

    private lateinit var albumVMCallback: ViewModelCallbacks

    /**
     * This onCreate() is becoming bloated so in the future we
     * should move things like dialog creation, onClick handling, and other secondary
     * view logic to different classes and methods.
     *
     * If more time was gained to work on this, all secondary view logic would be ported
     * to binding libraries to avoid a bloated activity.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = findViewById(R.id.progress_bar)
        retryBtn = findViewById(R.id.retry_btn)
        retryBtn.setOnClickListener(this)

        albumsListView = findViewById(R.id.album_list_view)

        val albumsModel: AlbumsVM by viewModels()
        albumVMCallback = albumsModel.getVMCallback()

        albumsModel.albumsSplit.observe(this, Observer {

            toggleProgress(false)

            if(it != null && it.get(0) != null && it.get(1) != null) {
                val copyright = if (albumsModel.albumCopyright != null) albumsModel.albumCopyright else null
                ListAdapter(this).populate(it.get(0), findViewById(R.id.list_1), copyright, supportFragmentManager)
                ListAdapter(this).populate(it.get(1), findViewById(R.id.list_0), copyright, supportFragmentManager)
                toggleOverlay(View.GONE)
            } else {
                val alertDialogBuilder = AlertDialog.Builder(this)
                var alertDialog: AlertDialog? = null
                alertDialogBuilder.setTitle(R.string.no_data_title)
                alertDialogBuilder.setMessage(R.string.no_data_message)
                alertDialogBuilder.setPositiveButton(R.string.no_data_retry_btn, object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        albumsModel.requestAlbums()
                    }
                })
                alertDialogBuilder.setNegativeButton(R.string.no_data_cancel_btn, object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dismissDialog(alertDialog!!)
                    }
                }).create()
                alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        })
    }

    override fun onDestroy() {
        if(albumVMCallback != null) albumVMCallback!!.isActive(false)
        super.onDestroy()
    }

    private fun toggleProgress(isActive: Boolean) {
        if(isActive) {
            progress.progress = 0
            progress.visibility = View.VISIBLE
            retryBtn.visibility = View.INVISIBLE
        } else {
            progress.progress = 100
            progress.visibility = View.INVISIBLE
            retryBtn.visibility = View.VISIBLE
        }
    }

    private fun toggleOverlay(isVisible: Int) {
        findViewById<LinearLayout>(R.id.screen_overlay).visibility = isVisible
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.retry_btn -> {
                if(albumVMCallback != null) albumVMCallback!!.retryRequest()
            }
        }
    }

    private fun dismissDialog(dialog: AlertDialog) {
        dialog.dismiss()
    }

    /*
    * LifecycleObservers should never be passed to ViewModel by Android documentation,
    * so instead we should use an interface to communicate activity status.
    * */

    interface ViewModelCallbacks {
        fun isActive(activityActive: Boolean)
        fun retryRequest()
    }
}