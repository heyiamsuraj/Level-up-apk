package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class PlayerEntity(
  @PrimaryKey val id: Int = 1,
  val name: String = "Hunter",
  val level: Int = 1,
  val currentXp: Int = 0,
  val rank: String = "E-Rank",
  val str: Int = 10,
  val intVal: Int = 10,
  val vit: Int = 10,
  val dis: Int = 10,
  val endVal: Int = 10,
  val foc: Int = 10,
  val streakDays: Int = 1,
  val currentWeight: Float = 76.0f,
  val targetWeight: Float = 68.0f,
  val heightCm: Float = 175.0f,
  val gateStudyTargetHours: Float = 3.5f,
  val collegeTiming: String = "09:00 - 16:30",
  val wakeTime: String = "06:30",
  val sleepTime: String = "23:00",
  val preparationLevel: String = "Intermediate",
  val isOnboarded: Boolean = false,
  val planningMode: String = "Normal College Day",
  val targetLevel: String = "Target Day",
  val soundAndHapticsEnabled: Boolean = true,
  val reducedMotion: Boolean = false,
  val dailySurvivalCompletedDate: String = ""
) {
  val xpRequiredForNextLevel: Int
    get() = level * 200 + 100

  val xpProgressRatio: Float
    get() = (currentXp.toFloat() / xpRequiredForNextLevel.toFloat()).coerceIn(0f, 1f)

  val bmi: Float
    get() {
      val heightM = heightCm / 100f
      return if (heightM > 0) currentWeight / (heightM * heightM) else 0f
    }

  companion object {
    fun calculateRank(level: Int): String = when {
      level >= 60 -> "National-Level Hunter"
      level >= 40 -> "S-Rank"
      level >= 25 -> "A-Rank"
      level >= 15 -> "B-Rank"
      level >= 10 -> "C-Rank"
      level >= 5 -> "D-Rank"
      else -> "E-Rank"
    }
  }
}
