package com.example.ccl3_bbetter_app.TrackingLog

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccl3_bbetter_app.DataBase.SQLiteHelper
import com.example.ccl3_bbetter_app.MainActivity2
import com.example.ccl3_bbetter_app.R
import com.example.ccl3_bbetter_app.adapter.ItemAdapter
import com.example.recyclerviewapp.TrackingLog.logAdapter
import kotlinx.android.synthetic.main.activity_detail_actvity.*
import kotlinx.android.synthetic.main.activity_log.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.recycler_view_items

class LogActivity : AppCompatActivity() {

    companion object {
        lateinit var logDB: LogDB
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        logDB = LogDB(this, null, 1)
        showLog()

        /**Back button**/
        Bck_btn4.setOnClickListener {
            finish()
        }

        /**Page Title = Habit name*/
        val title = intent.getStringExtra("HabitTitle")
        HabitLog_Title.text = title


        val counter = intent.getIntExtra("COUNTER", 0)
        val goal = intent.getIntExtra("GOAL", 0)

        /**Show Number of checkIns**/
        CounterLog.text = "Goal $counter/$goal"

    }

    /**Show Log list**/
    @SuppressLint("NotifyDataSetChanged")
    private fun showLog() {
        val habitId = intent.getIntExtra("HabitId",0)
        val logList = logDB.getAllLogsForHabit(habitId)

        // Adapter class is initialized and list is passed in the param.
        val adapter = logAdapter(this, logList)
        // Set the LayoutManager that this RecyclerView will use.
        recycler_view_items2.layoutManager = LinearLayoutManager(this)
        // adapter instance is set to the recyclerview to inflate the items.
        recycler_view_items2.adapter = adapter
        adapter.notifyDataSetChanged()

        /**Log Empty state**/
        if (logList.isEmpty()) {
            LogEmpty_State_Image.setVisibility(View.VISIBLE);
            Log_emptyState.setVisibility(View.VISIBLE);
            CounterLog.setVisibility(View.INVISIBLE);
        } else {
            LogEmpty_State_Image.setVisibility(View.INVISIBLE);
            Log_emptyState.setVisibility(View.INVISIBLE);
            CounterLog.setVisibility(View.VISIBLE);
        }
    }

    override fun onResume() {
        showLog()
        super.onResume()
    }
}