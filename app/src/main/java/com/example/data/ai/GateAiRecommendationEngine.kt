package com.example.data.ai

import android.util.Log
import com.example.data.model.GateSubjectEntity
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

private const val TAG = "GateAiEngine"

data class GateTopicRecommendation(
  val topicName: String,
  val subjectName: String,
  val subjectCode: String,
  val priorityRank: String, // CRITICAL (RANK S), HIGH (RANK A), STRATEGIC (RANK B)
  val difficulty: String, // Rank S, Rank A, Rank B, Rank C
  val importanceScore: Int, // 1 - 100
  val estimatedStudyMinutes: Int,
  val rationale: String,
  val suggestedActions: List<String>
)

sealed interface AiRecommendationState {
  data object Idle : AiRecommendationState
  data object Loading : AiRecommendationState
  data class Success(val recommendations: List<GateTopicRecommendation>, val isLiveAi: Boolean) : AiRecommendationState
  data class Error(val message: String, val fallbackRecommendations: List<GateTopicRecommendation>) : AiRecommendationState
}

class GateAiRecommendationEngine {

  private val generativeModel by lazy {
    Firebase.ai.generativeModel(
      modelName = "gemini-3.8-flash"
    )
  }

  suspend fun analyzeAndRecommend(subjects: List<GateSubjectEntity>): AiRecommendationState {
    val analyticalFallback = generateAnalyticalRecommendations(subjects)

    if (subjects.isEmpty()) {
      return AiRecommendationState.Success(analyticalFallback, isLiveAi = false)
    }

    return try {
      val prompt = buildAnalysisPrompt(subjects)
      val response = withTimeout(45_000L) {
        generativeModel.generateContent(prompt)
      }

      val jsonText = response.text
      if (!jsonText.isNullOrBlank()) {
        val parsedList = parseRecommendationsJson(jsonText)
        if (parsedList.isNotEmpty()) {
          AiRecommendationState.Success(parsedList, isLiveAi = true)
        } else {
          AiRecommendationState.Success(analyticalFallback, isLiveAi = false)
        }
      } else {
        AiRecommendationState.Success(analyticalFallback, isLiveAi = false)
      }
    } catch (e: TimeoutCancellationException) {
      Log.w(TAG, "GATE AI recommendation timed out, utilizing deterministic analytical model", e)
      AiRecommendationState.Success(analyticalFallback, isLiveAi = false)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      Log.e(TAG, "Firebase AI Logic inference error, providing deterministic fallback", e)
      AiRecommendationState.Success(analyticalFallback, isLiveAi = false)
    }
  }

  private fun buildAnalysisPrompt(subjects: List<GateSubjectEntity>): String {
    val subjectSummary = subjects.joinToString("\n") { s ->
      "- ${s.name} (${s.code}): Weightage: ${s.weightageMarks} Marks. Completed: ${s.completedTopics}/${s.totalTopics} topics (${(s.completionPercentage).toInt()}%). PYQs Solved: ${s.pyqsSolved}/${s.pyqTarget}. Weak Area: ${s.isWeakArea}. Key Notes: ${s.notes}"
    }

    return """
You are the AI System Guide for a final-year Mechanical Engineering student preparing for GATE ME.
Analyze the user's current preparation data:
$subjectSummary

Recommend the TOP 4 specific high-yield topics that require immediate study today.
Prioritize:
1. Topics with high exam weightage (e.g. Thermodynamics, SOM, Fluid Mechanics, Manufacturing).
2. The user's flagged weak areas or subjects with low PYQ completion.
Respond ONLY with a valid JSON object in this exact structure without markdown code blocks:
{
  "recommendations": [
    {
      "topicName": "Specific high-yield topic",
      "subjectName": "Subject Name",
      "subjectCode": "Code",
      "priorityRank": "CRITICAL (RANK S)",
      "difficulty": "Rank S",
      "importanceScore": 95,
      "estimatedStudyMinutes": 60,
      "rationale": "Why this topic is prioritized based on weightage and weak areas",
      "action1": "Specific revision step 1",
      "action2": "Specific revision step 2"
    }
  ]
}
""".trimIndent()
  }

