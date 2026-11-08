package com.example.data.repository

import com.example.data.db.PrepopulateData
import com.example.data.db.SystemDao
import com.example.data.model.AchievementEntity
import com.example.data.model.AssignmentEntity
import com.example.data.model.AttributeEntity
import com.example.data.model.ChatMessageEntity
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
import com.example.data.model.UserEntity
import com.example.data.model.WaterLogEntity
import com.example.data.model.WeightLogEntity
import com.example.data.model.WorkoutLogEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

data class LevelUpResult(
  val didLevelUp: Boolean,
  val oldLevel: Int,
  val newLevel: Int,
  val newRank: String,
  val gainedXp: Int
)

class SystemRepository(private val dao: SystemDao) {

  val playerFlow: Flow<PlayerEntity?> = dao.getPlayer()
  val userFlow: Flow<UserEntity?> = dao.getUser()
  val attributesFlow: Flow<List<AttributeEntity>> = dao.getAllAttributes()
  val chatMessagesFlow: Flow<List<ChatMessageEntity>> = dao.getAllChatMessages()
  val dashboardConfigFlow: Flow<DashboardConfigEntity?> = dao.getDashboardConfig()
  val gateSubjectsFlow: Flow<List<GateSubjectEntity>> = dao.getAllGateSubjects()
  val timetableFlow: Flow<List<CollegeTimetableEntity>> = dao.getAllTimetable()
  val projectMilestonesFlow: Flow<List<ProjectMilestoneEntity>> = dao.getAllProjectMilestones()
  val assignmentsFlow: Flow<List<AssignmentEntity>> = dao.getAllAssignments()
  val habitsFlow: Flow<List<HabitEntity>> = dao.getAllHabits()
  val achievementsFlow: Flow<List<AchievementEntity>> = dao.getAllAchievements()
  val focusSessionsFlow: Flow<List<FocusSessionEntity>> = dao.getAllFocusSessions()
  val workoutLogsFlow: Flow<List<WorkoutLogEntity>> = dao.getAllWorkouts()
  val weightLogsFlow: Flow<List<WeightLogEntity>> = dao.getAllWeightLogs()
  val allQuotesFlow: Flow<List<QuoteEntity>> = dao.getAllQuotes()

  fun getQuestsForDate(dateStr: String): Flow<List<QuestEntity>> = dao.getQuestsForDate(dateStr)

  fun getNutritionForDate(dateStr: String): Flow<List<NutritionLogEntity>> = dao.getNutritionForDate(dateStr)

  fun getWaterLogsForDate(dateStr: String): Flow<List<WaterLogEntity>> = dao.getWaterLogsForDate(dateStr)

  // Ensure default player, user, attributes, and today's quests exist
  suspend fun ensureInitialized() {
    val existingUser = dao.getUserSync()
    if (existingUser == null) {
      dao.insertOrUpdateUser(PrepopulateData.defaultUser)
      dao.insertAttributes(PrepopulateData.defaultAttributes)
      dao.insertChatMessage(PrepopulateData.initialChatMessage)
    }

    val existingPlayer = dao.getPlayerSync()
    if (existingPlayer == null) {
      dao.insertOrUpdatePlayer(PrepopulateData.defaultPlayer)
      dao.insertOrUpdateDashboardConfig(PrepopulateData.defaultDashboardConfig)
      val today = PrepopulateData.todayDateStr()
      dao.insertQuests(PrepopulateData.getDefaultQuests(today))
      dao.insertGateSubjects(PrepopulateData.gateSubjects)
      dao.insertTimetableEntries(PrepopulateData.defaultTimetable)
      dao.insertProjectMilestones(PrepopulateData.defaultProjectMilestones)
      for (a in PrepopulateData.defaultAssignments) dao.insertAssignment(a)
      dao.insertHabits(PrepopulateData.defaultHabits)
      dao.insertAchievements(PrepopulateData.defaultAchievements)
      dao.insertQuotes(PrepopulateData.motivationalQuotes)
    } else {
      // Check if today has quests
      val today = PrepopulateData.todayDateStr()
      val quests = dao.getQuestsForDate(today).firstOrNull()
      if (quests.isNullOrEmpty()) {
        dao.insertQuests(PrepopulateData.getDefaultQuests(today))
      }
    }
  }

