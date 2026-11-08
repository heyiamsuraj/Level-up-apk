package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attributes")
data class AttributeEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val userId: Long = 1L,
  val code: String, // STR, AGI, INT, VIT, END, FOC, DIS
  val name: String, // Strength, Agility, Intelligence, Vitality, Endurance, Focus, Discipline
  val baseValue: Int = 10,
  val bonusValue: Int = 0,
  val hexColor: String = "#00F0FF",
  val description: String = "",
  val lastTrainedTimestamp: Long = System.currentTimeMillis()
) {
  val totalValue: Int
    get() = baseValue + bonusValue
}
