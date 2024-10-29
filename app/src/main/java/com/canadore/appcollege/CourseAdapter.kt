package com.canadore.appcollege

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.canadore.appcollege.model.Course


class CourseAdapter(
    private val courses: List<Course>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    interface OnItemClickListener {
        fun onUpdate(course: Course)
        fun onDelete(course: Course)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.bind(course)
    }

    override fun getItemCount() = courses.size

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val courseName: TextView = itemView.findViewById(R.id.tvCourseName)
        private val courseCode: TextView = itemView.findViewById(R.id.tvCourseCode)
        private val btnUpdate: Button = itemView.findViewById(R.id.btnUpdate)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(course: Course) {
            courseName.text = course.name
            courseCode.text = course.code
            btnUpdate.setOnClickListener { listener.onUpdate(course) }
            btnDelete.setOnClickListener { listener.onDelete(course) }
        }
    }
}