  suspend fun updatePlayer(player: PlayerEntity) {
    dao.insertOrUpdatePlayer(player)
  }

  suspend fun updateDashboardConfig(config: DashboardConfigEntity) {
    dao.insertOrUpdateDashboardConfig(config)
  }

  // --- Complete Quest Logic with Level-Up & Attribute Processing ---
  suspend fun completeQuest(questId: Long): LevelUpResult {
    val quest = dao.getQuestById(questId) ?: return LevelUpResult(false, 1, 1, "E-Rank", 0)
    if (quest.status == "Completed") {
      return LevelUpResult(false, 1, 1, "E-Rank", 0)
    }

    val updatedQuest = quest.copy(
      status = "Completed",
      completedTimestamp = System.currentTimeMillis()
    )
    dao.updateQuest(updatedQuest)

    return awardProgress(
      xp = quest.xpReward,
      str = quest.strReward,
      intVal = quest.intReward,
      vit = quest.vitReward,
      dis = quest.disReward,
      endVal = quest.endReward,
      foc = quest.focReward
    )
  }

  suspend fun updateQuestStatus(quest: QuestEntity, newStatus: String) {
    dao.updateQuest(quest.copy(status = newStatus))
  }

  suspend fun insertQuest(quest: QuestEntity): Long = dao.insertQuest(quest)

  suspend fun deleteQuest(id: Long) = dao.deleteQuest(id)

  suspend fun awardProgress(
    xp: Int,
    str: Int = 0,
    intVal: Int = 0,
    vit: Int = 0,
    dis: Int = 0,
    endVal: Int = 0,
    foc: Int = 0
  ): LevelUpResult {
    val player = dao.getPlayerSync() ?: PrepopulateData.defaultPlayer
    val oldLevel = player.level
    var currentLevel = player.level
    var currentXp = player.currentXp + xp
    var didLevelUp = false

    while (currentXp >= (currentLevel * 200 + 100)) {
      currentXp -= (currentLevel * 200 + 100)
      currentLevel += 1
      didLevelUp = true
    }

    val newRank = PlayerEntity.calculateRank(currentLevel)
    val updatedPlayer = player.copy(
      level = currentLevel,
      currentXp = currentXp,
      rank = newRank,
      str = player.str + str,
      intVal = player.intVal + intVal,
      vit = player.vit + vit,
      dis = player.dis + dis,
      endVal = player.endVal + endVal,
      foc = player.foc + foc
    )
    dao.insertOrUpdatePlayer(updatedPlayer)

    // Sync UserEntity and AttributeEntity in Room
    val currentUser = dao.getUserSync()
    if (currentUser != null) {
      dao.insertOrUpdateUser(
        currentUser.copy(
          level = currentLevel,
          currentXp = currentXp,
          rank = newRank,
          lastActiveAt = System.currentTimeMillis()
        )
      )
    }
    if (str > 0) dao.incrementAttribute("STR", str)
    if (intVal > 0) dao.incrementAttribute("INT", intVal)
    if (vit > 0) dao.incrementAttribute("VIT", vit)
    if (dis > 0) dao.incrementAttribute("DIS", dis)
    if (endVal > 0) dao.incrementAttribute("END", endVal)
    if (foc > 0) dao.incrementAttribute("FOC", foc)

    return LevelUpResult(
      didLevelUp = didLevelUp,
      oldLevel = oldLevel,
      newLevel = currentLevel,
      newRank = newRank,
      gainedXp = xp
    )
  }

  suspend fun updateUser(user: UserEntity) {
    dao.insertOrUpdateUser(user)
  }

  suspend fun insertChatMessage(message: ChatMessageEntity): Long {
    return dao.insertChatMessage(message)
  }

