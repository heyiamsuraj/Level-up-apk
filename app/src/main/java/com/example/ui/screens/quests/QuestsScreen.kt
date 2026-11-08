package com.example.ui.screens.quests

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import com.example.data.model.PlayerEntity
import com.example.data.model.QuestEntity
import com.example.ui.components.GlowButton
import com.example.ui.components.SystemHologramCard
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HologramCardBg
import com.example.ui.theme.HologramCardBorder
import com.example.ui.theme.MonarchViolet
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.QuestCompletedColor
import com.example.ui.theme.QuestInProgressColor
import com.example.ui.theme.QuestSkippedColor
import com.example.ui.theme.RankS_Color
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VoidBlack

@Composable
fun QuestsScreen(
  player: PlayerEntity,
  quests: List<QuestEntity>,
  onCompleteQuest: (Long) -> Unit,
  onUpdateStatus: (QuestEntity, String) -> Unit,
  onDeleteQuest: (Long) -> Unit,
  onSwitchPlanningMode: (String, String) -> Unit,
  onAddQuest: (
    title: String,
    description: String,
    category: String,
    priority: String,
    difficulty: String,
    xpReward: Int,
    isMainQuest: Boolean,
    startTime: String,
    deadline: String
  ) -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var showAddDialog by remember { mutableStateOf(false) }

  val planningModes = listOf(
    "Normal College Day",
    "Busy College Day",
    "Free Day",
    "Weekend",
    "Examination Mode"
  )

  val targetLevels = listOf("Minimum Day", "Target Day", "Excellent Day")

  val filteredQuests = when (selectedTab) {
    1 -> quests.filter { it.isMainQuest }
    2 -> quests.filter { !it.isMainQuest }
    3 -> quests.filter { it.status == "Completed" }
    else -> quests
  }

  Box(modifier = Modifier.fillMaxSize().background(VoidBlack).testTag("quests_screen")) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "[ SYSTEM TASK MATRIX ]",
          style = MaterialTheme.typography.labelSmall,
          color = ElectricCyan,
          letterSpacing = 2.sp
        )
        Text(
          text = "DAILY QUEST LOG",
          style = MaterialTheme.typography.displaySmall,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
      }

      // Planning Mode Selector
      item {
        SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
          Column {
            Text(
              text = "ADAPTIVE PLANNING MODE",
              style = MaterialTheme.typography.labelMedium,
              color = ElectricCyan
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(planningModes) { mode ->
                val isSelected = player.planningMode == mode
                Surface(
                  shape = CutCornerShape(4.dp),
                  color = if (isSelected) ElectricCyan.copy(alpha = 0.25f) else HologramCardBg,
                  border = BorderStroke(1.dp, if (isSelected) ElectricCyan else HologramCardBorder),
                  modifier = Modifier
                    .clickable { onSwitchPlanningMode(mode, player.targetLevel) }
                    .padding(vertical = 4.dp)
                ) {
                  Text(
                    text = mode.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = if (isSelected) ElectricCyan else TextSecondary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Target Levels
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              targetLevels.forEach { level ->
                val isSelected = player.targetLevel == level
                Surface(
                  shape = CutCornerShape(3.dp),
                  color = if (isSelected) MonarchViolet.copy(alpha = 0.25f) else HologramCardBg,
                  border = BorderStroke(1.dp, if (isSelected) MonarchViolet else HologramCardBorder),
                  modifier = Modifier
                    .weight(1f)
                    .clickable { onSwitchPlanningMode(player.planningMode, level) }
                ) {
                  Text(
                    text = level.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = if (isSelected) MonarchViolet else TextMuted,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(vertical = 6.dp)
                  )
                }
              }
            }
          }
        }
      }

      // Filter Tabs
      item {
        val tabs = listOf("ALL (${quests.size})", "MAIN (${quests.count { it.isMainQuest }})", "SIDE (${quests.count { !it.isMainQuest }})", "CLEARED (${quests.count { it.status == "Completed" }})")
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
                  fontSize = 10.sp,
                  fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                  color = if (selectedTab == index) ElectricCyan else TextSecondary
                )
              }
            )
          }
        }
      }

      // Quests List
      if (filteredQuests.isEmpty()) {
        item {
          SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
            Text(
              text = "No quests in this category. Use the + button to forge a custom quest.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextSecondary
            )
          }
        }
      } else {
        items(filteredQuests, key = { it.id }) { quest ->
          QuestDetailCard(
            quest = quest,
            onComplete = { onCompleteQuest(quest.id) },
            onUpdateStatus = { status -> onUpdateStatus(quest, status) },
            onDelete = { onDeleteQuest(quest.id) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(72.dp))
      }
    }

    // Add Quest FAB
    FloatingActionButton(
      onClick = { showAddDialog = true },
      containerColor = ElectricCyan,
      contentColor = VoidBlack,
      shape = CutCornerShape(10.dp),
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(20.dp)
        .testTag("add_quest_fab")
    ) {
      Icon(Icons.Default.Add, contentDescription = "Add Quest")
    }

    if (showAddDialog) {
      AddEditQuestDialog(
        onDismiss = { showAddDialog = false },
        onAddQuest = onAddQuest
      )
    }
  }
}

