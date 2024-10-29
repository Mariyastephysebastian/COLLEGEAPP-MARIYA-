package com.canadore.appcollege

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.canadore.appcollege.databinding.ActivityAddCourseBinding
import com.canadore.appcollege.DatabaseHelper

class AddCourseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCourseBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.btnAdd.setOnClickListener {
            val name = binding.etCourseName.text.toString()
            val code = binding.etCourseCode.text.toString()

            if (name.isNotEmpty() && code.isNotEmpty()) {
                dbHelper.addCourse(name, code)
                Toast.makeText(this, "Course Added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