  suspend fun clearChatHistory() {
    dao.clearChatMessages()
    dao.insertChatMessage(PrepopulateData.initialChatMessage)
  }

  // --- Focus Dungeon Completion ---
  suspend fun completeFocusSession(
    mode: String,
    durationMinutes: Int,
    questTitle: String
  ): LevelUpResult {
    val xp = durationMinutes * 2
    val foc = (durationMinutes / 10).coerceAtLeast(1)
    val intVal = if (mode.contains("GATE") || mode.contains("Study")) (durationMinutes / 10).coerceAtLeast(1) else 0

    val session = FocusSessionEntity(
      dateStr = PrepopulateData.todayDateStr(),
      mode = mode,
      durationMinutes = durationMinutes,
      xpEarned = xp,
      focEarned = foc,
      intEarned = intVal,
      questTitle = questTitle
    )
    dao.insertFocusSession(session)

    return awardProgress(xp = xp, foc = foc, intVal = intVal)
  }

  // --- Habit Toggle ---
  suspend fun toggleHabit(habit: HabitEntity) {
    val newStatus = !habit.isCompletedToday
    val updatedStreak = if (newStatus) habit.currentStreak + 1 else (habit.currentStreak - 1).coerceAtLeast(0)
    val best = maxOf(updatedStreak, habit.bestStreak)
    val updatedHabit = habit.copy(
      isCompletedToday = newStatus,
      currentStreak = updatedStreak,
      bestStreak = best,
      lastCompletedDate = if (newStatus) PrepopulateData.todayDateStr() else habit.lastCompletedDate
    )
    dao.updateHabit(updatedHabit)

    if (newStatus) {
      val disGain = if (habit.attributeAffected == "DIS") 5 else 0
      val vitGain = if (habit.attributeAffected == "VIT") 5 else 0
      val strGain = if (habit.attributeAffected == "STR") 5 else 0
      val intGain = if (habit.attributeAffected == "INT") 5 else 0
      val focGain = if (habit.attributeAffected == "FOC") 5 else 0
      awardProgress(
        xp = habit.xpReward,
        str = strGain,
        intVal = intGain,
        vit = vitGain,
        dis = disGain,
        foc = focGain
      )
    }
  }

  // --- GATE Subject Management ---
  suspend fun updateGateSubject(subject: GateSubjectEntity) {
    dao.updateGateSubject(subject)
  }

  suspend fun logGateStudy(subjectId: Int, minutes: Int, pyqs: Int, topicCompleted: Boolean) {
    val all = dao.getAllGateSubjects().firstOrNull() ?: emptyList()
    val subject = all.find { it.id == subjectId } ?: return
    val updated = subject.copy(
      studyMinutesTotal = subject.studyMinutesTotal + minutes,
      pyqsSolved = subject.pyqsSolved + pyqs,
      completedTopics = if (topicCompleted) minOf(subject.completedTopics + 1, subject.totalTopics) else subject.completedTopics,
      lastRevisedDate = PrepopulateData.todayDateStr()
    )
    dao.updateGateSubject(updated)
    awardProgress(
      xp = minutes + pyqs * 3,
      intVal = (minutes / 15).coerceAtLeast(1) + pyqs,
      foc = (minutes / 20).coerceAtLeast(1)
    )
  }

  // --- College Timetable & Attendance ---
  suspend fun recordAttendance(classId: Long, attended: Boolean) {
    val all = dao.getAllTimetable().firstOrNull() ?: emptyList()
    val entry = all.find { it.id == classId } ?: return
    val updated = entry.copy(
      attendedCount = if (attended) entry.attendedCount + 1 else entry.attendedCount,
      totalClassesCount = entry.totalClassesCount + 1
    )
    dao.updateTimetableEntry(updated)
    if (attended) {
      awardProgress(xp = 25, dis = 10, intVal = 5)
    }
  }

  suspend fun toggleProjectMilestone(milestone: ProjectMilestoneEntity) {
    val updated = milestone.copy(isCompleted = !milestone.isCompleted)
    dao.updateProjectMilestone(updated)
    if (updated.isCompleted) {
      awardProgress(xp = 100, intVal = 25, dis = 20)
    }
  }

