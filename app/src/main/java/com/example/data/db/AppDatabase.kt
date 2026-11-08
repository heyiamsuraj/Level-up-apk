package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    UserEntity::class,
    PlayerEntity::class,
    QuestEntity::class,
    AttributeEntity::class,
    ChatMessageEntity::class,
    GateSubjectEntity::class,
    CollegeTimetableEntity::class,
    ProjectMilestoneEntity::class,
    AssignmentEntity::class,
    WorkoutLogEntity::class,
    WeightLogEntity::class,
    NutritionLogEntity::class,
    WaterLogEntity::class,
    HabitEntity::class,
    AchievementEntity::class,
    FocusSessionEntity::class,
    QuoteEntity::class,
    DashboardConfigEntity::class
  ],
  version = 2,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun systemDao(): SystemDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "levelup_system_database"
        )
          .fallbackToDestructiveMigration()
          .addCallback(DatabaseCallback())
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback : Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          CoroutineScope(Dispatchers.IO).launch {
            val dao = database.systemDao()
            dao.insertOrUpdatePlayer(PrepopulateData.defaultPlayer)
            dao.insertOrUpdateUser(PrepopulateData.defaultUser)
            dao.insertAttributes(PrepopulateData.defaultAttributes)
            dao.insertChatMessage(PrepopulateData.initialChatMessage)
            dao.insertOrUpdateDashboardConfig(PrepopulateData.defaultDashboardConfig)
            val today = PrepopulateData.todayDateStr()
            dao.insertQuests(PrepopulateData.getDefaultQuests(today))
            dao.insertGateSubjects(PrepopulateData.gateSubjects)
            dao.insertTimetableEntries(PrepopulateData.defaultTimetable)
            dao.insertProjectMilestones(PrepopulateData.defaultProjectMilestones)
            for (assignment in PrepopulateData.defaultAssignments) {
              dao.insertAssignment(assignment)
            }
            dao.insertHabits(PrepopulateData.defaultHabits)
            dao.insertAchievements(PrepopulateData.defaultAchievements)
            dao.insertQuotes(PrepopulateData.motivationalQuotes)
          }
        }
      }
    }
  }
}
