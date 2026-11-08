package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.PrepopulateData
import com.example.data.model.AchievementEntity
import com.example.data.model.AssignmentEntity
import com.example.data.model.CollegeTimetableEntity
import com.example.data.model.DashboardConfigEntity
import com.example.data.model.FocusSessionEntity
import com.example.data.model.GateSubjectEntity
import com.example.data.model.HabitEntity
import com.example.data.model.NutritionLogEntity
import com.example.data.model.PlayerEntity
import com.example.data.model.ProjectMilestoneEntity
import com.example.data.model.QuestEntity
import com.example.data.model.QuoteEntity
import com.example.data.model.WaterLogEntity
import com.example.data.model.WeightLogEntity
import com.example.data.model.WorkoutLogEntity
import com.example.data.repository.LevelUpResult
import com.example.data.repository.SystemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import com.example.data.ai.AiRecommendationState
import com.example.data.ai.GateAiRecommendationEngine
import com.example.data.ai.GateTopicRecommendation
import com.example.data.ai.SystemChatEngine
import com.example.data.ai.SystemChatRoles
import com.example.data.fitness.GeneratedWorkoutPlan
import com.example.data.fitness.WorkoutGenerator
import com.example.data.model.AttributeEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SystemViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: SystemRepository
  private val aiEngine = GateAiRecommendationEngine()
  private val chatEngine = SystemChatEngine()

  val todayDateStr = PrepopulateData.todayDateStr()

  val player: StateFlow<PlayerEntity>
  val user: StateFlow<UserEntity>
  val attributes: StateFlow<List<AttributeEntity>>
  val chatMessages: StateFlow<List<ChatMessageEntity>>
  val dashboardConfig: StateFlow<DashboardConfigEntity>
  val todayQuests: StateFlow<List<QuestEntity>>
  val gateSubjects: StateFlow<List<GateSubjectEntity>>
  val timetable: StateFlow<List<CollegeTimetableEntity>>
  val projectMilestones: StateFlow<List<ProjectMilestoneEntity>>
  val assignments: StateFlow<List<AssignmentEntity>>
  val habits: StateFlow<List<HabitEntity>>
  val achievements: StateFlow<List<AchievementEntity>>
  val focusSessions: StateFlow<List<FocusSessionEntity>>
  val workoutLogs: StateFlow<List<WorkoutLogEntity>>
  val weightLogs: StateFlow<List<WeightLogEntity>>
  val todayNutrition: StateFlow<List<NutritionLogEntity>>
  val todayWaterLogs: StateFlow<List<WaterLogEntity>>
  val quotes: StateFlow<List<QuoteEntity>>

  private val _isChatSending = MutableStateFlow(false)
  val isChatSending: StateFlow<Boolean> = _isChatSending.asStateFlow()

  private val _chatError = MutableStateFlow<String?>(null)
  val chatError: StateFlow<String?> = _chatError.asStateFlow()

  private val _selectedChatRole = MutableStateFlow("HUNTER_SYSTEM")
  val selectedChatRole: StateFlow<String> = _selectedChatRole.asStateFlow()

  private val _selectedChatModel = MutableStateFlow("gemini-3.8-flash")
  val selectedChatModel: StateFlow<String> = _selectedChatModel.asStateFlow()

  private val _aiRecommendationState = MutableStateFlow<AiRecommendationState>(AiRecommendationState.Idle)
  val aiRecommendationState: StateFlow<AiRecommendationState> = _aiRecommendationState.asStateFlow()

  private val _generatedWorkoutPlan = MutableStateFlow<GeneratedWorkoutPlan?>(null)
  val generatedWorkoutPlan: StateFlow<GeneratedWorkoutPlan?> = _generatedWorkoutPlan.asStateFlow()

  private val _levelUpEvent = MutableStateFlow<LevelUpResult?>(null)
  val levelUpEvent: StateFlow<LevelUpResult?> = _levelUpEvent.asStateFlow()

  init {
    val database = AppDatabase.getDatabase(application)
    repository = SystemRepository(database.systemDao())

    viewModelScope.launch {
      repository.ensureInitialized()
    }

    player = repository.playerFlow
      .map { it ?: PrepopulateData.defaultPlayer }
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PrepopulateData.defaultPlayer)

    user = repository.userFlow
      .map { it ?: PrepopulateData.defaultUser }
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PrepopulateData.defaultUser)

    attributes = repository.attributesFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PrepopulateData.defaultAttributes)

    chatMessages = repository.chatMessagesFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf(PrepopulateData.initialChatMessage))

    dashboardConfig = repository.dashboardConfigFlow
      .map { it ?: PrepopulateData.defaultDashboardConfig }
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PrepopulateData.defaultDashboardConfig)

    todayQuests = repository.getQuestsForDate(todayDateStr)
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    gateSubjects = repository.gateSubjectsFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    timetable = repository.timetableFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    projectMilestones = repository.projectMilestonesFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    assignments = repository.assignmentsFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    habits = repository.habitsFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    achievements = repository.achievementsFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    focusSessions = repository.focusSessionsFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    workoutLogs = repository.workoutLogsFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    weightLogs = repository.weightLogsFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    todayNutrition = repository.getNutritionForDate(todayDateStr)
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    todayWaterLogs = repository.getWaterLogsForDate(todayDateStr)
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    quotes = repository.allQuotesFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
  }

  fun completeQuest(questId: Long) {
    viewModelScope.launch {
      val result = repository.completeQuest(questId)
      if (result.didLevelUp) {
        _levelUpEvent.value = result
      }
    }
  }

  fun updateQuestStatus(quest: QuestEntity, newStatus: String) {
    viewModelScope.launch {
      repository.updateQuestStatus(quest, newStatus)
    }
  }

  fun addQuest(
    title: String,
    description: String,
    category: String,
    priority: String,
    difficulty: String,
    xpReward: Int,
    isMainQuest: Boolean,
    startTime: String,
    deadline: String
  ) {
    viewModelScope.launch {
      val quest = QuestEntity(
        title = title,
        description = description,
        category = category,
        priority = priority,
        difficulty = difficulty,
        xpReward = xpReward,
        isMainQuest = isMainQuest,
        dateStr = todayDateStr,
        startTime = startTime,
        deadline = deadline,
        intReward = if (category == "GATE") xpReward / 2 else 0,
        strReward = if (category == "Fitness") xpReward / 2 else 0,
        disReward = if (category == "College") xpReward / 2 else 10,
        focReward = 10,
        vitReward = if (category == "Health" || category == "Nutrition") xpReward / 2 else 0
      )
      repository.insertQuest(quest)
    }
  }

  fun deleteQuest(id: Long) {
    viewModelScope.launch {
      repository.deleteQuest(id)
    }
  }

  fun toggleHabit(habit: HabitEntity) {
    viewModelScope.launch {
      repository.toggleHabit(habit)
    }
  }

  fun logGateStudy(subjectId: Int, minutes: Int, pyqs: Int, topicCompleted: Boolean) {
    viewModelScope.launch {
      repository.logGateStudy(subjectId, minutes, pyqs, topicCompleted)
    }
  }

  fun recordAttendance(classId: Long, attended: Boolean) {
    viewModelScope.launch {
      repository.recordAttendance(classId, attended)
    }
  }

  fun toggleProjectMilestone(milestone: ProjectMilestoneEntity) {
    viewModelScope.launch {
      repository.toggleProjectMilestone(milestone)
    }
  }

  fun logWorkout(
    category: String,
    routineTitle: String,
    durationMinutes: Int,
    exercisesDetail: String,
    caloriesBurned: Int,
    intensity: String
  ) {
    viewModelScope.launch {
      val workout = WorkoutLogEntity(
        dateStr = todayDateStr,
        category = category,
        routineTitle = routineTitle,
        durationMinutes = durationMinutes,
        exercisesDetail = exercisesDetail,
        caloriesBurned = caloriesBurned,
        intensity = intensity
      )
      repository.logWorkout(workout)
    }
  }

  fun logWeight(weightKg: Float, waistCm: Float, notes: String) {
    viewModelScope.launch {
      repository.logWeight(weightKg, waistCm, notes)
    }
  }

  fun addNutritionItem(
    foodName: String,
    serving: String,
    calories: Int,
    protein: Float,
    carbs: Float,
    fats: Float,
    mealType: String
  ) {
    viewModelScope.launch {
      val item = NutritionLogEntity(
        dateStr = todayDateStr,
        foodName = foodName,
        quantityServing = serving,
        calories = calories,
        proteinGrams = protein,
        carbsGrams = carbs,
        fatsGrams = fats,
        mealType = mealType
      )
      repository.addNutritionItem(item)
    }
  }

  fun deleteNutritionItem(id: Long) {
    viewModelScope.launch {
      repository.deleteNutritionItem(id)
    }
  }

  fun logWater(amountMl: Int) {
    viewModelScope.launch {
      repository.logWater(amountMl)
    }
  }

  fun completeFocusSession(mode: String, minutes: Int, questTitle: String) {
    viewModelScope.launch {
      val result = repository.completeFocusSession(mode, minutes, questTitle)
      if (result.didLevelUp) {
        _levelUpEvent.value = result
      }
    }
  }

  fun switchPlanningMode(mode: String, targetLevel: String) {
    viewModelScope.launch {
      repository.switchPlanningMode(mode, targetLevel)
    }
  }

  fun completeSurvivalQuest() {
    viewModelScope.launch {
      val p = player.value
      repository.updatePlayer(p.copy(dailySurvivalCompletedDate = todayDateStr))
      val result = repository.awardProgress(xp = 100, str = 15, intVal = 15, vit = 20, dis = 20, endVal = 15, foc = 15)
      if (result.didLevelUp) {
        _levelUpEvent.value = result
      }
    }
  }

  fun completeOnboarding(
    name: String,
    collegeTiming: String,
    wakeTime: String,
    sleepTime: String,
    gateHours: Float,
    currentWeight: Float,
    targetWeight: Float,
    prepLevel: String
  ) {
    viewModelScope.launch {
      val p = player.value.copy(
        name = name.ifBlank { "Hunter" },
        collegeTiming = collegeTiming,
        wakeTime = wakeTime,
        sleepTime = sleepTime,
        gateStudyTargetHours = gateHours,
        currentWeight = currentWeight,
        targetWeight = targetWeight,
        preparationLevel = prepLevel,
        isOnboarded = true
      )
      repository.updatePlayer(p)
      // Award awakening XP
      val result = repository.awardProgress(xp = 100)
      if (result.didLevelUp) {
        _levelUpEvent.value = result
      }
    }
  }

  fun updateDashboardSettings(
    themePreset: String,
    wallpaper: String,
    accentHex: String,
    dimming: Float,
    blur: Float
  ) {
    viewModelScope.launch {
      val current = dashboardConfig.value
      repository.updateDashboardConfig(
        current.copy(
          themePreset = themePreset,
          wallpaperChoice = wallpaper,
          accentColorHex = accentHex,
          overlayDimming = dimming,
          blurRadius = blur
        )
      )
    }
  }

  fun toggleQuoteFavorite(quote: QuoteEntity) {
    viewModelScope.launch {
      repository.toggleQuoteFavorite(quote)
    }
  }

  fun addCustomQuote(text: String, author: String, category: String) {
    viewModelScope.launch {
      repository.addCustomQuote(text, author, category)
    }
  }

  fun deleteQuote(id: Long) {
    viewModelScope.launch {
      repository.deleteQuote(id)
    }
  }

  fun dismissLevelUp() {
    _levelUpEvent.value = null
  }

  fun refreshAiRecommendations() {
    viewModelScope.launch {
      _aiRecommendationState.value = AiRecommendationState.Loading
      val subjects = gateSubjects.value
      val result = aiEngine.analyzeAndRecommend(subjects)
      _aiRecommendationState.value = result
    }
  }

  fun acceptRecommendedTopicAsQuest(rec: GateTopicRecommendation) {
    viewModelScope.launch {
      val actionsSummary = rec.suggestedActions.joinToString(" • ")
      val quest = QuestEntity(
        title = "GATE ME: ${rec.topicName} (${rec.subjectCode})",
        description = "${rec.rationale} Directives: $actionsSummary",
        category = "GATE",
        priority = "Urgent",
        difficulty = when {
          rec.difficulty.contains("S") -> "S"
          rec.difficulty.contains("A") -> "A"
          rec.difficulty.contains("B") -> "B"
          else -> "C"
        },
        xpReward = rec.importanceScore,
        intReward = (rec.importanceScore * 0.6f).toInt(),
        focReward = (rec.importanceScore * 0.3f).toInt(),
        isMainQuest = true,
        dateStr = todayDateStr,
        startTime = "18:00",
        deadline = "20:00"
      )
      repository.insertQuest(quest)
    }
  }

  fun generateWorkoutRoutine(
    equipment: String,
    durationMinutes: Int,
    targetMuscles: String,
    rpgClass: String
  ) {
    val plan = WorkoutGenerator.generateRoutine(equipment, durationMinutes, targetMuscles, rpgClass)
    _generatedWorkoutPlan.value = plan
  }

  fun clearGeneratedWorkout() {
    _generatedWorkoutPlan.value = null
  }

  fun acceptWorkoutPlan(plan: GeneratedWorkoutPlan) {
    viewModelScope.launch {
      // 1. Create a Quest
      val quest = QuestEntity(
        title = plan.title,
        description = "Class: ${plan.rpgClass} • Equipment: ${plan.equipment} • Target: ${plan.targetMuscles} (${plan.durationMinutes}m)",
        category = "Fitness",
        priority = "High",
        difficulty = "C",
        xpReward = plan.xpReward,
        strReward = plan.strReward,
        endReward = plan.endReward,
        vitReward = plan.vitReward,
        isMainQuest = true,
        dateStr = todayDateStr,
        startTime = "07:00",
        deadline = "08:15"
      )
      repository.insertQuest(quest)

      // 2. Also log workout details
      val allExerciseNames = plan.phases.flatMap { it.exercises }.joinToString("; ") { "${it.name} (${it.sets}x${it.reps})" }
      val workout = WorkoutLogEntity(
        dateStr = todayDateStr,
        category = if (plan.equipment == "Full Gym") "Gym" else "Home Workout",
        routineTitle = plan.title,
        durationMinutes = plan.durationMinutes,
        exercisesDetail = allExerciseNames,
        caloriesBurned = (plan.durationMinutes * 6.5f).toInt(),
        intensity = "High"
      )
      repository.logWorkout(workout)
    }
  }

  fun setSystemAccentColor(hex: String) {
    viewModelScope.launch {
      val current = dashboardConfig.value
      repository.updateDashboardConfig(current.copy(accentColorHex = hex))
    }
  }

  // --- Gemini Chatbot Multi-Turn Operations ---
  fun selectChatRole(roleId: String) {
    _selectedChatRole.value = roleId
    val role = SystemChatRoles.getRole(roleId)
    _selectedChatModel.value = role.defaultModel
  }

  fun selectChatModel(modelName: String) {
    _selectedChatModel.value = modelName
  }

  fun sendChatMessage(text: String) {
    val trimmed = text.trim()
    if (trimmed.isBlank() || _isChatSending.value) return

    val role = SystemChatRoles.getRole(_selectedChatRole.value)
    val model = _selectedChatModel.value
    _chatError.value = null
    _isChatSending.value = true

    viewModelScope.launch {
      // 1. Save user message to offline Room DB
      val userMsg = ChatMessageEntity(
        sender = "user",
        message = trimmed,
        roleName = role.name,
        modelName = model
      )
      repository.insertChatMessage(userMsg)

      // 2. Fetch conversation history
      val history = chatMessages.value

      // 3. Request multi-turn response from Gemini via Firebase AI Logic
      val result = chatEngine.sendMessage(
        history = history,
        userMessage = trimmed,
        roleId = role.id,
        requestedModel = model
      )

      result.onSuccess { responseText ->
        val modelMsg = ChatMessageEntity(
          sender = "model",
          message = responseText,
          roleName = role.name,
          modelName = model
        )
        repository.insertChatMessage(modelMsg)
        _isChatSending.value = false
      }.onFailure { err ->
        val errorText = "[TRANSMISSION ANOMALY: ${err.message ?: "Failed to contact Hunter System"}]\n\nVerify App Check attestation and internet connectivity, then retry."
        val modelErrorMsg = ChatMessageEntity(
          sender = "model",
          message = errorText,
          roleName = role.name,
          modelName = model,
          isError = true
        )
        repository.insertChatMessage(modelErrorMsg)
        _chatError.value = err.message
        _isChatSending.value = false
      }
    }
  }

  fun clearChatHistory() {
    viewModelScope.launch {
      repository.clearChatHistory()
    }
  }
}

