package com.mx.testsocialbag

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.mx.testsocialbag.adapters.AppAdapter
import com.mx.testsocialbag.fragments.LoadingFragment
import com.mx.testsocialbag.helpers.FirebaseHelper
import com.mx.testsocialbag.helpers.UStats
import com.mx.testsocialbag.pojos.AppUsageInfo
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity(), LifecycleObserver {

    private var calendarOnStart: Calendar? = null
    private var list = ArrayList<AppUsageInfo>()
    lateinit var adapter: AppAdapter
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("M-d-yyyy HH:mm:ss")
    private var initialDate = ""
    private var finalDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        permission_button?.setOnClickListener {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }

        adapter = AppAdapter(list)


        list_apps?.layoutManager = LinearLayoutManager(this)
        list_apps?.adapter = adapter


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        calendarOnStart = Calendar.getInstance()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        if (calendarOnStart != null) {


            val currentTime = Calendar.getInstance()
            val startTime: Long = calendarOnStart!!.timeInMillis
            val endTime: Long = currentTime.timeInMillis

            list.addAll(UStats.getUsageStatsList(this@MainActivity, startTime, endTime));
            adapter.notifyDataSetChanged()
            title_label_text_view?.visibility = View.VISIBLE

            initialDate = dateFormat.format(startTime)
            finalDate = dateFormat.format(startTime)

            start_time_text_view?.text = "Tiempo de inicio : ${initialDate}"
            end_time_text_view?.text = "Tiempo Final : ${finalDate}"

            uploadData()
        }

    }

    private fun uploadData() {
        val loadingFragment = LoadingFragment()
        loadingFragment.show(supportFragmentManager, "loading-fragment")

        val hashToUpload = generateHash()


        FirebaseHelper.uploadData(hashToUpload, {
            Toast.makeText(this, "Datos subido con exito", Toast.LENGTH_LONG).show()
            loadingFragment.dismissAllowingStateLoss()
        }, {
            loadingFragment.dismissAllowingStateLoss()
            Toast.makeText(this, "Error al subir ${it}", Toast.LENGTH_LONG).show()
        })

    }

    fun generateHash(): HashMap<String, Any> {

        val hash: HashMap<String, Any> = HashMap()
        val listOfApps = ArrayList<HashMap<String, Any>>()

        list.forEach {
            listOfApps.add(it.toHash())
        }

        hash["apps"] = listOfApps
        hash["dateCreated"] = Date().toString()
        hash["initialDate"] = initialDate
        hash["finalDate"] = finalDate
        return hash
    }
}