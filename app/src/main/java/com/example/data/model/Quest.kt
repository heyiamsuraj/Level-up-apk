package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quests")
data class QuestEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val description: String,
  val category: String, // GATE, College, Project, Fitness, Nutrition, Habit, Personal
  val priority: String = "Medium", // Urgent, High, Medium, Low
  val difficulty: String = "D", // E, D, C, B, A, S
  val xpReward: Int = 50,
  val strReward: Int = 0,
  val intReward: Int = 0,
  val vitReward: Int = 0,
  val disReward: Int = 0,
  val endReward: Int = 0,
  val focReward: Int = 0,
  val status: String = "Available", // Available, In Progress, Completed, Skipped, Rescheduled
  val isMainQuest: Boolean = true,
  val dateStr: String, // YYYY-MM-DD
  val startTime: String = "",
  val deadline: String = "",
  val completedTimestamp: Long = 0L,
  val isSurvivalQuestItem: Boolean = false
)
