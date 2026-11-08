package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "college_timetable")
data class CollegeTimetableEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val dayOfWeek: String, // Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
  val subject: String,
  val timeSlot: String, // e.g. "09:30 - 10:30"
  val room: String = "Mech Dept LH-3",
  val attendedCount: Int = 18,
  val totalClassesCount: Int = 22
) {
  val attendancePercentage: Float
    get() = if (totalClassesCount > 0) (attendedCount.toFloat() / totalClassesCount.toFloat()) * 100f else 100f
}

@Entity(tableName = "project_milestones")
data class ProjectMilestoneEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val stage: String, // Literature Review, Methodology, Experimental Work, Data Collection, Analysis, Results, Documentation, Presentation
  val deadlineDate: String,
  val isCompleted: Boolean = false,
  val notes: String = "",
  val orderIndex: Int = 0
)

@Entity(tableName = "academic_assignments")
data class AssignmentEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val subject: String,
  val title: String,
  val deadlineDate: String,
  val isSubmitted: Boolean = false,
  val priority: String = "High"
)
