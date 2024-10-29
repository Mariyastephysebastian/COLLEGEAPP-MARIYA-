package com.canadore.appcollege

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.canadore.appcollege.databinding.ActivityViewCoursesBinding
import com.canadore.appcollege.model.Course
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewCoursesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewCoursesBinding
    private lateinit var courseAdapter: CourseAdapter
    private var courseList = mutableListOf<Course>()
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().reference

        // Initialize the RecyclerView
        setupRecyclerView()

        // Load courses from the Firebase database
        loadCourses()
    }

    private fun setupRecyclerView() {
        courseAdapter = CourseAdapter(courseList, object : CourseAdapter.OnItemClickListener {
            override fun onUpdate(course: Course) {
                val intent = Intent(this@ViewCoursesActivity, UpdateCourseActivity::class.java)
                intent.putExtra("courseId", course.id)
                startActivity(intent)
            }

            override fun onDelete(course: Course) {
                deleteCourse(course.id.toString())
            }
        })

        // Setup RecyclerView
        binding.recyclerViewCourses.apply {
            layoutManager = LinearLayoutManager(this@ViewCoursesActivity) // Set layout manager
            adapter = courseAdapter // Set adapter
        }
    }

    private fun loadCourses() {
        databaseReference.child("courses").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                courseList.clear() // Clear the current list
                for (courseSnapshot in snapshot.children) {
                    val course = courseSnapshot.getValue(Course::class.java)
                    if (course != null) {
                        courseList.add(course)
                    }
                }
                Log.d("Courses", "Total courses: ${courseList.size}") // Log the number of courses
                courseAdapter.notifyDataSetChanged() // Notify adapter about the changes
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun deleteCourse(courseId: String) {
        databaseReference.child(courseId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Course deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete course", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadCourses() // Reload courses when the activity resumes
    }
}
