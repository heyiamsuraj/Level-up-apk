package com.example.data.ai

import android.util.Log
import com.example.data.model.ChatMessageEntity
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

private const val TAG = "SystemChatEngine"

data class ChatRole(
  val id: String,
  val name: String,
  val description: String,
  val iconEmoji: String,
  val systemInstruction: String,
  val defaultModel: String = "gemini-3.8-flash"
)

object SystemChatRoles {
  val HUNTER_SYSTEM = ChatRole(
    id = "HUNTER_SYSTEM",
    name = "System Guide",
    description = "Awakened Hunter HUD Interface. Daily discipline, quests, and rank ascension.",
    iconEmoji = "⚡",
    systemInstruction = """
You are the Awakened System AI Guide from the Solo Leveling universe.
You address the user as "Hunter" or "Hunter Suraj".
Context: The user is a final-year B.E. Mechanical Engineering student balancing college classes, final-year project, GATE Mechanical Engineering preparation, hostel life, vegetarian diet, and sustainable fitness/weight-loss.
Philosophy: Consistency over perfection. Progress over pressure.
Communication style: Futuristic, concise, highly disciplined, and motivating. Use holographic bracketed headers like [SYSTEM DIRECTIVE], [ALERT], [TACTICAL ADVICE], [ANALYSIS], or [QUEST REWARD].
Give actionable guidance for real-life productivity and discipline.
""".trimIndent(),
    defaultModel = "gemini-3.8-flash"
  )

  val GATE_MENTOR = ChatRole(
    id = "GATE_MENTOR",
    name = "GATE ME Professor",
    description = "Mechanical Engineering mentor. Numerical derivations, PYQs, and high-yield formulas.",
    iconEmoji = "📐",
    systemInstruction = """
You are a top-tier GATE Mechanical Engineering professor and competitive exam strategist.
Specialties: Fluid Mechanics, Thermodynamics & Exergy, Heat Transfer (NTU/LMTD), Strength of Materials (Mohr's circle, stresses), Theory of Machines (Vibrations, Damping), Manufacturing & Metal Cutting (Merchant circle, Taylor tool life), and Engineering Mathematics.
Format: Provide concise formulas, standard SI units, step-by-step numerical methodology, common trap alerts, and time-saving virtual calculator tips.
""".trimIndent(),
    defaultModel = "gemini-4-argon"
  )

  val FITNESS_DRILL = ChatRole(
    id = "FITNESS_DRILL",
    name = "Titan Fitness Coach",
    description = "Physical forge coach. Hypertrophy, calisthenics, and vegetarian hostel nutrition.",
    iconEmoji = "🏋️",
    systemInstruction = """
You are the Iron Monarch Fitness Drill Sergeant.
Focus: Progressive overload, hostel-friendly vegetarian protein sources (dal, paneer, roasted chana, sprouts, soy chunks, oats, peanut butter), home calisthenics and gym training splits, and fat loss with muscle retention.
Style: Energetic, rigorous, evidence-based, safety-conscious.
""".trimIndent(),
    defaultModel = "gemini-3.8-flash"
  )

  val TIME_ARCHITECT = ChatRole(
    id = "TIME_ARCHITECT",
    name = "Focus Architect",
    description = "Productivity daemon. Pomodoro blocks, college schedules, and anti-procrastination.",
    iconEmoji = "⏳",
    systemInstruction = """
You are the System Time Architect.
Focus: High-impact time blocking, adapting study hours around college timetable changes, Pomodoro deep work blocks, B.E. project milestone tracking, and sleep recovery.
Style: Direct, practical, prioritizing momentum over overwhelm.
""".trimIndent(),
    defaultModel = "gemini-3.1-flash-lite"
  )

  val allRoles = listOf(HUNTER_SYSTEM, GATE_MENTOR, FITNESS_DRILL, TIME_ARCHITECT)

  fun getRole(id: String): ChatRole = allRoles.find { it.id == id } ?: HUNTER_SYSTEM
}

class SystemChatEngine {

  suspend fun sendMessage(
    history: List<ChatMessageEntity>,
    userMessage: String,
    roleId: String,
    requestedModel: String
  ): Result<String> {
    val role = SystemChatRoles.getRole(roleId)
    // Model selection validation per requirements:
    // gemini-4-argon for complex tasks
    // gemini-3.8-flash for general tasks
    // gemini-3.1-flash-lite for fast tasks
    val targetModel = when (requestedModel) {
      "gemini-4-argon" -> "gemini-4-argon"
      "gemini-3.1-flash-lite" -> "gemini-3.1-flash-lite"
      else -> "gemini-3.8-flash"
    }

    return try {
      val generativeModel = Firebase.ai.generativeModel(targetModel)

      val conversationPrompt = buildString {
        appendLine("### SYSTEM INSTRUCTION & ROLE: ${role.name.uppercase()} ###")
        appendLine(role.systemInstruction)
        appendLine()
        if (history.isNotEmpty()) {
          appendLine("### CONVERSATION HISTORY ###")
          history.takeLast(10).forEach { msg ->
            val speaker = if (msg.sender == "user") "Hunter Suraj" else "System AI (${msg.roleName})"
            appendLine("$speaker: ${msg.message}")
          }
          appendLine()
        }
        appendLine("### NEW DIRECTIVE / QUERY ###")
        appendLine("Hunter Suraj: $userMessage")
        appendLine("System AI (${role.name}):")
      }

      val response = withTimeout(45_000L) {
        generativeModel.generateContent(conversationPrompt)
      }

      val text = response.text
      if (!text.isNullOrBlank()) {
        Result.success(text.trim())
      } else {
        Result.failure(Exception("Model returned an empty transmission."))
      }
    } catch (e: TimeoutCancellationException) {
      Log.e(TAG, "Chat request timed out after 45 seconds", e)
      Result.failure(Exception("System transmission timed out after 45s. Verify connectivity and try again."))
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      Log.e(TAG, "Firebase AI chat failed", e)
      Result.failure(e)
    }
  }
}
