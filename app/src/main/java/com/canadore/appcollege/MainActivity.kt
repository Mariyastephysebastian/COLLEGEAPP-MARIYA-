package com.canadore.appcollege

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.canadore.appcollege.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { binding = it }
        setContentView(binding.root)

        binding.btnAddCourse.setOnClickListener {
            startActivity(Intent(this, AddCourseActivity::class.java))
        }

        binding.btnViewCourses.setOnClickListener {
            startActivity(Intent(this, ViewCoursesActivity::class.java))
        }
    }
}