  private fun parseRecommendationsJson(jsonStr: String): List<GateTopicRecommendation> {
    val results = mutableListOf<GateTopicRecommendation>()
    try {
      val cleanJson = jsonStr.trim()
        .removePrefix("```json")
        .removePrefix("```")
        .removeSuffix("```")
        .trim()
      val root = JSONObject(cleanJson)
      val arr = root.optJSONArray("recommendations") ?: return emptyList()
      for (i in 0 until arr.length()) {
        val obj = arr.getJSONObject(i)
        val actions = mutableListOf<String>()
        if (obj.has("action1")) actions.add(obj.getString("action1"))
        if (obj.has("action2")) actions.add(obj.getString("action2"))
        if (actions.isEmpty()) actions.add("Solve 15 previous year GATE numericals.")

        results.add(
          GateTopicRecommendation(
            topicName = obj.optString("topicName", "Core Mechanical Topic"),
            subjectName = obj.optString("subjectName", "GATE Mechanical"),
            subjectCode = obj.optString("subjectCode", "ME"),
            priorityRank = obj.optString("priorityRank", "HIGH"),
            difficulty = obj.optString("difficulty", "Rank A"),
            importanceScore = obj.optInt("importanceScore", 90),
            estimatedStudyMinutes = obj.optInt("estimatedStudyMinutes", 60),
            rationale = obj.optString("rationale", "High syllabus weightage & targeted revision needed."),
            suggestedActions = actions
          )
        )
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error parsing recommendations JSON", e)
    }
    return results
  }

  fun generateAnalyticalRecommendations(subjects: List<GateSubjectEntity>): List<GateTopicRecommendation> {
    // Subject to high-yield GATE topics map
    val highYieldTopicMap = mapOf(
      "FM" to Triple("Boundary Layer Theory & Pipe Flow Losses", "Rank A", "Highest question frequency in Fluid Mechanics (3-4 marks). Requires Reynolds, Blasius thickness & Darcy friction factor."),
      "TD" to Triple("Entropy Generation & Exergy Analysis", "Rank S", "Guaranteed 2-mark numerical every GATE ME paper. Resolves Second Law efficiency and availability loss."),
      "TOM" to Triple("Vibrations: Single DOF Free & Forced Damped Systems", "Rank S", "Critical high-scoring area. Master logarithmic decrement, critical damping ratio & transmissibility ratio."),
      "SOM" to Triple("Mohr's Circle, Combined Stresses & Principal Planes", "Rank A", "Fundamental cornerstone for machine design and fracture mechanics. Solve 10 2-D stress transformation problems."),
      "HT" to Triple("Heat Exchangers: LMTD & NTU Effectiveness Relations", "Rank B", "Consistent 2-mark formula-based problem. Revise counter-flow vs parallel flow NTU equations."),
      "ME" to Triple("Taylor's Tool Life Equation & Orthogonal Metal Cutting", "Rank A", "Manufacturing carries 14 marks. Merchant circle force analysis and tool life optimization are essential."),
      "EM" to Triple("Linear Algebra: Eigenvalues & Cayley-Hamilton Theorem", "Rank B", "Guaranteed 2-3 marks in general engineering mathematics across all sets.")
    )

    // Calculate urgency score for each subject
    val sortedSubjects = subjects.sortedByDescending { s ->
      val weightScore = s.weightageMarks * 5.0f
      val completionGap = (1.0f - (s.completionPercentage / 100f)) * 30.0f
      val pyqGap = (1.0f - s.pyqProgressRatio) * 25.0f
      val weakBonus = if (s.isWeakArea) 35.0f else 0.0f
      weightScore + completionGap + pyqGap + weakBonus
    }

    val recommendations = mutableListOf<GateTopicRecommendation>()
    for (s in sortedSubjects.take(4)) {
      val topicInfo = highYieldTopicMap[s.code] ?: Triple(
        "Fundamental Numerical Derivations & PYQs",
        "Rank A",
        "Essential weightage (${s.weightageMarks} marks). Solve pending questions to close syllabus gap."
      )

      val priority = if (s.isWeakArea || s.weightageMarks >= 9f) "CRITICAL (RANK S)" else "HIGH (RANK A)"
      val score = (80 + (s.weightageMarks * 1.5f).toInt()).coerceIn(75, 99)

      recommendations.add(
        GateTopicRecommendation(
          topicName = topicInfo.first,
          subjectName = s.name,
          subjectCode = s.code,
          priorityRank = priority,
          difficulty = topicInfo.second,
          importanceScore = score,
          estimatedStudyMinutes = if (s.isWeakArea) 75 else 50,
          rationale = "${topicInfo.third} Current progress: ${s.completedTopics}/${s.totalTopics} topics, ${s.pyqsSolved} PYQs.",
          suggestedActions = listOf(
            "Review key formula derivations & boundary constraints (15 mins)",
            "Solve 12-15 previous year GATE numericals with virtual calculator (45 mins)",
            "Catalog error types and note shortcut relations (15 mins)"
          )
        )
      )
    }

    return recommendations
  }
}
