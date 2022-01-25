package com.example.ccl3_bbetter_app.TrackingLog

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.ccl3_bbetter_app.TrackingLog.model.LogModel
import java.lang.Exception

class LogDB(
    context: Context,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "habitLog.db"

        private const val TBL_LOG = "Log"
        private const val ID = "id"
        private const val HABITID = "habitId"
        private const val HABITTITLE = "title"
        private const val CURRENTDATE = "currentDate"
        private const val CURRENTTIME = "currentTime"
        private const val STATUS = "status"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        //Create table + columns
        val CREATE_TABLE_HABIT = (
                "CREATE TABLE " + TBL_LOG + "("
                        + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + HABITID + " INTEGER,"
                        + HABITTITLE + " TEXT,"
                        + CURRENTDATE + " TEXT,"
                        + CURRENTTIME + " TEXT,"
                        + STATUS + " TEXT"
                        + ")")
        db?.execSQL(CREATE_TABLE_HABIT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_LOG")
        onCreate(db)
    }


    fun getAllLogsForHabit(habitId: Int): ArrayList<LogModel> {
        val selectQuery = "SELECT * FROM $TBL_LOG WHERE $HABITID =$habitId"
        //val selectQuery = "Delete From ${SQLiteHelper.TBL_HABIT} where ${SQLiteHelper.ID} = $id"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val logs = ArrayList<LogModel>()

        if (cursor.count == 0) {

        } else {
            cursor.moveToFirst()
            while (!cursor.isAfterLast()) {
                val log = LogModel()
                log.id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                log.habitTitle = cursor.getString(cursor.getColumnIndexOrThrow(HABITTITLE))
                log.habitId = cursor.getInt(cursor.getColumnIndexOrThrow(HABITID))
                log.status = cursor.getString(cursor.getColumnIndexOrThrow(STATUS))
                log.currentDate = cursor.getString(cursor.getColumnIndexOrThrow(CURRENTDATE))
                log.currentTime = cursor.getString(cursor.getColumnIndexOrThrow(CURRENTTIME))
                logs.add(log)
                cursor.moveToNext()
            }
        }
        cursor.close()
        //db.close()
        return logs
    }

    /**Delete Habit**/
    fun deleteHabitLogs(habitId: Int): Boolean {
        val db = this.readableDatabase
        val selectQuery = "Delete From ${TBL_LOG} where $HABITID = $habitId"
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

    fun deleteAllLogs(context: Context): Boolean {
        val db = this.writableDatabase
        var result: Boolean = false

        /**Error prevention**/
        try {
            db.delete(TBL_LOG, null, null)
            result = true
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error Deleting")
        }
        //db.close()
        return result
    }

    /**Insert Habits to dataBase Table**/
    fun insertLog(context: Context, log: LogModel) {
        val contentValues = ContentValues()
        contentValues.put(HABITID, log.habitId)
        contentValues.put(HABITTITLE, log.habitTitle)
        contentValues.put(CURRENTDATE, log.currentDate)
        contentValues.put(CURRENTTIME, log.currentTime)
        contentValues.put(STATUS, log.status)

        val db = this.writableDatabase
        try {
            db.insert(TBL_LOG, null, contentValues)
            Toast.makeText(context, "Habit added", Toast.LENGTH_SHORT)
                .show()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        //db.close()
    }

}