  // --- Fitness & Weight & Nutrition ---
  suspend fun logWorkout(workout: WorkoutLogEntity) {
    dao.insertWorkout(workout)
    val strReward = if (workout.category == "Gym" || workout.category == "Home Workout") 25 else 10
    val endReward = if (workout.category == "Running" || workout.category == "Walking" || workout.category == "Cycling") 25 else 15
    awardProgress(
      xp = workout.durationMinutes + 30,
      str = strReward,
      endVal = endReward,
      vit = 15
    )
  }

  suspend fun logWeight(weightKg: Float, waistCm: Float, notes: String) {
    val log = WeightLogEntity(
      dateStr = PrepopulateData.todayDateStr(),
      weightKg = weightKg,
      waistCm = waistCm,
      notes = notes
    )
    dao.insertWeightLog(log)
    val player = dao.getPlayerSync()
    if (player != null) {
      dao.insertOrUpdatePlayer(player.copy(currentWeight = weightKg))
    }
  }

  suspend fun addNutritionItem(item: NutritionLogEntity) {
    dao.insertNutrition(item)
    awardProgress(xp = 15, vit = 5)
  }

  suspend fun deleteNutritionItem(id: Long) = dao.deleteNutrition(id)

  suspend fun logWater(amountMl: Int) {
    val log = WaterLogEntity(
      dateStr = PrepopulateData.todayDateStr(),
      amountMl = amountMl
    )
    dao.insertWaterLog(log)
    awardProgress(xp = 10, vit = 5)
  }

  // --- Quotes & Custom Quotes ---
  suspend fun toggleQuoteFavorite(quote: QuoteEntity) {
    dao.updateQuote(quote.copy(isFavorite = !quote.isFavorite))
  }

  suspend fun addCustomQuote(text: String, author: String, category: String) {
    val quote = QuoteEntity(
      text = text,
      author = author.ifBlank { "Player Hunter" },
      category = category,
      isCustom = true
    )
    dao.insertQuote(quote)
  }

  suspend fun deleteQuote(id: Long) = dao.deleteQuote(id)

  // --- Adaptive Planning Mode Switch ---
  suspend fun switchPlanningMode(mode: String, targetLevel: String) {
    val player = dao.getPlayerSync() ?: return
    dao.insertOrUpdatePlayer(player.copy(planningMode = mode, targetLevel = targetLevel))
    // Generate adaptive quests based on mode
    val today = PrepopulateData.todayDateStr()
    val adaptiveQuests = generateAdaptiveQuests(mode, targetLevel, today)
    dao.insertQuests(adaptiveQuests)
  }

