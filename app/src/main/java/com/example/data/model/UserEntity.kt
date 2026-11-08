package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 1L,
  val username: String = "Hunter",
  val email: String = "hunter@system.awakened",
  val level: Int = 1,
  val currentXp: Int = 0,
  val rank: String = "E-Rank",
  val title: String = "The Awakened Mechanical Engineer",
  val collegeTiming: String = "09:00 - 16:30",
  val wakeTime: String = "06:30",
  val sleepTime: String = "23:00",
  val currentWeight: Float = 76.0f,
  val targetWeight: Float = 68.0f,
  val heightCm: Float = 175.0f,
  val gateStudyTargetHours: Float = 3.5f,
  val preparationLevel: String = "Intermediate",
  val streakDays: Int = 1,
  val isSystemAwakened: Boolean = true,
  val createdAt: Long = System.currentTimeMillis(),
  val lastActiveAt: Long = System.currentTimeMillis()
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
}
