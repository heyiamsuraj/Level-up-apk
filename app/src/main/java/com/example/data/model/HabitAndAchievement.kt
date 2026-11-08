package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val category: String, // Discipline, Study, Fitness, Health
  val attributeAffected: String, // DIS, VIT, STR, INT, FOC, END
  val xpReward: Int = 20,
  val currentStreak: Int = 0,
  val bestStreak: Int = 0,
  val lastCompletedDate: String = "",
  val isCompletedToday: Boolean = false
)

@Entity(tableName = "achievements")
data class AchievementEntity(
  @PrimaryKey val id: String,
  val title: String,
  val description: String,
  val rankRequirement: String, // E-Rank, D-Rank, C-Rank, B-Rank, A-Rank, S-Rank, National
  val category: String, // Quest, Study, Fitness, Discipline, Awakening
  val xpReward: Int = 100,
  val isUnlocked: Boolean = false,
  val unlockedDate: String = "",
  val progressCurrent: Int = 0,
  val progressTarget: Int = 1
) {
  val progressRatio: Float
    get() = if (progressTarget > 0) (progressCurrent.toFloat() / progressTarget.toFloat()).coerceIn(0f, 1f) else 1f
}
