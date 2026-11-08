package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val sender: String, // "user" or "model"
  val message: String,
  val roleName: String = "System Guide",
  val modelName: String = "gemini-3.8-flash",
  val timestamp: Long = System.currentTimeMillis(),
  val isError: Boolean = false
)
