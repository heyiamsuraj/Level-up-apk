package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dashboard_config")
data class DashboardConfigEntity(
  @PrimaryKey val id: Int = 1,
  val themePreset: String = "SYSTEM_DEFAULT", // SYSTEM_DEFAULT, SHADOW_HUNTER, DUNGEON_MODE, MINIMAL_SYSTEM, STUDY_MODE, FITNESS_MODE, AMOLED
  val wallpaperChoice: String = "bg_dungeon_gate", // bg_dungeon_gate, bg_shadow_monarch, solid_dark, custom_gallery
  val customWallpaperUri: String = "",
  val accentColorHex: String = "#00F0FF",
  val overlayDimming: Float = 0.65f,
  val blurRadius: Float = 0f,
  val showSurvivalQuestWidget: Boolean = true,
  val showAttributeWidget: Boolean = true,
  val showGateWidget: Boolean = true,
  val showCollegeWidget: Boolean = true,
  val showFitnessWidget: Boolean = true,
  val showQuoteWidget: Boolean = true
)
