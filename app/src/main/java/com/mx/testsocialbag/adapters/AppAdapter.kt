package com.mx.testsocialbag.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mx.testsocialbag.R
import com.mx.testsocialbag.pojos.AppUsageInfo
import kotlinx.android.synthetic.main.item_app.view.*

class AppAdapter(var list: ArrayList<AppUsageInfo>) : RecyclerView.Adapter<AppViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)

        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {

        val app = list[position]
        holder.view.name_app.text = app.packageName

        val seconds = (app.timeInForeground!! / 1000).toDouble()
        holder.view.duration_text_view.text = "${seconds} segundos"

    }

    override fun getItemCount(): Int {
        return list.size
    }
}


class AppViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

}