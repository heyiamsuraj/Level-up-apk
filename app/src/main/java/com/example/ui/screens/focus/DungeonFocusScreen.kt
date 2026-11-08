package com.example.ui.screens.focus

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.GlowButton
import com.example.ui.components.SystemHologramCard
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HologramCardBg
import com.example.ui.theme.HologramCardBorder
import com.example.ui.theme.MonarchViolet
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.QuestCompletedColor
import com.example.ui.theme.RankS_Color
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VoidBlack
import kotlinx.coroutines.delay

@Composable
fun DungeonFocusScreen(
  onCompleteSession: (mode: String, minutes: Int, questTitle: String) -> Unit,
  onClose: () -> Unit
) {
  var selectedPresetMinutes by remember { mutableIntStateOf(25) }
  var totalSeconds by remember { mutableIntStateOf(25 * 60) }
  var secondsRemaining by remember { mutableIntStateOf(25 * 60) }
  var isRunning by remember { mutableStateOf(false) }
  var selectedMode by remember { mutableStateOf("GATE Study") }
  var sessionCompleteDialog by remember { mutableStateOf(false) }

  val modes = listOf("GATE Study", "B.E. Project", "Formula Revision", "Physical Workout")
  val presets = listOf(
    25 to "25M (SOLO)",
    50 to "50M (DUNGEON)",
    90 to "90M (BOSS RAID)"
  )

  // Timer Tick Coroutine
  LaunchedEffect(isRunning, secondsRemaining) {
    if (isRunning && secondsRemaining > 0) {
      delay(1000L)
      secondsRemaining -= 1
      if (secondsRemaining == 0) {
        isRunning = false
        sessionCompleteDialog = true
      }
    }
  }

  val progress = if (totalSeconds > 0) {
    (totalSeconds - secondsRemaining).toFloat() / totalSeconds.toFloat()
  } else 0f

  val minutesDisplay = secondsRemaining / 60
  val secondsDisplay = secondsRemaining % 60
  val timeFormatted = "%02d:%02d".format(minutesDisplay, secondsDisplay)

  val infiniteTransition = rememberInfiniteTransition(label = "portal_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.96f,
    targetValue = 1.04f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(VoidBlack, Color(0xFF030A14), Color(0xFF020409))
        )
      )
      .padding(20.dp)
      .testTag("dungeon_focus_screen")
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Header
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "[ DUNGEON FOCUS MODE ]",
          style = MaterialTheme.typography.labelSmall,
          color = ElectricCyan,
          letterSpacing = 2.sp
        )
        Text(
          text = "REALM OF INTENSE CONCENTRATION",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Mode Selectors
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          modes.forEach { mode ->
            val isSelected = selectedMode == mode
            Surface(
              shape = CutCornerShape(4.dp),
              color = if (isSelected) ElectricCyan.copy(alpha = 0.25f) else HologramCardBg,
              border = BorderStroke(1.dp, if (isSelected) ElectricCyan else HologramCardBorder),
              modifier = Modifier
                .weight(1f)
                .clickable(enabled = !isRunning) { selectedMode = mode }
            ) {
              Text(
                text = mode.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                color = if (isSelected) ElectricCyan else TextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 6.dp)
              )
            }
          }
        }
      }

      // Glowing Holographic Circular Timer
      Box(
        modifier = Modifier.size(260.dp),
        contentAlignment = Alignment.Center
      ) {
        // Outer pulsing ring
        Box(
          modifier = Modifier
            .size((260 * pulseScale).dp)
            .clip(CircleShape)
            .border(1.dp, ElectricCyan.copy(alpha = 0.2f), CircleShape)
        )

        // Progress Ring
        CircularProgressIndicator(
          progress = { progress },
          modifier = Modifier.size(240.dp),
          color = ElectricCyan,
          trackColor = HologramCardBg,
          strokeWidth = 10.dp
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = selectedMode.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MonarchViolet,
            letterSpacing = 1.sp
          )
          Text(
            text = timeFormatted,
            style = MaterialTheme.typography.displayLarge,
            fontSize = 46.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "+${selectedPresetMinutes * 2} XP REWARD",
            style = MaterialTheme.typography.labelSmall,
            color = RankS_Color,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Controls and Presets
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Preset row (disabled while timer is running)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          presets.forEach { (mins, label) ->
            val isSelected = selectedPresetMinutes == mins
            Surface(
              shape = CutCornerShape(4.dp),
              color = if (isSelected) ElectricCyan.copy(alpha = 0.2f) else HologramCardBg,
              border = BorderStroke(1.dp, if (isSelected) ElectricCyan else HologramCardBorder),
              modifier = Modifier.clickable(enabled = !isRunning) {
                selectedPresetMinutes = mins
                totalSeconds = mins * 60
                secondsRemaining = mins * 60
              }
            ) {
              Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) ElectricCyan else TextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Play / Pause / Reset buttons
        Row(
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Reset
          IconButton(
            onClick = {
              isRunning = false
              secondsRemaining = selectedPresetMinutes * 60
            },
            modifier = Modifier
              .size(48.dp)
              .background(HologramCardBg, CircleShape)
              .border(1.dp, HologramCardBorder, CircleShape)
          ) {
            Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = TextSecondary)
          }

          // Main Play/Pause
          IconButton(
            onClick = { isRunning = !isRunning },
            modifier = Modifier
              .size(64.dp)
              .background(ElectricCyan, CircleShape)
              .testTag("toggle_focus_timer_button")
          ) {
            Icon(
              imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = if (isRunning) "Pause" else "Start",
              tint = VoidBlack,
              modifier = Modifier.size(32.dp)
            )
          }

          // Quick Finish / Claim
          IconButton(
            onClick = {
              isRunning = false
              sessionCompleteDialog = true
            },
            modifier = Modifier
              .size(48.dp)
              .background(HologramCardBg, CircleShape)
              .border(1.dp, QuestCompletedColor, CircleShape)
              .testTag("finish_focus_early_button")
          ) {
            Icon(Icons.Default.Check, contentDescription = "Finish Early", tint = QuestCompletedColor)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        GlowButton(
          text = "RETURN TO SYSTEM HUD",
          onClick = onClose,
          isPrimary = false,
          modifier = Modifier.fillMaxWidth(0.85f),
          testTag = "close_focus_screen_button"
        )
      }
    }

    // Completion Dialog
    if (sessionCompleteDialog) {
      Dialog(onDismissRequest = { sessionCompleteDialog = false }) {
        Card(
          shape = CutCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = VoidBlack),
          border = BorderStroke(2.dp, ElectricCyan),
          modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("dungeon_cleared_dialog")
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "[ SYSTEM MESSAGE ]",
              style = MaterialTheme.typography.labelSmall,
              color = ElectricCyan,
              letterSpacing = 2.sp
            )
            Text(
              text = "DUNGEON CLEARED!",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.Bold,
              color = ElectricCyan
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "TRAINING SESSION COMPLETED SUCCESSFULLY.",
              style = MaterialTheme.typography.bodySmall,
              color = TextSecondary,
              textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            val earnedMinutes = ((totalSeconds - secondsRemaining) / 60).coerceAtLeast(1)
            val earnedXp = earnedMinutes * 2

            Text(
              text = "+$earnedXp XP • FOCUS +${(earnedMinutes / 10).coerceAtLeast(1)} • INTELLECT ENHANCED",
              style = MaterialTheme.typography.titleSmall,
              color = RankS_Color,
              fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            GlowButton(
              text = "CLAIM REWARDS & EXIT",
              onClick = {
                onCompleteSession(selectedMode, earnedMinutes, "Dungeon Session: $selectedMode")
                sessionCompleteDialog = false
                onClose()
              },
              modifier = Modifier.fillMaxWidth(),
              testTag = "claim_focus_rewards_button"
            )
          }
        }
      }
    }
  }
}
