package com.example.ui.screens.study

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AssignmentEntity
import com.example.data.model.CollegeTimetableEntity
import com.example.data.model.GateSubjectEntity
import com.example.data.model.ProjectMilestoneEntity
import com.example.ui.components.GlowButton
import com.example.ui.components.SystemHologramCard
import com.example.ui.components.SystemProgressBar
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HologramCardBg
import com.example.ui.theme.HologramCardBorder
import com.example.ui.theme.MonarchViolet
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.QuestCompletedColor
import com.example.ui.theme.QuestWarningRed
import com.example.ui.theme.RankS_Color
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VoidBlack

@Composable
fun StudyHubScreen(
  gateSubjects: List<GateSubjectEntity>,
  timetable: List<CollegeTimetableEntity>,
  projectMilestones: List<ProjectMilestoneEntity>,
  assignments: List<AssignmentEntity>,
  aiRecommendationState: com.example.data.ai.AiRecommendationState,
  onRefreshAiRecommendations: () -> Unit,
  onAcceptRecommendedTopic: (com.example.data.ai.GateTopicRecommendation) -> Unit,
  onLogGateStudy: (subjectId: Int, minutes: Int, pyqs: Int, topicCompleted: Boolean) -> Unit,
  onRecordAttendance: (classId: Long, attended: Boolean) -> Unit,
  onToggleProjectMilestone: (ProjectMilestoneEntity) -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var studyLogSubject by remember { mutableStateOf<GateSubjectEntity?>(null) }

  val tabs = listOf("GATE ME TRACKER", "COLLEGE TIMETABLE", "B.E. PROJECT")

  Box(modifier = Modifier.fillMaxSize().background(VoidBlack).testTag("study_hub_screen")) {
    Column(modifier = Modifier.fillMaxSize()) {
      Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
          text = "[ INTELLECT & ACADEMIC HUB ]",
          style = MaterialTheme.typography.labelSmall,
          color = ElectricCyan,
          letterSpacing = 2.sp
        )
        Text(
          text = "ENGINEERING MASTERY",
          style = MaterialTheme.typography.displaySmall,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
      }

      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = HologramCardBg,
        contentColor = ElectricCyan,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
            color = ElectricCyan
          )
        }
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                color = if (selectedTab == index) ElectricCyan else TextSecondary
              )
            }
          )
        }
      }

      when (selectedTab) {
        0 -> GateTrackerContent(
          subjects = gateSubjects,
          aiRecommendationState = aiRecommendationState,
          onRefreshAiRecommendations = onRefreshAiRecommendations,
          onAcceptRecommendedTopic = onAcceptRecommendedTopic,
          onOpenLogDialog = { studyLogSubject = it }
        )
        1 -> CollegeTimetableContent(
          timetable = timetable,
          onRecordAttendance = onRecordAttendance
        )
        2 -> ProjectMilestonesContent(
          milestones = projectMilestones,
          assignments = assignments,
          onToggleMilestone = onToggleProjectMilestone
        )
      }
    }

    if (studyLogSubject != null) {
      LogStudyDialog(
        subject = studyLogSubject!!,
        onDismiss = { studyLogSubject = null },
        onSave = { minutes, pyqs, topicDone ->
          onLogGateStudy(studyLogSubject!!.id, minutes, pyqs, topicDone)
          studyLogSubject = null
        }
      )
    }
  }
}

