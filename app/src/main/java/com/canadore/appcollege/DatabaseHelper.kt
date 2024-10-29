package com.canadore.appcollege

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.canadore.appcollege.model.Course

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "CollegeApp.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_COURSES = "courses"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_CODE = "code"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_COURSES ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT, $COLUMN_CODE TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COURSES")
        onCreate(db)
    }

    fun addCourse(name: String, code: String): String {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_CODE, code)
        }
        val id = db.insert(TABLE_COURSES, null, values)
        return id.toString() // Return ID as a String
    }

    @SuppressLint("Range")
    fun getAllCourses(): List<Course> {
        val courses = mutableListOf<Course>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_COURSES", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))// Get ID as String
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val code = cursor.getString(cursor.getColumnIndex(COLUMN_CODE))
                courses.add(Course(id, name, code))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return courses
    }

    fun updateCourse(id: String, name: String, code: String): Int { // Accept ID as String
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_CODE, code)
        }
        val idUpdated : Int  = db.update(TABLE_COURSES, values, "$COLUMN_ID = ?", arrayOf(id))

        db.close()
        return idUpdated
    }

    fun deleteCourse(id: String): Int { // Accept ID as String
        val db = this.writableDatabase
        return db.delete(TABLE_COURSES, "$COLUMN_ID = ?", arrayOf(id))
    }
}
