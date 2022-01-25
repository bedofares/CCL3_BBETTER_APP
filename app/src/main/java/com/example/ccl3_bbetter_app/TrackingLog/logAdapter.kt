package com.example.recyclerviewapp.TrackingLog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ccl3_bbetter_app.R
import com.example.ccl3_bbetter_app.TrackingLog.model.LogModel
import kotlinx.android.synthetic.main.item_custom_row2.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class logAdapter(val context: Context, val Logs: ArrayList<LogModel>) :
    RecyclerView.Adapter<logAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val LogTitle = view.title
        val LogCurrentDate = view.CurrentDate
        val currentTime = view.CurrentTimeDisplay
        val LogStatus = view.status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_custom_row2, parent, false)
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val log: LogModel = Logs[position]
        holder.LogTitle.text = log.habitTitle
        holder.LogCurrentDate.text = log.currentDate
        holder.LogStatus.text = log.status
        holder.currentTime.text = log.currentTime

        /**Change text color based on the status**/
        if (holder.LogStatus.text == "Skipped") {
            holder.LogStatus.setTextColor(Color.parseColor("#FF0000"))
        } else if (holder.LogStatus.text == "Completed") {
            holder.LogStatus.setTextColor(Color.parseColor("#29A98B"))
        }
    }

    /**Gets the number of items in the list*/
    override fun getItemCount(): Int {
        return Logs.size
    }

}



