package com.example.ccl3_bbetter_app.DataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.ccl3_bbetter_app.model.HabitModel
import java.lang.Exception


class SQLiteHelper(
    context: Context,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "habit.db"


        private const val TBL_HABIT = "Habits"
        private const val ID = "id"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val GOAL = "goal"
        private const val REMINDER = "reminder"
        private const val STARTDATE = "startDate"
        private const val IMAGE = "habitImage"
        private const val COUNTER = "counter"
        private const val LASTCHECKIN = "lastCheck"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //Create table + columns
        val CREATE_TABLE_HABIT = (
                "CREATE TABLE " + TBL_HABIT + "("
                        + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + TITLE + " TEXT,"
                        + DESCRIPTION + " TEXT,"
                        + GOAL + " INTEGER,"
                        + REMINDER + " TEXT,"
                        + STARTDATE + " TEXT,"
                        + IMAGE + " TEXT,"
                        + COUNTER + " INTEGER,"
                        + LASTCHECKIN + " TEXT"
                        + ")")
        db?.execSQL(CREATE_TABLE_HABIT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_HABIT")
        onCreate(db)
    }


    /** Retrieve all Habits **/
    fun getAllHabit(context: Context): ArrayList<HabitModel> {
        val selectQuery = "SELECT * FROM $TBL_HABIT"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val habits = ArrayList<HabitModel>()

        if (cursor.count == 0) {

        } else {
            cursor.moveToFirst()
            while (!cursor.isAfterLast()) {
                val habit = HabitModel()
                habit.id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                habit.title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                habit.description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                habit.goal = cursor.getInt(cursor.getColumnIndexOrThrow(GOAL))
                habit.reminderTime = cursor.getString(cursor.getColumnIndexOrThrow(REMINDER))
                habit.startDate = cursor.getString(cursor.getColumnIndexOrThrow(STARTDATE))
                habit.habitImage = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE))
                habit.counterScore = cursor.getInt(cursor.getColumnIndexOrThrow(COUNTER))
                habit.lastCheck = cursor.getString(cursor.getColumnIndexOrThrow(LASTCHECKIN))

                habits.add(habit)
                cursor.moveToNext()
            }

        }
        cursor.close()
        //db.close()
        return habits
    }


    /**Insert Habits to dataBase Table**/
    fun insertHabit(context: Context, habit: HabitModel) {
        val contentValues = ContentValues()
        contentValues.put(TITLE, habit.title)
        contentValues.put(DESCRIPTION, habit.description)
        contentValues.put(GOAL, habit.goal)
        contentValues.put(REMINDER, habit.reminderTime)
        contentValues.put(STARTDATE, habit.startDate)
        contentValues.put(IMAGE, habit.habitImage)
        contentValues.put(COUNTER, habit.counterScore)
        contentValues.put(LASTCHECKIN, habit.lastCheck)

        val db = this.writableDatabase
        try {
            db.insert(TBL_HABIT, null, contentValues)
            // Toast.makeText(context, "Habit added", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }


    /** Update Habit details **/
    fun updateHabit(habit: HabitModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, habit.id)
        contentValues.put(TITLE, habit.title)
        contentValues.put(DESCRIPTION, habit.description)
        contentValues.put(GOAL, habit.goal)
        contentValues.put(REMINDER, habit.reminderTime)
        contentValues.put(STARTDATE, habit.startDate)
        contentValues.put(IMAGE, habit.habitImage)

        val success = db.update(TBL_HABIT, contentValues, "$ID=?", arrayOf(habit.id.toString()))
        return success

    }

    /** Update Tracking counter **/
    fun updateCounter(habit: HabitModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COUNTER, habit.counterScore)
        val success = db.update(TBL_HABIT, contentValues, "$ID=?", arrayOf(habit.id.toString()))
        return success

    }

    /** Update last checkIn **/
    fun updateCheckIn(habit: HabitModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(LASTCHECKIN, habit.lastCheck)
        val success = db.update(TBL_HABIT, contentValues, "$ID=?", arrayOf(habit.id.toString()))
        return success
    }

    /** Delete single Habit **/
    fun deleteHabit(id: Int): Boolean {
        val db = this.readableDatabase
        val selectQuery = "Delete From $TBL_HABIT where $ID = $id"
        var result: Boolean = false

        try {
            val cursor = db.execSQL(selectQuery)
            result = true
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error Deleting")
        }
        //db.close()
        return result
    }

    /** Delete All Habits **/

    fun deleteAllHabit(context: Context): Boolean {
        val db = this.writableDatabase
        var result: Boolean = false

        /**Error prevention**/
        try {
            db.delete(TBL_HABIT, null, null)
            result = true
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error Deleting")
        }
        //db.close()
        return result
    }


}