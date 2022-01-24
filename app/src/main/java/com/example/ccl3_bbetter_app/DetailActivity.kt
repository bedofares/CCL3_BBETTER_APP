package com.example.ccl3_bbetter_app

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.ccl3_bbetter_app.DataBase.SQLiteHelper
import com.example.ccl3_bbetter_app.Reminder.AlarmReceiver
import com.example.ccl3_bbetter_app.model.HabitModel
import kotlinx.android.synthetic.main.activity_detail_actvity.*
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity() : AppCompatActivity() {

    companion object {
        lateinit var sqLiteHelper: SQLiteHelper
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_actvity)

        sqLiteHelper = SQLiteHelper(this, null, 1)


        var intent = intent

        val id = intent.getIntExtra("ID", 0)
        val title = intent.getStringExtra("TITLE")
        val description = intent.getStringExtra("DESCRIPTION")
        val reminderTime = intent.getStringExtra("REMINDER")
        val startDate = intent.getStringExtra("DATE")
        val image = intent.getStringExtra("IMAGE")
        val goal = intent.getIntExtra("GOAL", 0)

        //counter = 0
        var counterScore = intent.getIntExtra("COUNTER", 0)



        HabitDetail_Title.text = title

        Habit_Goal.text = "Goal ${goal.toString()} Days"
        //taskTimeDetail.text = reminderTime
        taskStartDateDetail.text = "Start Date : ${startDate} "

        //INITIALIZING COUNTER
        Habit_Counter.text = counterScore.toString()

        val date = Calendar.getInstance().time
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val formatedDate = sdf.format(date)


        CheckedIn_btn.setOnClickListener {
            val habit = HabitModel()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("CHECK IN")
            builder.setMessage("Have you made it today?")

            if (startDate == formatedDate) {
                Toast.makeText(this, formatedDate, Toast.LENGTH_SHORT).show()
                if (counterScore == goal) {
                    Habit_Counter.text = "Congrats"
                } else {
                    builder.setPositiveButton("Yes") { dialog, which ->
                        //Toast.makeText(this, "yes was clicked", Toast.LENGTH_SHORT).show()
                        counterScore++
                        Habit_Counter.text = counterScore.toString()
                        habit.id = intent.getIntExtra("ID", 0)
                        habit.counterScore = counterScore


                        val success = sqLiteHelper.updateCounter(habit)

                    }
                    builder.setNegativeButton("Not Yet") { dialog, which ->

                    }

                    builder.setNeutralButton("Skipped") { dialog, which ->

                    }
                    builder.show()
                }
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Habit is not yet started")
                builder.setMessage("Your habit will start on $startDate")
                builder.setPositiveButton("Ok") { dialog, which ->
                    dialog.dismiss()

                }
                builder.show()
            }


        }
        Bck_btn.setOnClickListener {
            finish()
        }
        // Adapter class is initialized and list is passed in the param.
        btn_edit.setOnClickListener {
            val intent = Intent(this, UpdateDetailActivity::class.java)
            intent.putExtra("ID", id)
            intent.putExtra("TITLE", title)
            intent.putExtra("DESCRIPTION", description)
            intent.putExtra("GOAL", goal)
            intent.putExtra("REMINDER", reminderTime)
            intent.putExtra("DATE", startDate)
            intent.putExtra("IMAGE", image)

            startActivity(intent)
        }

        //Delete button
        btn_delete.setOnClickListener {
            var alertDialog = AlertDialog.Builder(this)
                .setTitle("Waring")
                .setMessage("Are you sure you want to delete $title")
                .setPositiveButton(
                    "Yes",
                    DialogInterface.OnClickListener { dialogInterface, which ->
                        val success = sqLiteHelper?.deleteHabit(intent.getIntExtra("ID", 0))
                        val intentt = Intent(applicationContext, AlarmReceiver::class.java)
                        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val pendingIntent = PendingIntent.getBroadcast(
                            this,
                            1,
                            intentt,
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        alarmManager.cancel(pendingIntent)
//                        Toast.makeText(this, "Alarm Cancelled ", Toast.LENGTH_LONG).show();
                        if (success)
                            finish()
                        dialogInterface.dismiss()
                    })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, which ->
                    dialogInterface.dismiss()
                })
                .show()
        }

    }
}