package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gate_subjects")
data class GateSubjectEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val name: String,
  val code: String, // e.g. EM, SOM, TOM, TD, FM, HT, MD, IE, ME
  val weightageMarks: Float = 8.0f,
  val totalTopics: Int = 20,
  val completedTopics: Int = 0,
  val pyqsSolved: Int = 0,
  val pyqTarget: Int = 100,
  val studyMinutesTotal: Int = 0,
  val notes: String = "",
  val isWeakArea: Boolean = false,
  val lastRevisedDate: String = ""
) {
  val completionPercentage: Float
    get() = if (totalTopics > 0) (completedTopics.toFloat() / totalTopics.toFloat()) * 100f else 0f

  val pyqProgressRatio: Float
    get() = if (pyqTarget > 0) (pyqsSolved.toFloat() / pyqTarget.toFloat()).coerceIn(0f, 1f) else 0f
}