  private fun generateAdaptiveQuests(mode: String, targetLevel: String, dateStr: String): List<QuestEntity> {
    return when (mode) {
      "Busy College Day" -> listOf(
        QuestEntity(title = "Essential College Classes", description = "Attend core lectures and complete urgent assignment submission.", category = "College", priority = "Urgent", difficulty = "C", xpReward = 40, disReward = 20, isMainQuest = true, dateStr = dateStr),
        QuestEntity(title = "Micro GATE Revision Block (45 Mins)", description = "Quick formula flashcards + 5 high-yield numericals.", category = "GATE", priority = "High", difficulty = "D", xpReward = 40, intReward = 25, focReward = 15, isMainQuest = true, dateStr = dateStr),
        QuestEntity(title = "Hydration & Fast Campus Walk", description = "Stay hydrated and get 6,000 steps between lecture buildings.", category = "Fitness", priority = "Medium", difficulty = "E", xpReward = 30, vitReward = 15, endReward = 15, isMainQuest = false, dateStr = dateStr),
        QuestEntity(title = "Protected Sleep Window", description = "Get at least 7 hours sleep despite hostel deadlines.", category = "Health", priority = "High", difficulty = "E", xpReward = 30, vitReward = 25, isMainQuest = true, dateStr = dateStr)
      )
      "Free Day" -> listOf(
        QuestEntity(title = "Deep GATE Master Block: Fluid & Thermodynamics (3.5h)", description = "Solve 35 PYQs and clarify weak thermodynamic relations.", category = "GATE", priority = "Urgent", difficulty = "B", xpReward = 120, intReward = 80, focReward = 40, isMainQuest = true, dateStr = dateStr),
        QuestEntity(title = "Final-Year B.E. Project Simulation Sprint", description = "Run complete parametric FEA mesh convergence.", category = "Project", priority = "High", difficulty = "C", xpReward = 80, intReward = 40, disReward = 30, isMainQuest = true, dateStr = dateStr),
        QuestEntity(title = "Full Power Gym Resistance Training", description = "Heavy compound movements (Squats/Press/Rows) + core work.", category = "Fitness", priority = "High", difficulty = "C", xpReward = 70, strReward = 45, endReward = 25, isMainQuest = true, dateStr = dateStr),
        QuestEntity(title = "Macro-Nutrient Tracking: 100g Vegetarian Protein", description = "Log paneer, soya, dal, and curd consistently.", category = "Nutrition", priority = "Medium", difficulty = "D", xpReward = 45, vitReward = 30, strReward = 15, isMainQuest = false, dateStr = dateStr),
        QuestEntity(title = "Dungeon Focus Marathon (90 Mins Deep Work)", description = "Zero phone distractions study session.", category = "Habit", priority = "High", difficulty = "C", xpReward = 75, focReward = 40, intReward = 35, isMainQuest = true, dateStr = dateStr)
      )
      "Weekend" -> listOf(
        QuestEntity(title = "Full Length GATE ME Mock Test (65 Questions, 3h)", description = "Simulate real exam environment with virtual calculator.", category = "GATE", priority = "Urgent", difficulty = "A", xpReward = 150, intReward = 90, focReward = 60, isMainQuest = true, dateStr = dateStr),
        QuestEntity(title = "Mock Test Diagnostic & Weak Subject Analysis", description = "Categorize error types: Conceptual, Calculation, or Silly.", category = "GATE", priority = "High", difficulty = "C", xpReward = 65, intReward = 45, disReward = 20, isMainQuest = true, dateStr = dateStr),
        QuestEntity(title = "Active Recovery & Outdoor Walk / Jog", description = "Relax and recharge physical endurance in open air.", category = "Fitness", priority = "Medium", difficulty = "D", xpReward = 45, endReward = 30, vitReward = 20, isMainQuest = false, dateStr = dateStr),
        QuestEntity(title = "Project Documentation & Weekly Report", description = "Summarize findings for guide review meeting.", category = "Project", priority = "Medium", difficulty = "D", xpReward = 50, disReward = 25, intReward = 20, isMainQuest = true, dateStr = dateStr)
      )
      "Examination Mode" -> listOf(
        QuestEntity(title = "University Semester Exam Revision", description = "Target syllabus units for upcoming engineering theory exam.", category = "College", priority = "Urgent", difficulty = "B", xpReward = 90, intReward = 60, disReward = 30, isMainQuest = true, dateStr = dateStr),
        QuestEntity(title = "Previous 5-Year University Papers Review", description = "Solve key derivation questions and numericals.", category = "College", priority = "High", difficulty = "C", xpReward = 70, intReward = 50, focReward = 25, isMainQuest = true, dateStr = dateStr),
        QuestEntity(title = "Stress Management & Calming Walk", description = "20-minute gentle stretch or campus stroll to decompress.", category = "Fitness", priority = "Medium", difficulty = "E", xpReward = 30, vitReward = 25, isMainQuest = false, dateStr = dateStr),
        QuestEntity(title = "Adequate Sleep Before Exam Day", description = "Strict 8 hours sleep for peak cognitive recall.", category = "Health", priority = "Urgent", difficulty = "D", xpReward = 40, vitReward = 35, isMainQuest = true, dateStr = dateStr)
      )
      else -> PrepopulateData.getDefaultQuests(dateStr)
    }
  }
}
