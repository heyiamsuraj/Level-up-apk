package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

@Dao
interface SystemDao {

  // --- Player ---
  @Query("SELECT * FROM player WHERE id = 1")
  fun getPlayer(): Flow<PlayerEntity?>

  @Query("SELECT * FROM player WHERE id = 1")
  suspend fun getPlayerSync(): PlayerEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdatePlayer(player: PlayerEntity)

  // --- Quests ---
  @Query("SELECT * FROM quests ORDER BY id DESC")
  fun getAllQuests(): Flow<List<QuestEntity>>

  @Query("SELECT * FROM quests WHERE dateStr = :dateStr ORDER BY isMainQuest DESC, priority ASC, id ASC")
  fun getQuestsForDate(dateStr: String): Flow<List<QuestEntity>>

  @Query("SELECT * FROM quests WHERE id = :id")
  suspend fun getQuestById(id: Long): QuestEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuest(quest: QuestEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuests(quests: List<QuestEntity>)

  @Update
  suspend fun updateQuest(quest: QuestEntity)

  @Query("DELETE FROM quests WHERE id = :id")
  suspend fun deleteQuest(id: Long)

  // --- GATE Subjects ---
  @Query("SELECT * FROM gate_subjects ORDER BY id ASC")
  fun getAllGateSubjects(): Flow<List<GateSubjectEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGateSubject(subject: GateSubjectEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGateSubjects(subjects: List<GateSubjectEntity>)

  @Update
  suspend fun updateGateSubject(subject: GateSubjectEntity)

  // --- College Timetable & Projects ---
  @Query("SELECT * FROM college_timetable ORDER BY dayOfWeek, timeSlot ASC")
  fun getAllTimetable(): Flow<List<CollegeTimetableEntity>>

  @Query("SELECT * FROM college_timetable WHERE dayOfWeek = :dayOfWeek ORDER BY timeSlot ASC")
  fun getTimetableForDay(dayOfWeek: String): Flow<List<CollegeTimetableEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTimetableEntries(entries: List<CollegeTimetableEntity>)

  @Update
  suspend fun updateTimetableEntry(entry: CollegeTimetableEntity)

  @Query("SELECT * FROM project_milestones ORDER BY orderIndex ASC")
  fun getAllProjectMilestones(): Flow<List<ProjectMilestoneEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProjectMilestones(milestones: List<ProjectMilestoneEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProjectMilestone(milestone: ProjectMilestoneEntity)

  @Update
  suspend fun updateProjectMilestone(milestone: ProjectMilestoneEntity)

  @Query("SELECT * FROM academic_assignments ORDER BY isSubmitted ASC, deadlineDate ASC")
  fun getAllAssignments(): Flow<List<AssignmentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAssignment(assignment: AssignmentEntity)

  @Update
  suspend fun updateAssignment(assignment: AssignmentEntity)

  // --- Fitness, Weight, Water & Nutrition ---
  @Query("SELECT * FROM workout_logs ORDER BY id DESC")
  fun getAllWorkouts(): Flow<List<WorkoutLogEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertWorkout(workout: WorkoutLogEntity)

  @Query("SELECT * FROM weight_logs ORDER BY id DESC")
  fun getAllWeightLogs(): Flow<List<WeightLogEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertWeightLog(weightLog: WeightLogEntity)

  @Query("SELECT * FROM nutrition_logs WHERE dateStr = :dateStr ORDER BY id ASC")
  fun getNutritionForDate(dateStr: String): Flow<List<NutritionLogEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNutrition(item: NutritionLogEntity)

  @Query("DELETE FROM nutrition_logs WHERE id = :id")
  suspend fun deleteNutrition(id: Long)

  @Query("SELECT * FROM water_logs WHERE dateStr = :dateStr")
  fun getWaterLogsForDate(dateStr: String): Flow<List<WaterLogEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertWaterLog(waterLog: WaterLogEntity)

  // --- Habits ---
  @Query("SELECT * FROM habits ORDER BY id ASC")
  fun getAllHabits(): Flow<List<HabitEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertHabit(habit: HabitEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertHabits(habits: List<HabitEntity>)

  @Update
  suspend fun updateHabit(habit: HabitEntity)

  // --- Achievements ---
  @Query("SELECT * FROM achievements ORDER BY isUnlocked DESC, rankRequirement ASC")
  fun getAllAchievements(): Flow<List<AchievementEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAchievements(achievements: List<AchievementEntity>)

  @Update
  suspend fun updateAchievement(achievement: AchievementEntity)

  // --- Focus Sessions ---
  @Query("SELECT * FROM focus_sessions ORDER BY id DESC")
  fun getAllFocusSessions(): Flow<List<FocusSessionEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFocusSession(session: FocusSessionEntity)

  // --- Quotes ---
  @Query("SELECT * FROM motivational_quotes ORDER BY isFavorite DESC, id ASC")
  fun getAllQuotes(): Flow<List<QuoteEntity>>

  @Query("SELECT * FROM motivational_quotes WHERE category = :category")
  fun getQuotesByCategory(category: String): Flow<List<QuoteEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuotes(quotes: List<QuoteEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuote(quote: QuoteEntity)

  @Update
  suspend fun updateQuote(quote: QuoteEntity)

  @Query("DELETE FROM motivational_quotes WHERE id = :id")
  suspend fun deleteQuote(id: Long)

  // --- Dashboard Config ---
  @Query("SELECT * FROM dashboard_config WHERE id = 1")
  fun getDashboardConfig(): Flow<DashboardConfigEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateDashboardConfig(config: DashboardConfigEntity)

  // --- Users ---
  @Query("SELECT * FROM users WHERE id = 1")
  fun getUser(): Flow<UserEntity?>

  @Query("SELECT * FROM users WHERE id = 1")
  suspend fun getUserSync(): UserEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateUser(user: UserEntity)

  // --- RPG Attributes ---
  @Query("SELECT * FROM attributes ORDER BY id ASC")
  fun getAllAttributes(): Flow<List<AttributeEntity>>

  @Query("SELECT * FROM attributes WHERE code = :code LIMIT 1")
  suspend fun getAttributeByCode(code: String): AttributeEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAttributes(attributes: List<AttributeEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateAttribute(attribute: AttributeEntity)

  @Query("UPDATE attributes SET baseValue = baseValue + :amount, lastTrainedTimestamp = :timestamp WHERE code = :code")
  suspend fun incrementAttribute(code: String, amount: Int, timestamp: Long = System.currentTimeMillis())

  // --- Gemini Chatbot Messages ---
  @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
  fun getAllChatMessages(): Flow<List<ChatMessageEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChatMessage(msg: ChatMessageEntity): Long

  @Query("DELETE FROM chat_messages")
  suspend fun clearChatMessages()
}
