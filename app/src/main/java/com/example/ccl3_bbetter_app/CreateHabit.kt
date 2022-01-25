package com.example.ccl3_bbetter_app

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.ccl3_bbetter_app.DataBase.SQLiteHelper
import com.example.ccl3_bbetter_app.Reminder.AlarmReceiver
import com.example.ccl3_bbetter_app.Reminder.Util.RandomUtil
import com.example.ccl3_bbetter_app.Reminder.channelID
import com.example.ccl3_bbetter_app.Reminder.messageExtra
import com.example.ccl3_bbetter_app.Reminder.titleExtra
import com.example.ccl3_bbetter_app.model.HabitModel
import kotlinx.android.synthetic.main.activity_create_habit.*
import java.text.SimpleDateFormat
import java.util.*

class CreateHabit : AppCompatActivity() {

    companion object {
        lateinit var sqLiteHelper: SQLiteHelper
    }

    var cal = Calendar.getInstance()
    var selectedImageTag = ""
    var timeTest: Long = 0
    var RandomInt = 0

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_habit)

        sqLiteHelper = SQLiteHelper(this, null, 1)

        /**
        Create Notification channel
         **/
        createNotificationChannel()

        /**
        Go Back to previous activity
         **/
        Bck_btn2.setOnClickListener {
            finish()
        }

        /**
        Cancel creating a habit
         **/
        btn_cancel.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            clearEditText()
        }

        /**
            Creating a habit
         **/
        btn_confirm.setOnClickListener {
            /** Assigning input fields data into variables **/
            val title = habitTitle_Create.text.toString()
            val description = habitDescription_Create.text.toString()
            val goal = habitGoal_Create.text.toString()
            val reminderTime = HabitTime_Create.text.toString()
            val startDate = HabitStartDate_Create.text.toString()
            val habitImageTag = selectedImageTag

            if (title.isEmpty() || description.isEmpty() || goal.isEmpty() || reminderTime.isEmpty() || startDate.isEmpty()) {
                Toast.makeText(this, "Please fill the empty filed", Toast.LENGTH_SHORT).show()
            } else if (habitImageTag.isEmpty()) {
                Toast.makeText(this, "Please select an icon", Toast.LENGTH_SHORT).show()
            } else {
                val habit = HabitModel()
                habit.title = title
                habit.description = description
                habit.goal = goal.toInt()
                habit.reminderTime = reminderTime
                habit.startDate = startDate
                habit.habitImage = habitImageTag

                /**Insert data into Habits table**/
                sqLiteHelper.insertHabit(this, habit)
                //clearEditText()
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)

                /**schedule Notification**/
                scheduleNotification()
            }
        }
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd/MM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                HabitStartDate_Create.setText(sdf.format(cal.getTime()))
            }
        }
        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        HabitStartDate_Create.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    this@CreateHabit,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        })

        HabitTime_Create.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                HabitTime_Create.setText(SimpleDateFormat("HH:mm a").format(cal.time))
                timeTest = cal.timeInMillis
            }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

    }

    /**
    Empty input fields
     **/
    private fun clearEditText() {
        habitTitle_Create.setText("")
        habitDescription_Create.setText("")
        habitGoal_Create.setText("")
        HabitTime_Create.setText("")
    }

    /**
    Selecting Image/Icon
     **/
    fun onImageClicked(view: View) {
        val image = view as ImageView
        val imgTag = image.tag.toString()
        val testImgId = this.resources.getIdentifier(imgTag, "drawable", packageName)
        image.setImageResource(testImgId)
        selectedImageTag = imgTag
        study.setBackgroundResource(if (selectedImageTag == "ic_study_work_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        smoke.setBackgroundResource(if (selectedImageTag == "ic_no_smoking_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        cycling.setBackgroundResource(if (selectedImageTag == "ic_cycling_sport_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        sleep.setBackgroundResource(if (selectedImageTag == "ic_sleep_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        water.setBackgroundResource(if (selectedImageTag == "ic_water_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        wakeUp.setBackgroundResource(if (selectedImageTag == "ic_wake_up_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        meditate.setBackgroundResource(if (selectedImageTag == "ic_meditation_think_guru_meditate_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        sport.setBackgroundResource(if (selectedImageTag == "ic_sport_faculty_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        quit.setBackgroundResource(if (selectedImageTag == "ic_stop_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
    }

    /**
    Pushing notification
     **/
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "BBETTER"
            val desc = "Keep tracking your habit"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelID, name, importance)
            channel.description = desc

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        val title = habitTitle_Create.text.toString()
        val message = habitDescription_Create.text.toString()

        /**
         * Passing title and description
         */
        RandomInt = RandomUtil.getRandomInt()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)
        intent.putExtra("RandomInt", RandomInt)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            RandomInt,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        //val time = timeTest
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            SystemClock.elapsedRealtime() + 2000,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }


}