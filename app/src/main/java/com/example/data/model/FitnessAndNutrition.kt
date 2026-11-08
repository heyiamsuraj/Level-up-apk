package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val dateStr: String, // YYYY-MM-DD
  val category: String, // Gym, Home Workout, Walking, Running, Cycling, Yoga, Stretching
  val routineTitle: String,
  val durationMinutes: Int,
  val exercisesDetail: String, // e.g. "Barbell Squats 4x10, Bench Press 4x8, Lat Pulldown 3x12"
  val caloriesBurned: Int,
  val intensity: String = "Moderate" // Moderate, High, Intense
)

@Entity(tableName = "weight_logs")
data class WeightLogEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val dateStr: String,
  val weightKg: Float,
  val waistCm: Float = 0f,
  val notes: String = ""
)

@Entity(tableName = "nutrition_logs")
data class NutritionLogEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val dateStr: String,
  val foodName: String,
  val quantityServing: String, // e.g. "2 Rotis", "100g Paneer"
  val calories: Int,
  val proteinGrams: Float,
  val carbsGrams: Float = 0f,
  val fatsGrams: Float = 0f,
  val mealType: String // Breakfast, Lunch, Snacks, Dinner
)

@Entity(tableName = "water_logs")
data class WaterLogEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val dateStr: String,
  val amountMl: Int = 250
)