@Composable
fun GateTrackerContent(
  subjects: List<GateSubjectEntity>,
  aiRecommendationState: com.example.data.ai.AiRecommendationState,
  onRefreshAiRecommendations: () -> Unit,
  onAcceptRecommendedTopic: (com.example.data.ai.GateTopicRecommendation) -> Unit,
  onOpenLogDialog: (GateSubjectEntity) -> Unit
) {
  val totalTopics = subjects.sumOf { it.totalTopics }
  val completedTopics = subjects.sumOf { it.completedTopics }
  val totalPyqs = subjects.sumOf { it.pyqsSolved }
  val overallProgress = if (totalTopics > 0) completedTopics.toFloat() / totalTopics.toFloat() else 0f

  // Auto-trigger AI recommendation analysis on initial view
  androidx.compose.runtime.LaunchedEffect(subjects.isNotEmpty()) {
    if (aiRecommendationState is com.example.data.ai.AiRecommendationState.Idle && subjects.isNotEmpty()) {
      onRefreshAiRecommendations()
    }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))
      // PROMINENT AI RECOMMENDATION DIRECTIVE CARD
      AiStudyRecommendationCard(
        state = aiRecommendationState,
        onRefresh = onRefreshAiRecommendations,
        onAccept = onAcceptRecommendedTopic
      )
    }

    item {
      // Overall Overview Card
      SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "GATE ME OVERALL SYLLABUS",
              style = MaterialTheme.typography.labelMedium,
              color = ElectricCyan
            )
            Text(
              text = "${(overallProgress * 100).toInt()}% COMPLETED",
              style = MaterialTheme.typography.labelMedium,
              color = ElectricCyan,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(6.dp))
          SystemProgressBar(progress = overallProgress, height = 10.dp)
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Topics: $completedTopics / $totalTopics", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            Text(text = "PYQs Solved: $totalPyqs", style = MaterialTheme.typography.bodySmall, color = RankS_Color, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    items(subjects, key = { it.id }) { subject ->
      GateSubjectItemCard(
        subject = subject,
        onLogStudy = { onOpenLogDialog(subject) }
      )
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
fun GateSubjectItemCard(
  subject: GateSubjectEntity,
  onLogStudy: () -> Unit
) {
  SystemHologramCard(
    borderColor = if (subject.isWeakArea) QuestWarningRed.copy(alpha = 0.7f) else HologramCardBorder,
    modifier = Modifier.fillMaxWidth()
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CutCornerShape(3.dp),
            color = ElectricCyan.copy(alpha = 0.15f),
            border = BorderStroke(0.8.dp, ElectricCyan)
          ) {
            Text(
              text = subject.code,
              style = MaterialTheme.typography.labelSmall,
              fontSize = 9.sp,
              color = ElectricCyan,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Text(
            text = subject.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        }

        if (subject.isWeakArea) {
          Surface(
            shape = CutCornerShape(2.dp),
            color = QuestWarningRed.copy(alpha = 0.2f),
            border = BorderStroke(0.6.dp, QuestWarningRed)
          ) {
            Text(
              text = "PRIORITY REVISION",
              style = MaterialTheme.typography.labelSmall,
              fontSize = 8.sp,
              color = QuestWarningRed,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = subject.notes,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Syllabus: ${subject.completedTopics}/${subject.totalTopics} Topics",
          style = MaterialTheme.typography.bodySmall,
          color = TextMuted
        )
        Text(
          text = "PYQs: ${subject.pyqsSolved} / ${subject.pyqTarget}",
          style = MaterialTheme.typography.bodySmall,
          color = RankS_Color
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      SystemProgressBar(
        progress = if (subject.totalTopics > 0) subject.completedTopics.toFloat() / subject.totalTopics.toFloat() else 0f,
        color = if (subject.isWeakArea) QuestWarningRed else ElectricCyan,
        height = 6.dp
      )

      Spacer(modifier = Modifier.height(10.dp))

      GlowButton(
        text = "+ LOG STUDY & PYQS",
        onClick = onLogStudy,
        modifier = Modifier.fillMaxWidth(),
        isPrimary = false,
        testTag = "log_study_${subject.code}"
      )
    }
  }
}

@Composable
fun CollegeTimetableContent(
  timetable: List<CollegeTimetableEntity>,
  onRecordAttendance: (classId: Long, attended: Boolean) -> Unit
) {
  var selectedDay by remember { mutableStateOf("Monday") }
  val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
  val dayEntries = timetable.filter { it.dayOfWeek.equals(selectedDay, ignoreCase = true) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(days) { day ->
          val isSelected = selectedDay == day
          Surface(
            shape = CutCornerShape(4.dp),
            color = if (isSelected) ElectricCyan.copy(alpha = 0.25f) else HologramCardBg,
            border = BorderStroke(1.dp, if (isSelected) ElectricCyan else HologramCardBorder),
            modifier = Modifier.clickable { selectedDay = day }
          ) {
            Text(
              text = day.uppercase(),
              style = MaterialTheme.typography.labelSmall,
              fontSize = 10.sp,
              color = if (isSelected) ElectricCyan else TextSecondary,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
          }
        }
      }
    }

    if (dayEntries.isEmpty()) {
      item {
        SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "No classes scheduled for $selectedDay. Enjoy your dedicated GATE preparation block!",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
          )
        }
      }
    } else {
      items(dayEntries, key = { it.id }) { item ->
        val pct = item.attendancePercentage
        val isLowAttendance = pct < 75f

        SystemHologramCard(
          borderColor = if (isLowAttendance) QuestWarningRed else HologramCardBorder,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = item.timeSlot,
                style = MaterialTheme.typography.labelSmall,
                color = ElectricCyan
              )
              Text(
                text = item.subject,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = item.room,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Attendance: ${"%.1f".format(pct)}% (${item.attendedCount}/${item.totalClassesCount})",
                style = MaterialTheme.typography.labelSmall,
                color = if (isLowAttendance) QuestWarningRed else QuestCompletedColor,
                fontWeight = FontWeight.Bold
              )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              IconButton(
                onClick = { onRecordAttendance(item.id, true) },
                modifier = Modifier
                  .size(36.dp)
                  .background(QuestCompletedColor.copy(alpha = 0.2f), CutCornerShape(4.dp))
                  .border(1.dp, QuestCompletedColor, CutCornerShape(4.dp))
              ) {
                Icon(Icons.Default.Check, contentDescription = "Attended", tint = QuestCompletedColor)
              }

              IconButton(
                onClick = { onRecordAttendance(item.id, false) },
                modifier = Modifier
                  .size(36.dp)
                  .background(QuestWarningRed.copy(alpha = 0.2f), CutCornerShape(4.dp))
                  .border(1.dp, QuestWarningRed, CutCornerShape(4.dp))
              ) {
                Icon(Icons.Default.Close, contentDescription = "Missed", tint = QuestWarningRed)
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
fun ProjectMilestonesContent(
  milestones: List<ProjectMilestoneEntity>,
  assignments: List<AssignmentEntity>,
  onToggleMilestone: (ProjectMilestoneEntity) -> Unit
) {
  val completedCount = milestones.count { it.isCompleted }
  val progress = if (milestones.isNotEmpty()) completedCount.toFloat() / milestones.size.toFloat() else 0f

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))
      SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
        Column {
          Text(
            text = "FINAL-YEAR B.E. MECHANICAL PROJECT",
            style = MaterialTheme.typography.labelMedium,
            color = ElectricCyan
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "STAGES COMPLETED: $completedCount / ${milestones.size}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Spacer(modifier = Modifier.height(6.dp))
          SystemProgressBar(progress = progress, height = 8.dp)
        }
      }
    }

    item {
      Text(
        text = "PROJECT ROADMAP STAGES",
        style = MaterialTheme.typography.labelLarge,
        color = ElectricCyan
      )
    }

    items(milestones, key = { it.id }) { milestone ->
      SystemHologramCard(
        borderColor = if (milestone.isCompleted) QuestCompletedColor else HologramCardBorder,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Checkbox(
            checked = milestone.isCompleted,
            onCheckedChange = { onToggleMilestone(milestone) },
            colors = CheckboxDefaults.colors(
              checkedColor = QuestCompletedColor,
              uncheckedColor = TextMuted
            )
          )

          Spacer(modifier = Modifier.width(8.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = milestone.stage.uppercase(),
              style = MaterialTheme.typography.labelSmall,
              color = if (milestone.isCompleted) QuestCompletedColor else MonarchViolet,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = milestone.title,
              style = MaterialTheme.typography.bodyMedium,
              color = if (milestone.isCompleted) TextMuted else TextPrimary,
              fontWeight = FontWeight.SemiBold
            )
            if (milestone.notes.isNotBlank()) {
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = milestone.notes,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
              )
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
fun LogStudyDialog(
  subject: GateSubjectEntity,
  onDismiss: () -> Unit,
  onSave: (minutes: Int, pyqs: Int, topicDone: Boolean) -> Unit
) {
  var minutesStr by remember { mutableStateOf("60") }
  var pyqsStr by remember { mutableStateOf("15") }
  var topicDone by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = CutCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = VoidBlack),
      border = BorderStroke(1.2.dp, ElectricCyan),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("log_study_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "LOG STUDY SESSION",
          style = MaterialTheme.typography.titleMedium,
          color = ElectricCyan,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = subject.name,
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = minutesStr,
          onValueChange = { minutesStr = it },
          label = { Text("Study Duration (Minutes)") },
          modifier = Modifier.fillMaxWidth().testTag("study_minutes_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ElectricCyan,
            unfocusedBorderColor = HologramCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = pyqsStr,
          onValueChange = { pyqsStr = it },
          label = { Text("PYQs Solved") },
          modifier = Modifier.fillMaxWidth().testTag("study_pyqs_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ElectricCyan,
            unfocusedBorderColor = HologramCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Checkbox(
            checked = topicDone,
            onCheckedChange = { topicDone = it },
            colors = CheckboxDefaults.colors(checkedColor = ElectricCyan)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Mark one syllabus topic completed", color = TextPrimary)
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) {
            Text("CANCEL", color = TextSecondary)
          }
          Spacer(modifier = Modifier.width(8.dp))
          GlowButton(
            text = "VERIFY & SAVE",
            onClick = {
              onSave(
                minutesStr.toIntOrNull() ?: 60,
                pyqsStr.toIntOrNull() ?: 15,
                topicDone
              )
            },
            testTag = "submit_log_study_button"
          )
        }
      }
    }
  }
}

@Composable
fun AiStudyRecommendationCard(
  state: com.example.data.ai.AiRecommendationState,
  onRefresh: () -> Unit,
  onAccept: (com.example.data.ai.GateTopicRecommendation) -> Unit
) {
  SystemHologramCard(
    borderColor = ElectricCyan,
    modifier = Modifier
      .fillMaxWidth()
      .testTag("ai_study_recommendation_card")
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = ElectricCyan,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "SYSTEM AI DIRECTIVE",
              style = MaterialTheme.typography.labelSmall,
              color = ElectricCyan,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.5.sp
            )
          }
          Text(
            text = "PRIORITIZED TOPICS FOR IMMEDIATE STUDY",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        }

        IconButton(
          onClick = onRefresh,
          modifier = Modifier
            .size(34.dp)
            .background(HologramCardBg, CutCornerShape(4.dp))
            .border(1.dp, HologramCardBorder, CutCornerShape(4.dp))
            .testTag("refresh_ai_recommendations_button")
        ) {
          Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.Schedule,
            contentDescription = "Refresh",
            tint = ElectricCyan,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Dynamically calculated from syllabus weightage, PYQ completion rate, and identified weak areas.",
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary
      )

      Spacer(modifier = Modifier.height(12.dp))

      when (state) {
        is com.example.data.ai.AiRecommendationState.Loading -> {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            androidx.compose.material3.CircularProgressIndicator(
              modifier = Modifier.size(28.dp),
              color = ElectricCyan,
              strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "ANALYZING GATE MECHANICAL SYLLABUS & WEAKNESS MATRIX...",
              style = MaterialTheme.typography.labelSmall,
              color = ElectricCyan,
              letterSpacing = 1.sp
            )
          }
        }

        is com.example.data.ai.AiRecommendationState.Success -> {
          val recs = state.recommendations
          recs.forEach { rec ->
            SingleTopicRecommendationCard(rec = rec, onAccept = { onAccept(rec) })
            Spacer(modifier = Modifier.height(8.dp))
          }
        }

        is com.example.data.ai.AiRecommendationState.Error -> {
          Text(
            text = "Offline Mode: Using cached syllabus priority algorithms.",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
          )
          Spacer(modifier = Modifier.height(8.dp))
          state.fallbackRecommendations.forEach { rec ->
            SingleTopicRecommendationCard(rec = rec, onAccept = { onAccept(rec) })
            Spacer(modifier = Modifier.height(8.dp))
          }
        }

        else -> {
          Text(
            text = "Tap refresh to run the AI study recommendation analysis.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
        }
      }
    }
  }
}

@Composable
fun SingleTopicRecommendationCard(
  rec: com.example.data.ai.GateTopicRecommendation,
  onAccept: () -> Unit
) {
  var isAccepted by remember { mutableStateOf(false) }
  val isCritical = rec.priorityRank.contains("CRITICAL")

  Surface(
    shape = CutCornerShape(6.dp),
    color = HologramCardBg,
    border = BorderStroke(1.dp, if (isCritical) QuestWarningRed.copy(alpha = 0.8f) else HologramCardBorder),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CutCornerShape(3.dp),
            color = if (isCritical) QuestWarningRed.copy(alpha = 0.25f) else ElectricCyan.copy(alpha = 0.2f),
            border = BorderStroke(0.8.dp, if (isCritical) QuestWarningRed else ElectricCyan)
          ) {
            Text(
              text = rec.priorityRank,
              style = MaterialTheme.typography.labelSmall,
              fontSize = 8.sp,
              color = if (isCritical) QuestWarningRed else ElectricCyan,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          Surface(
            shape = CutCornerShape(2.dp),
            color = HologramCardBg,
            border = BorderStroke(0.6.dp, TextMuted)
          ) {
            Text(
              text = rec.difficulty,
              style = MaterialTheme.typography.labelSmall,
              fontSize = 8.sp,
              color = RankS_Color,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }
        }

        Text(
          text = "${rec.estimatedStudyMinutes} MINS",
          style = MaterialTheme.typography.labelSmall,
          color = RankS_Color,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "${rec.topicName} • ${rec.subjectCode}",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = rec.rationale,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary
      )

      if (rec.suggestedActions.isNotEmpty()) {
        Spacer(modifier = Modifier.height(6.dp))
        rec.suggestedActions.forEach { action ->
          Text(
            text = "▸ $action",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted,
            fontSize = 11.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (isAccepted) {
        Surface(
          shape = CutCornerShape(3.dp),
          color = QuestCompletedColor.copy(alpha = 0.2f),
          border = BorderStroke(0.8.dp, QuestCompletedColor)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = androidx.compose.material.icons.Icons.Default.Check,
              contentDescription = null,
              tint = QuestCompletedColor,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "QUEST FORGED & ADDED TO TODAY'S LOG",
              style = MaterialTheme.typography.labelSmall,
              color = QuestCompletedColor,
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp
            )
          }
        }
      } else {
        GlowButton(
          text = "+ ACCEPT QUEST & START STUDY",
          onClick = {
            onAccept()
            isAccepted = true
          },
          modifier = Modifier.fillMaxWidth(),
          testTag = "accept_study_recommendation_${rec.subjectCode}"
        )
      }
    }
  }
}

