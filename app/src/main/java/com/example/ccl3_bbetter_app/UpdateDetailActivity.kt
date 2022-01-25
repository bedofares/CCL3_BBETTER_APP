package com.example.ccl3_bbetter_app

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ccl3_bbetter_app.DataBase.SQLiteHelper
import com.example.ccl3_bbetter_app.model.HabitModel
import kotlinx.android.synthetic.main.activity_create_habit.*
import kotlinx.android.synthetic.main.activity_update_detail_activity.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateDetailActivity : AppCompatActivity() {
    companion object {
        lateinit var sqLiteHelper: SQLiteHelper
    }

    var cal = Calendar.getInstance()
    var selectedImageTag = ""

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_detail_activity)

        sqLiteHelper = SQLiteHelper(this, null, 1)


        var intent = intent

        /**Get and Assign passed data into variables**/
        val title = intent.getStringExtra("TITLE")
        val description = intent.getStringExtra("DESCRIPTION")
        val reminderTime = intent.getStringExtra("REMINDER")
        val startDate = intent.getStringExtra("DATE")

        val goal = intent.getIntExtra("GOAL", 0)
        val id = intent.getIntExtra("ID", 0)


        val imageHabit = intent.getStringExtra("IMAGE")
        selectedImageTag = imageHabit.toString()


        /**Set title = Habit Title**/
        HabitEdit_Title.text = title


        /**Assign text to input fields**/
        habitTitle_Edit.setText(title)
        habitDescription_Edit.setText(description)
        HabitGoal_Edit.setText(goal.toString())
        HabitStartDate_Edit.setText(startDate)
        HabitTime_Edit.setText(reminderTime)

        /**
        Go Back to previous activity
         **/
        Bck_btn1.setOnClickListener {
            finish()
        }
        /**
        Cancel Editing a habit
         **/
        btn_cancel_edit.setOnClickListener {
            finish()
        }


        HabitTime_Edit.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                HabitTime_Edit.setText(SimpleDateFormat("HH:mm a").format(cal.time))
            }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        HabitStartDate_Edit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    this@UpdateDetailActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })

        btn_update.setOnClickListener {
            val habits: HabitModel = HabitModel()

            /** Assigning input fields data into variables **/
            val Habittitle = habitTitle_Edit.text
            val Habitdescription = habitDescription_Edit.text
            val Habitgoal = HabitGoal_Edit.text
            val HabitreminderTime = HabitTime_Edit.text
            val HabitstartDate = HabitStartDate_Edit.text


            /**Prevent User Error**/
            if (Habittitle.isEmpty() || Habitdescription.isEmpty() || Habitgoal.isEmpty() || HabitreminderTime.isEmpty() || HabitstartDate.isEmpty()) {
                Toast.makeText(this, "Please fill the empty filed", Toast.LENGTH_SHORT).show()
            } else {
                habits.id = intent.getIntExtra("ID", 0)
                habits.title = habitTitle_Edit.text.toString()
                habits.description = habitDescription_Edit.text.toString()
                habits.goal = HabitGoal_Edit.text.toString().toInt()
                habits.reminderTime = HabitTime_Edit.text.toString()
                habits.startDate = HabitStartDate_Edit.text.toString()
                habits.habitImage = selectedImageTag


                /**User feedback status**/
                if (title == habits.title && description == habits.description && goal == habits.goal && reminderTime == habits.reminderTime && startDate == habits.startDate && imageHabit == habits.habitImage) {
                    Toast.makeText(this, "There is no changes occurred.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Habit Updated.", Toast.LENGTH_LONG).show()
                }

                val success = sqLiteHelper.updateHabit(habits)

                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            }
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        HabitStartDate_Edit.setText(sdf.format(cal.getTime()))
    }

    fun onImageClicked(view: View) {
        val image = view as ImageView
        val imgTag = image.tag.toString()
        val testImgId = this.resources.getIdentifier(imgTag, "drawable", packageName)

        image.setImageResource(testImgId)
        selectedImageTag = imgTag

        studyy.setBackgroundResource(if (selectedImageTag == "ic_study_work_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        smokee.setBackgroundResource(if (selectedImageTag == "ic_no_smoking_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        cyclingg.setBackgroundResource(if (selectedImageTag == "ic_cycling_sport_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        waterr.setBackgroundResource(if (selectedImageTag == "ic_water_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        sleepp.setBackgroundResource(if (selectedImageTag == "ic_sleep_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        wakeUpp.setBackgroundResource(if (selectedImageTag == "ic_wake_up_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        meditatee.setBackgroundResource(if (selectedImageTag == "ic_meditation_think_guru_meditate_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        sportt.setBackgroundResource(if (selectedImageTag == "ic_sport_faculty_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
        quitt.setBackgroundResource(if (selectedImageTag == "ic_stop_svgrepo_com") R.drawable.btn_img_border_selected else R.drawable.btn_img_border)
    }
}