@Composable
fun QuestDetailCard(
  quest: QuestEntity,
  onComplete: () -> Unit,
  onUpdateStatus: (String) -> Unit,
  onDelete: () -> Unit
) {
  val isCompleted = quest.status == "Completed"
  val borderColor = when (quest.status) {
    "Completed" -> QuestCompletedColor
    "In Progress" -> QuestInProgressColor
    "Skipped" -> QuestSkippedColor
    else -> if (quest.isMainQuest) ElectricCyan else HologramCardBorder
  }

  SystemHologramCard(
    borderColor = borderColor,
    modifier = Modifier.fillMaxWidth()
  ) {
    Column {
      // Header row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CutCornerShape(3.dp),
            color = if (quest.isMainQuest) ElectricCyan.copy(alpha = 0.2f) else MonarchViolet.copy(alpha = 0.2f),
            border = BorderStroke(0.8.dp, if (quest.isMainQuest) ElectricCyan else MonarchViolet)
          ) {
            Text(
              text = if (quest.isMainQuest) "MAIN QUEST" else "SIDE QUEST",
              style = MaterialTheme.typography.labelSmall,
              fontSize = 9.sp,
              color = if (quest.isMainQuest) ElectricCyan else MonarchViolet,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          Surface(
            shape = CutCornerShape(3.dp),
            color = HologramCardBg,
            border = BorderStroke(0.6.dp, TextMuted)
          ) {
            Text(
              text = "RANK ${quest.difficulty}",
              style = MaterialTheme.typography.labelSmall,
              fontSize = 8.sp,
              color = RankS_Color,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "+${quest.xpReward} XP",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = RankS_Color
          )
          Spacer(modifier = Modifier.width(8.dp))
          IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = quest.title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = if (isCompleted) TextMuted else TextPrimary
      )

      if (quest.description.isNotBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = quest.description,
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Timing and Attribute tags
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (quest.startTime.isNotBlank() || quest.deadline.isNotBlank()) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Schedule, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${quest.startTime} - ${quest.deadline}",
              style = MaterialTheme.typography.bodySmall,
              color = TextMuted
            )
          }
        } else {
          Spacer(modifier = Modifier.width(1.dp))
        }

        // Status action buttons
        if (isCompleted) {
          Surface(
            shape = CutCornerShape(3.dp),
            color = QuestCompletedColor.copy(alpha = 0.2f),
            border = BorderStroke(0.8.dp, QuestCompletedColor)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = QuestCompletedColor, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("CLEARED", style = MaterialTheme.typography.labelSmall, color = QuestCompletedColor, fontWeight = FontWeight.Bold)
            }
          }
        } else {
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Surface(
              shape = CutCornerShape(3.dp),
              color = HologramCardBg,
              border = BorderStroke(0.8.dp, QuestInProgressColor),
              modifier = Modifier.clickable { onUpdateStatus("In Progress") }
            ) {
              Text(
                text = "IN PROGRESS",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                color = QuestInProgressColor,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
              )
            }

            GlowButton(
              text = "COMPLETE",
              onClick = onComplete,
              color = ElectricCyan,
              icon = Icons.Default.Check,
              testTag = "quest_complete_${quest.id}"
            )
          }
        }
      }
    }
  }
}
