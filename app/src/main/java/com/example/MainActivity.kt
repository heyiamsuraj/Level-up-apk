package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.HunterBottomBar
import com.example.ui.components.LevelUpCelebrationDialog
import com.example.ui.screens.chat.SystemChatScreen
import com.example.ui.screens.dashboard.DashboardScreen
import com.example.ui.screens.fitness.FitnessHubScreen
import com.example.ui.screens.focus.DungeonFocusScreen
import com.example.ui.screens.onboarding.OnboardingAwakeningScreen
import com.example.ui.screens.player.PlayerStatusScreen
import com.example.ui.screens.quests.QuestsScreen
import com.example.ui.screens.study.StudyHubScreen
import com.example.ui.theme.LevelUpSystemTheme
import com.example.ui.viewmodel.SystemViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: SystemViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    com.example.util.configureAppCheck(this, intent)
    enableEdgeToEdge()

    setContent {
      val player by viewModel.player.collectAsStateWithLifecycle()
      val dashboardConfig by viewModel.dashboardConfig.collectAsStateWithLifecycle()
      val todayQuests by viewModel.todayQuests.collectAsStateWithLifecycle()
      val gateSubjects by viewModel.gateSubjects.collectAsStateWithLifecycle()
      val timetable by viewModel.timetable.collectAsStateWithLifecycle()
      val projectMilestones by viewModel.projectMilestones.collectAsStateWithLifecycle()
      val assignments by viewModel.assignments.collectAsStateWithLifecycle()
      val habits by viewModel.habits.collectAsStateWithLifecycle()
      val achievements by viewModel.achievements.collectAsStateWithLifecycle()
      val focusSessions by viewModel.focusSessions.collectAsStateWithLifecycle()
      val workoutLogs by viewModel.workoutLogs.collectAsStateWithLifecycle()
      val weightLogs by viewModel.weightLogs.collectAsStateWithLifecycle()
      val todayNutrition by viewModel.todayNutrition.collectAsStateWithLifecycle()
      val todayWaterLogs by viewModel.todayWaterLogs.collectAsStateWithLifecycle()
      val quotes by viewModel.quotes.collectAsStateWithLifecycle()
      val levelUpEvent by viewModel.levelUpEvent.collectAsStateWithLifecycle()
      val aiRecommendationState by viewModel.aiRecommendationState.collectAsStateWithLifecycle()
      val generatedWorkoutPlan by viewModel.generatedWorkoutPlan.collectAsStateWithLifecycle()
      val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
      val isChatSending by viewModel.isChatSending.collectAsStateWithLifecycle()
      val selectedChatRole by viewModel.selectedChatRole.collectAsStateWithLifecycle()
      val selectedChatModel by viewModel.selectedChatModel.collectAsStateWithLifecycle()

      var currentScreen by remember { mutableStateOf("dashboard") }
      var isFocusModeOpen by remember { mutableStateOf(false) }

      val isAmoled = dashboardConfig.themePreset == "AMOLED"
      val accentColor = try {
        Color(android.graphics.Color.parseColor(dashboardConfig.accentColorHex))
      } catch (e: Exception) {
        null
      }

      LevelUpSystemTheme(isAmoled = isAmoled, accentColor = accentColor) {
        if (!player.isOnboarded) {
          OnboardingAwakeningScreen(
            onCompleteAwakening = { name, collegeTiming, wake, sleep, gateHours, currentWeight, targetWeight, prepLevel ->
              viewModel.completeOnboarding(
                name,
                collegeTiming,
                wake,
                sleep,
                gateHours,
                currentWeight,
                targetWeight,
                prepLevel
              )
            }
          )
        } else if (isFocusModeOpen) {
          BackHandler { isFocusModeOpen = false }
          DungeonFocusScreen(
            onCompleteSession = { mode, minutes, questTitle ->
              viewModel.completeFocusSession(mode, minutes, questTitle)
            },
            onClose = { isFocusModeOpen = false }
          )
        } else {
          BackHandler(enabled = currentScreen != "dashboard") {
            currentScreen = "dashboard"
          }

          Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
              HunterBottomBar(
                currentRoute = currentScreen,
                onNavigate = { route -> currentScreen = route }
              )
            }
          ) { innerPadding ->
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
            ) {
              when (currentScreen) {
                "dashboard" -> DashboardScreen(
                  player = player,
                  dashboardConfig = dashboardConfig,
                  todayQuests = todayQuests,
                  gateSubjects = gateSubjects,
                  quotes = quotes,
                  onCompleteQuest = { id -> viewModel.completeQuest(id) },
                  onNavigateToTab = { route -> currentScreen = route },
                  onStartFocus = { isFocusModeOpen = true },
                  onSaveDashboardConfig = { preset, wall, hex, dim, blur ->
                    viewModel.updateDashboardSettings(preset, wall, hex, dim, blur)
                  },
                  onToggleQuoteFavorite = { q -> viewModel.toggleQuoteFavorite(q) },
                  onCompleteSurvivalQuest = { viewModel.completeSurvivalQuest() }
                )

                "quests" -> QuestsScreen(
                  player = player,
                  quests = todayQuests,
                  onCompleteQuest = { id -> viewModel.completeQuest(id) },
                  onUpdateStatus = { q, s -> viewModel.updateQuestStatus(q, s) },
                  onDeleteQuest = { id -> viewModel.deleteQuest(id) },
                  onSwitchPlanningMode = { m, l -> viewModel.switchPlanningMode(m, l) },
                  onAddQuest = { title, desc, cat, prio, diff, xp, isMain, start, dead ->
                    viewModel.addQuest(title, desc, cat, prio, diff, xp, isMain, start, dead)
                  }
                )

                "study" -> StudyHubScreen(
                  gateSubjects = gateSubjects,
                  timetable = timetable,
                  projectMilestones = projectMilestones,
                  assignments = assignments,
                  aiRecommendationState = aiRecommendationState,
                  onRefreshAiRecommendations = { viewModel.refreshAiRecommendations() },
                  onAcceptRecommendedTopic = { rec -> viewModel.acceptRecommendedTopicAsQuest(rec) },
                  onLogGateStudy = { id, min, pyq, done ->
                    viewModel.logGateStudy(id, min, pyq, done)
                  },
                  onRecordAttendance = { id, att ->
                    viewModel.recordAttendance(id, att)
                  },
                  onToggleProjectMilestone = { m ->
                    viewModel.toggleProjectMilestone(m)
                  }
                )

                "fitness" -> FitnessHubScreen(
                  player = player,
                  workoutLogs = workoutLogs,
                  weightLogs = weightLogs,
                  nutritionLogs = todayNutrition,
                  waterLogs = todayWaterLogs,
                  generatedWorkoutPlan = generatedWorkoutPlan,
                  onGenerateWorkoutRoutine = { eq, dur, musc, cls ->
                    viewModel.generateWorkoutRoutine(eq, dur, musc, cls)
                  },
                  onClearGeneratedWorkout = { viewModel.clearGeneratedWorkout() },
                  onAcceptWorkoutPlan = { plan -> viewModel.acceptWorkoutPlan(plan) },
                  onLogWorkout = { cat, title, dur, det, cal, inten ->
                    viewModel.logWorkout(cat, title, dur, det, cal, inten)
                  },
                  onLogWeight = { wt, waist, notes ->
                    viewModel.logWeight(wt, waist, notes)
                  },
                  onAddFood = { name, serv, cal, p, c, f, meal ->
                    viewModel.addNutritionItem(name, serv, cal, p, c, f, meal)
                  },
                  onDeleteFood = { id -> viewModel.deleteNutritionItem(id) },
                  onLogWater = { ml -> viewModel.logWater(ml) }
                )

                "chat" -> SystemChatScreen(
                  player = player,
                  messages = chatMessages,
                  isSending = isChatSending,
                  selectedRole = selectedChatRole,
                  selectedModel = selectedChatModel,
                  onSelectRole = { role -> viewModel.selectChatRole(role) },
                  onSelectModel = { model -> viewModel.selectChatModel(model) },
                  onSendMessage = { text -> viewModel.sendChatMessage(text) },
                  onClearHistory = { viewModel.clearChatHistory() }
                )

                "player" -> PlayerStatusScreen(
                  player = player,
                  habits = habits,
                  achievements = achievements,
                  workoutLogs = workoutLogs,
                  focusSessions = focusSessions,
                  quotes = quotes,
                  dashboardConfig = dashboardConfig,
                  onSetSystemColor = { hex -> viewModel.setSystemAccentColor(hex) },
                  onToggleHabit = { h -> viewModel.toggleHabit(h) },
                  onToggleQuoteFavorite = { q -> viewModel.toggleQuoteFavorite(q) },
                  onAddCustomQuote = { txt, auth, cat -> viewModel.addCustomQuote(txt, auth, cat) },
                  onDeleteQuote = { id -> viewModel.deleteQuote(id) }
                )
              }
            }
          }
        }

        // Level Up Full-Screen Celebration Dialog
        levelUpEvent?.let { event ->
          LevelUpCelebrationDialog(
            oldLevel = event.oldLevel,
            newLevel = event.newLevel,
            newRank = event.newRank,
            gainedXp = event.gainedXp,
            onDismiss = { viewModel.dismissLevelUp() }
          )
        }
      }
    }
  }
}
