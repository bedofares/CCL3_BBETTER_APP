package com.example.ccl3_bbetter_app

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import com.example.ccl3_bbetter_app.DataBase.SQLiteHelper
import com.example.ccl3_bbetter_app.Reminder.AlarmReceiver
import com.example.ccl3_bbetter_app.TrackingLog.LogActivity
import com.example.ccl3_bbetter_app.TrackingLog.LogDB
import com.example.ccl3_bbetter_app.model.HabitModel
import com.example.ccl3_bbetter_app.TrackingLog.model.LogModel
import kotlinx.android.synthetic.main.activity_detail_actvity.*
import kotlinx.android.synthetic.main.activity_log.*
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*

class DetailActivity() : AppCompatActivity() {

    companion object {
        lateinit var sqLiteHelper: SQLiteHelper
        lateinit var logDB: LogDB
    }

    var Currentstatus = ""

    @SuppressLint("RestrictedApi", "DiscouragedPrivateApi", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_actvity)

        sqLiteHelper = SQLiteHelper(this, null, 1)
        logDB = LogDB(this, null, 1)


        val intent = intent

        /**Getting All passed values**/
        val id = intent.getIntExtra("ID", 0)
        val title = intent.getStringExtra("TITLE")
        val description = intent.getStringExtra("DESCRIPTION")
        val reminderTime = intent.getStringExtra("REMINDER")
        val startDate = intent.getStringExtra("DATE")
        val image = intent.getStringExtra("IMAGE")
        val goal = intent.getIntExtra("GOAL", 0)
        var counterScore = intent.getIntExtra("COUNTER", 0)
        var lastCheckInTime = intent.getStringExtra("LASTCHECK")


        //Set page title to habit name
        HabitDetail_Title.text = title


        Habit_Goal.text = "Goal ${goal} Days"
        taskStartDateDetail.text = "Start Date : ${startDate} "

        //INITIALIZING COUNTER
        Habit_Counter.text = counterScore.toString()

        //INITIALIZING CHECK IN
        LastCheck.text = "Last CheckIn : $lastCheckInTime"

        //Get today's date in this format "dd/MM/yyyy"
        val date = Calendar.getInstance().time
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val formatedDate = sdf.format(date)

        //On done? click....
        CheckedIn_btn.setOnClickListener {
            val habit = HabitModel()
            val log = LogModel()

            /**
             * Get current date & time on Done ClickListener
             */
            val Currentdate = Calendar.getInstance().time
            val sdfDate = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val formatedCurrentDate = sdfDate.format(Currentdate)

            /**Get time**/
            val sdfTime = SimpleDateFormat("HH:mm:ss a")
            val formatedCurrentTime = sdfTime.format(Currentdate)

            /**Get Date & time**/
            val sdfCheckIn = SimpleDateFormat("dd/MM/yyyy HH:mm:ss a")
            val lastCheckIn = sdfCheckIn.format(Currentdate)

            /**Check In dialog**/
            val builder = AlertDialog.Builder(this)
            builder.setTitle("CHECK IN")
            builder.setMessage("Have you made it today?")

            /**Prevent user from checkingIn until the habit starts**/
            if (startDate?.compareTo(formatedDate)!! <= 0) {
                /** Show this message if user achieved his goal **/
                if (counterScore == goal) {
                    Habit_Counter.text = "Congrats"
                } else {
                    builder.setPositiveButton("Yes") { dialog, which ->
                        /**Increment checkIn counter**/
                        counterScore++
                        Habit_Counter.text = counterScore.toString()
                        habit.id = intent.getIntExtra("ID", 0)
                        habit.counterScore = counterScore

                        /**Set  Last checkIn Date & Time**/
                        lastCheckInTime = lastCheckIn
                        LastCheck.text = "Last CheckIn : $lastCheckInTime"
                        habit.lastCheck = lastCheckIn

                        /** Update Counter and lastCheckIn **/
                        sqLiteHelper.updateCounter(habit)
                        sqLiteHelper.updateCheckIn(habit)


                        /*** Set habit status*/
                        Currentstatus = "Completed"


                        /*** Insert Data Into db and into LogModel*/
                        log.habitId = intent.getIntExtra("ID", 0)
                        log.habitTitle = title.toString()
                        log.currentDate = formatedCurrentDate
                        log.currentTime = formatedCurrentTime
                        log.status = Currentstatus
                        logDB.insertLog(this, log)
                    }
                    builder.setNegativeButton("Not Yet") { dialog, which ->
                         dialog.dismiss()
                    }
                    builder.setNeutralButton("Skipped") { dialog, which ->
                        Currentstatus = "Skipped"

                        /** Update lastCheckIn **/
                        lastCheckInTime = lastCheckIn
                        LastCheck.text = "Last CheckIn : $lastCheckInTime"
                        habit.lastCheck = lastCheckIn


                        sqLiteHelper.updateCheckIn(habit)
                        /*** Insert Data Into db and into LogModel*/
                        log.habitId = intent.getIntExtra("ID", 0)
                        log.habitTitle = title.toString()
                        log.currentDate = formatedCurrentDate
                        log.currentTime = formatedCurrentTime
                        log.status = Currentstatus
                        logDB.insertLog(this, log)
                    }
                    builder.show()
                }
            } else {
                /**Inform user that the habit is not started yet**/
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

        btn_log.setOnClickListener {
            /**Send Data to Log Activity **/
            val intent = Intent(this, LogActivity::class.java)
            intent.putExtra("HabitId", id)
            intent.putExtra("HabitTitle", title)
            intent.putExtra("COUNTER", counterScore)
            intent.putExtra("GOAL", goal)
            startActivity(intent)
        }

        /** Option menu Edit - Delete **/
        options.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            if (popupMenu is MenuBuilder) (popupMenu as MenuBuilder).setOptionalIconsVisible(true)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.delete -> {
                        var alertDialog = AlertDialog.Builder(this)
                            .setTitle("Waring")
                            .setMessage("Are you sure you want to delete $title")
                            .setPositiveButton(
                                "Yes",
                                DialogInterface.OnClickListener { dialogInterface, which ->
                                    val success =
                                        sqLiteHelper?.deleteHabit(intent.getIntExtra("ID", 0))
                                    logDB.deleteHabitLogs(intent.getIntExtra("ID", 0))

                                    /**Cancel Alarm**/
                                    val intentt =
                                        Intent(applicationContext, AlarmReceiver::class.java)
                                    val alarmManager =
                                        getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                    val pendingIntent = PendingIntent.getBroadcast(
                                        this,
                                        1,
                                        intentt,
                                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                                    )
                                    alarmManager.cancel(pendingIntent)
                                    if (success)
                                        finish()
                                    dialogInterface.dismiss()
                                })
                            .setNegativeButton(
                                "No",
                                DialogInterface.OnClickListener { dialogInterface, which ->
                                    dialogInterface.dismiss()
                                })
                            .show()
                        true
                    }
                    R.id.edit -> {
                        val intent = Intent(this, UpdateDetailActivity::class.java)
                        /**Pass data to Edit Activity**/
                        intent.putExtra("ID", id)
                        intent.putExtra("TITLE", title)
                        intent.putExtra("DESCRIPTION", description)
                        intent.putExtra("GOAL", goal)
                        intent.putExtra("REMINDER", reminderTime)
                        intent.putExtra("DATE", startDate)
                        intent.putExtra("IMAGE", image)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.inflate(R.menu.menu2)

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

    }
}