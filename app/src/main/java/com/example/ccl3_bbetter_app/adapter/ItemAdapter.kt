package com.example.ccl3_bbetter_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ccl3_bbetter_app.DetailActivity
import com.example.ccl3_bbetter_app.R
import com.example.ccl3_bbetter_app.model.HabitModel
import kotlinx.android.synthetic.main.item_custom_row.view.*

class ItemAdapter(val context: Context, val habits: ArrayList<HabitModel>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    /**A ViewHolder describes an item view and metadata about its place within the RecyclerView.**/
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val HabitTitle = view.Habit_Title
        //val tvItemDescription = view.tv_item_description
        val cardViewItem = view.card_view_item
        val habitImage = view.habit_Image
    }

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_custom_row, parent, false)
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit: HabitModel = habits[position]
        holder.HabitTitle.text = habit.title
        //holder.tvItemDescription.text = habit.description
        val testImgId = holder.HabitTitle.context.resources.getIdentifier(
            habit.habitImage,
            "drawable",
            holder.HabitTitle.context.packageName
        )
        holder.habitImage.setImageResource(testImgId)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("ID", habit.id)
            intent.putExtra("TITLE", habit.title)
            intent.putExtra("DESCRIPTION", habit.description)
            intent.putExtra("GOAL", habit.goal)
            intent.putExtra("REMINDER", habit.reminderTime)
            intent.putExtra("DATE", habit.startDate)
            intent.putExtra("IMAGE", habit.habitImage)
            intent.putExtra("COUNTER", habit.counterScore)
            context.startActivity(intent)
        }
    }

    /**Gets the number of items in the list**/
    override fun getItemCount(): Int {
        return habits.size
    }


}