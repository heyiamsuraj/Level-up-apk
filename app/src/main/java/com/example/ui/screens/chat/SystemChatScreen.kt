package com.example.ui.screens.chat

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.SystemChatRoles
import com.example.data.model.ChatMessageEntity
import com.example.data.model.PlayerEntity
import com.example.ui.components.GlowButton
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HologramCardBg
import com.example.ui.theme.HologramCardBorder
import com.example.ui.theme.MonarchViolet
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.QuestWarningRed
import com.example.ui.theme.RankS_Color
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VoidBlack
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SystemChatScreen(
  player: PlayerEntity,
  messages: List<ChatMessageEntity>,
  isSending: Boolean,
  selectedRole: String,
  selectedModel: String,
  onSelectRole: (String) -> Unit,
  onSelectModel: (String) -> Unit,
  onSendMessage: (String) -> Unit,
  onClearHistory: () -> Unit
) {
  var inputText by remember { mutableStateOf("") }
  val listState = rememberLazyListState()

  // Auto-scroll when new messages arrive or when sending state changes
  LaunchedEffect(messages.size, isSending) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  val suggestedPrompts = when (selectedRole) {
    "GATE_MENTOR" -> listOf(
      "Explain Mohr's circle stress transformation shortcuts",
      "Formulas for single DOF damped vibrations",
      "Explain Exergy & Entropy generation for GATE",
      "Taylor tool life equation derivation tips"
    )
    "FITNESS_DRILL" -> listOf(
      "Hostel vegetarian high-protein day plan",
      "30-min upper body calisthenics routine",
      "How to break weight loss plateau",
      "Post-workout recovery stretching checklist"
    )
    "TIME_ARCHITECT" -> listOf(
      "Schedule study hours around my college timetable",
      "Overcoming exam prep burnout & brain fog",
      "50-10 Pomodoro strategy for engineering subjects",
      "Sleep optimization routine for 11 PM bedtime"
    )
    else -> listOf(
      "Analyze my current GATE preparation stats",
      "Give me my daily Hunter Directive",
      "Recommend workout split for this week",
      "Strategies to level up my DISCIPLINE attribute"
    )
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(VoidBlack)
      .testTag("system_chat_screen")
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // 1. Holographic Top Header
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(HologramCardBg)
          .border(BorderStroke(0.8.dp, HologramCardBorder.copy(alpha = 0.4f)))
          .padding(horizontal = 16.dp, vertical = 10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.SmartToy,
                contentDescription = null,
                tint = ElectricCyan,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "[ SYSTEM AI TERMINAL ]",
                style = MaterialTheme.typography.labelSmall,
                color = ElectricCyan,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = "GEMINI HUNTER INTELLIGENCE",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          }

          IconButton(
            onClick = onClearHistory,
            modifier = Modifier.testTag("clear_chat_history_button")
          ) {
            Icon(
              imageVector = Icons.Default.DeleteSweep,
              contentDescription = "Clear History",
              tint = TextMuted
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Role Selector Carousel
        Text(
          text = "ACTIVE AI PERSONA & ROLE INSTRUCTION:",
          style = MaterialTheme.typography.labelSmall,
          fontSize = 8.sp,
          color = TextMuted
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          items(SystemChatRoles.allRoles) { role ->
            val isSelected = selectedRole == role.id
            Surface(
              shape = CutCornerShape(4.dp),
              color = if (isSelected) ElectricCyan.copy(alpha = 0.25f) else VoidBlack,
              border = BorderStroke(1.dp, if (isSelected) ElectricCyan else HologramCardBorder),
              modifier = Modifier
                .clickable { onSelectRole(role.id) }
                .testTag("select_role_${role.id}")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = role.iconEmoji, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = role.name.uppercase(),
                  style = MaterialTheme.typography.labelSmall,
                  fontSize = 9.sp,
                  color = if (isSelected) ElectricCyan else TextSecondary,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Model Selector Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "GEMINI MODEL ENGINE:",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 8.sp,
            color = TextMuted
          )

          Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(
              "gemini-3.8-flash" to "Flash (General)",
              "gemini-4-argon" to "Argon (Deep STEM)",
              "gemini-3.1-flash-lite" to "Lite (Fast)"
            ).forEach { (modelId, label) ->
              val isSelected = selectedModel == modelId
              Surface(
                shape = CutCornerShape(2.dp),
                color = if (isSelected) MonarchViolet.copy(alpha = 0.3f) else Color.Transparent,
                border = BorderStroke(0.6.dp, if (isSelected) MonarchViolet else HologramCardBorder.copy(alpha = 0.5f)),
                modifier = Modifier
                  .clickable { onSelectModel(modelId) }
                  .testTag("select_model_$modelId")
              ) {
                Text(
                  text = label,
                  style = MaterialTheme.typography.labelSmall,
                  fontSize = 8.sp,
                  color = if (isSelected) MonarchViolet else TextMuted,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
              }
            }
          }
        }
      }

      // 2. Chat Messages Thread
      LazyColumn(
        state = listState,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        item {
          Spacer(modifier = Modifier.height(8.dp))
        }

        items(messages, key = { it.id }) { msg ->
          ChatMessageBubble(msg = msg)
        }

        if (isSending) {
          item {
            SendingIndicatorBubble(roleName = SystemChatRoles.getRole(selectedRole).name, modelName = selectedModel)
          }
        }

        item {
          Spacer(modifier = Modifier.height(8.dp))
        }
      }

      // 3. Quick Action Suggested Prompts
      if (!isSending) {
        LazyRow(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          items(suggestedPrompts) { prompt ->
            Surface(
              shape = CutCornerShape(4.dp),
              color = HologramCardBg,
              border = BorderStroke(0.6.dp, ElectricCyan.copy(alpha = 0.5f)),
              modifier = Modifier.clickable {
                inputText = prompt
              }
            ) {
              Text(
                text = prompt,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = ElectricCyan,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
              )
            }
          }
        }
      }

      // 4. Bottom Input Bar
      Surface(
        color = HologramCardBg,
        border = BorderStroke(0.8.dp, HologramCardBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            placeholder = {
              Text(
                text = "Consult the System AI (${SystemChatRoles.getRole(selectedRole).name})...",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
              )
            },
            maxLines = 4,
            modifier = Modifier
              .weight(1f)
              .testTag("chat_input_field"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = ElectricCyan,
              unfocusedBorderColor = HologramCardBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary,
              cursorColor = ElectricCyan
            )
          )

          Spacer(modifier = Modifier.width(8.dp))

          IconButton(
            onClick = {
              if (inputText.isNotBlank() && !isSending) {
                val toSend = inputText
                inputText = ""
                onSendMessage(toSend)
              }
            },
            enabled = inputText.isNotBlank() && !isSending,
            modifier = Modifier
              .size(48.dp)
              .clip(CutCornerShape(6.dp))
              .background(if (inputText.isNotBlank() && !isSending) ElectricCyan else HologramCardBg)
              .border(1.dp, if (inputText.isNotBlank() && !isSending) ElectricCyan else HologramCardBorder, CutCornerShape(6.dp))
              .testTag("send_chat_message_button")
          ) {
            if (isSending) {
              CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = ElectricCyan,
                strokeWidth = 2.dp
              )
            } else {
              Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = if (inputText.isNotBlank()) VoidBlack else TextMuted,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun ChatMessageBubble(msg: ChatMessageEntity) {
  val isUser = msg.sender == "user"
  val sdf = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
  val timeStr = remember(msg.timestamp) { sdf.format(Date(msg.timestamp)) }

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
  ) {
    if (!isUser) {
      Box(
        modifier = Modifier
          .padding(top = 4.dp, end = 6.dp)
          .size(28.dp)
          .clip(CircleShape)
          .background(MonarchViolet.copy(alpha = 0.2f))
          .border(1.dp, MonarchViolet, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.SmartToy,
          contentDescription = null,
          tint = MonarchViolet,
          modifier = Modifier.size(16.dp)
        )
      }
    }

    Card(
      shape = CutCornerShape(
        topStart = if (isUser) 10.dp else 2.dp,
        topEnd = if (isUser) 2.dp else 10.dp,
        bottomStart = 10.dp,
        bottomEnd = 10.dp
      ),
      colors = CardDefaults.cardColors(
        containerColor = if (isUser) NeonBlue.copy(alpha = 0.2f) else if (msg.isError) QuestWarningRed.copy(alpha = 0.15f) else HologramCardBg
      ),
      border = BorderStroke(
        1.dp,
        if (isUser) NeonBlue.copy(alpha = 0.8f) else if (msg.isError) QuestWarningRed else ElectricCyan.copy(alpha = 0.5f)
      ),
      modifier = Modifier
        .fillMaxWidth(if (isUser) 0.85f else 0.95f)
        .testTag("chat_bubble_${msg.id}")
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        // Bubble Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (isUser) "HUNTER" else "[SYSTEM: ${msg.roleName.uppercase()}]",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            color = if (isUser) NeonBlue else if (msg.isError) QuestWarningRed else ElectricCyan,
            fontWeight = FontWeight.Bold
          )

          Row(verticalAlignment = Alignment.CenterVertically) {
            if (!isUser && msg.modelName.isNotBlank()) {
              Surface(
                shape = CutCornerShape(2.dp),
                color = Color.Black.copy(alpha = 0.4f),
                border = BorderStroke(0.5.dp, HologramCardBorder)
              ) {
                Text(
                  text = msg.modelName,
                  fontSize = 7.sp,
                  color = TextMuted,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
              }
              Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
              text = timeStr,
              style = MaterialTheme.typography.labelSmall,
              fontSize = 8.sp,
              color = TextMuted
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Message Content
        SelectionContainer {
          Text(
            text = msg.message,
            style = MaterialTheme.typography.bodyMedium,
            color = if (msg.isError) QuestWarningRed else TextPrimary,
            lineHeight = 20.sp,
            fontFamily = if (!isUser) FontFamily.Default else FontFamily.Default
          )
        }
      }
    }
  }
}

@Composable
fun SendingIndicatorBubble(roleName: String, modelName: String) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse_trans")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.3f,
    targetValue = 0.9f,
    animationSpec = infiniteRepeatable(
      animation = tween(600, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Start
  ) {
    Card(
      shape = CutCornerShape(8.dp),
      colors = CardDefaults.cardColors(containerColor = HologramCardBg),
      border = BorderStroke(1.dp, ElectricCyan.copy(alpha = pulseAlpha)),
      modifier = Modifier.fillMaxWidth(0.9f)
    ) {
      Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        CircularProgressIndicator(
          modifier = Modifier.size(16.dp),
          color = ElectricCyan,
          strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "[RECEIVING NEURAL TRANSMISSION FROM $roleName via $modelName...]",
          style = MaterialTheme.typography.labelSmall,
          fontSize = 9.sp,
          color = ElectricCyan,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}
