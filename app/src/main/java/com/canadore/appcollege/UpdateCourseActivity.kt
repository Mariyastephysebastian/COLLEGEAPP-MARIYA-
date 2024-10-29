package com.canadore.appcollege

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.canadore.appcollege.databinding.ActivityUpdateCourseBinding
import com.canadore.appcollege.model.Course
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class UpdateCourseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateCourseBinding
    private lateinit var databaseReference: DatabaseReference
    private var courseId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("courses")

        // Get course ID from Intent
        courseId = intent.getStringExtra("courseId") ?: ""

        // Load the course details from Firebase
        loadCourseDetails()

        binding.btnUpdate.setOnClickListener {
            val name = binding.etCourseName.text.toString()
            val code = binding.etCourseCode.text.toString()

            if (name.isNotEmpty() && code.isNotEmpty()) {
                updateCourse(courseId, name, code)
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCourseDetails() {
        databaseReference.child(courseId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val course = snapshot.getValue(Course::class.java)
                    course?.let {
                        binding.etCourseName.setText(it.name)
                        binding.etCourseCode.setText(it.code)
                    }
                } else {
                    Toast.makeText(this@UpdateCourseActivity, "Course not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UpdateCourseActivity, "Failed to load course: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateCourse(courseId: String, name: String, code: String) {
        val course = Course(Integer.parseInt(courseId), name, code)
        databaseReference.child(courseId).setValue(course).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Course updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            } else {
                Toast.makeText(this, "Failed to update course", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
