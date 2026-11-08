package com.example.ui.screens.quests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.GlowButton
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HologramCardBg
import com.example.ui.theme.HologramCardBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VoidBlack

@Composable
fun AddEditQuestDialog(
  onDismiss: () -> Unit,
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
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("GATE") }
  var priority by remember { mutableStateOf("High") }
  var difficulty by remember { mutableStateOf("C") }
  var xpRewardStr by remember { mutableStateOf("50") }
  var isMainQuest by remember { mutableStateOf(true) }
  var startTime by remember { mutableStateOf("17:00") }
  var deadline by remember { mutableStateOf("19:00") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = CutCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = VoidBlack),
      border = androidx.compose.foundation.BorderStroke(1.2.dp, ElectricCyan),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("add_quest_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Text(
          text = "GENERATE NEW QUEST",
          style = MaterialTheme.typography.titleMedium,
          color = ElectricCyan,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Quest Objective / Title") },
          modifier = Modifier.fillMaxWidth().testTag("quest_title_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ElectricCyan,
            unfocusedBorderColor = HologramCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Quest Description & Directives") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 3,
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ElectricCyan,
            unfocusedBorderColor = HologramCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category (GATE/College/Fitness/Habit)") },
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = ElectricCyan,
              unfocusedBorderColor = HologramCardBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )

          Spacer(modifier = Modifier.width(8.dp))

          OutlinedTextField(
            value = xpRewardStr,
            onValueChange = { xpRewardStr = it },
            label = { Text("XP Reward") },
            modifier = Modifier.weight(0.7f),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = ElectricCyan,
              unfocusedBorderColor = HologramCardBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = startTime,
            onValueChange = { startTime = it },
            label = { Text("Start Time") },
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = ElectricCyan,
              unfocusedBorderColor = HologramCardBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )

          Spacer(modifier = Modifier.width(8.dp))

          OutlinedTextField(
            value = deadline,
            onValueChange = { deadline = it },
            label = { Text("Deadline") },
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = ElectricCyan,
              unfocusedBorderColor = HologramCardBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Classify as Main Quest", color = TextPrimary)
          Switch(
            checked = isMainQuest,
            onCheckedChange = { isMainQuest = it },
            colors = SwitchDefaults.colors(
              checkedThumbColor = ElectricCyan,
              checkedTrackColor = ElectricCyan.copy(alpha = 0.3f)
            )
          )
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
            text = "ADD QUEST",
            onClick = {
              if (title.isNotBlank()) {
                onAddQuest(
                  title,
                  description,
                  category,
                  priority,
                  difficulty,
                  xpRewardStr.toIntOrNull() ?: 50,
                  isMainQuest,
                  startTime,
                  deadline
                )
                onDismiss()
              }
            },
            testTag = "submit_add_quest_button"
          )
        }
      }
    }
  }
}
