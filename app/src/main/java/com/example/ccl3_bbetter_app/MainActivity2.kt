package com.example.ccl3_bbetter_app

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ccl3_bbetter_app.DataBase.SQLiteHelper
import com.example.ccl3_bbetter_app.adapter.ItemAdapter
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {

    companion object {
        lateinit var sqLiteHelper: SQLiteHelper
    }

    @SuppressLint("RestrictedApi", "DiscouragedPrivateApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        sqLiteHelper = SQLiteHelper(this, null, 1)
        showHabit()
        /**Display all habits**/

        deleteAll.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            if (popupMenu is MenuBuilder) (popupMenu as MenuBuilder).setOptionalIconsVisible(true)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.deleteAllHabits -> {
                        var alertDialog = AlertDialog.Builder(this)
                            .setTitle("Waring")
                            .setMessage("Are you sure you want to delete all habits")
                            .setPositiveButton(
                                "Yes",
                                DialogInterface.OnClickListener { dialogInterface, which ->
                                    sqLiteHelper.deleteAllHabit(this)
                                    showHabit()
//                                    Toast.makeText(
//                                        this,
//                                        "All data are deleted.",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
                                    dialogInterface.dismiss()
                                    // The id of the channel.
                                    //val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                                    //notificationManager.createNotificationChannel(channel)
                                   // val id: String = "channel1"
                                    notificationManager.deleteNotificationChannel("channel1")
                                })
                            .setNegativeButton(
                                "No",
                                DialogInterface.OnClickListener { dialogInterface, which ->
                                    dialogInterface.dismiss()
                                })
                            .show()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.inflate(R.menu.menu)

            /**showing icon next to text**/
            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopup = fieldMPopup.get(popupMenu)
                mPopup.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopup, true)
            } catch (e: Exception) {
                Log.e("Main", "Error showing menu icons.", e)
            } finally {
                popupMenu.show()
            }
            popupMenu.show()
        }

        /**Add new habit button**/
        floatingActionButton.setOnClickListener {
            val intent = Intent(this, CreateHabit::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHabit() {
        val habitList = sqLiteHelper.getAllHabit(this)
        // Adapter class is initialized and list is passed in the param.
        val adapter = ItemAdapter(this, habitList)
        // Set the LayoutManager that this RecyclerView will use.
        recycler_view_items.layoutManager = GridLayoutManager(this, 2)
        // adapter instance is set to the recyclerview to inflate the items.
        recycler_view_items.adapter = adapter
        adapter?.notifyDataSetChanged()

        /**Empty state**/
        if (habitList.isEmpty()) {
            Empty_State_Quote.setVisibility(View.VISIBLE);
            Empty_State_Image.setVisibility(View.VISIBLE);
            CreateHabit_emptyState.setVisibility(View.VISIBLE);
            deleteAll.setVisibility(View.INVISIBLE);
        } else {
            Empty_State_Quote.setVisibility(View.INVISIBLE);
            Empty_State_Image.setVisibility(View.INVISIBLE);
            CreateHabit_emptyState.setVisibility(View.INVISIBLE);
            deleteAll.setVisibility(View.VISIBLE);
        }
    }

    override fun onResume() {
        showHabit()
        super.onResume()
    }

}