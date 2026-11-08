package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val dateStr: String,
  val mode: String, // GATE Study, College Project, Reading, Workout
  val durationMinutes: Int,
  val xpEarned: Int,
  val focEarned: Int,
  val intEarned: Int,
  val questTitle: String = ""
)

@Entity(tableName = "motivational_quotes")
data class QuoteEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val text: String,
  val author: String,
  val category: String, // Discipline, GATE Prep, Academic Success, Fitness, Consistency, Resilience, Solo Leveling / Anime Wisdom
  val isFavorite: Boolean = false,
  val isCustom: Boolean = false
)
