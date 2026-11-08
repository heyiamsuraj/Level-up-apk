package com.example.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlowButton
import com.example.ui.components.SystemHologramCard
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HologramCardBg
import com.example.ui.theme.HologramCardBorder
import com.example.ui.theme.MonarchViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VoidBlack

@Composable
fun OnboardingAwakeningScreen(
  onCompleteAwakening: (
    name: String,
    collegeTiming: String,
    wakeTime: String,
    sleepTime: String,
    gateHours: Float,
    currentWeight: Float,
    targetWeight: Float,
    prepLevel: String
  ) -> Unit
) {
  var name by remember { mutableStateOf("Suraj") }
  var collegeTiming by remember { mutableStateOf("09:00 - 16:30") }
  var wakeTime by remember { mutableStateOf("06:30") }
  var sleepTime by remember { mutableStateOf("23:00") }
  var gateHours by remember { mutableFloatStateOf(3.5f) }
  var currentWeightStr by remember { mutableStateOf("76.5") }
  var targetWeightStr by remember { mutableStateOf("68.0") }
  var prepLevel by remember { mutableStateOf("Intermediate") }
  var hasAwakenedAnimation by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(VoidBlack, Color(0xFF040A18), Color(0xFF02050B))
        )
      )
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(16.dp)
  ) {
    if (hasAwakenedAnimation) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          tint = ElectricCyan,
          modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "[ SYSTEM NOTIFICATION ]",
          style = MaterialTheme.typography.labelMedium,
          color = ElectricCyan,
          letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "THE SYSTEM HAS AWAKENED.",
          style = MaterialTheme.typography.displayMedium,
          color = ElectricCyan,
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "PLAYER ID: ${name.uppercase()} • RANK: E-RANK HUNTER\nYOUR REAL LIFE IS THE MAIN QUEST.",
          style = MaterialTheme.typography.bodyLarge,
          color = TextSecondary,
          textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        GlowButton(
          text = "ENTER SYSTEM DASHBOARD",
          onClick = {
            onCompleteAwakening(
              name,
              collegeTiming,
              wakeTime,
              sleepTime,
              gateHours,
              currentWeightStr.toFloatOrNull() ?: 76.5f,
              targetWeightStr.toFloatOrNull() ?: 68.0f,
              prepLevel
            )
          },
          modifier = Modifier.fillMaxWidth(0.85f),
          testTag = "enter_dashboard_button"
        )
      }
    } else {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Spacer(modifier = Modifier.height(8.dp))

        // System Header
        Text(
          text = "[ SYSTEM INITIALIZED ]",
          style = MaterialTheme.typography.labelMedium,
          color = ElectricCyan,
          letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "PLAYER REGISTRATION",
          style = MaterialTheme.typography.displaySmall,
          color = TextPrimary,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Final-Year B.E. Mechanical Engineering • GATE Aspirant",
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Onboarding Form
        SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
          Column {
            Text(
              text = "HUNTER CREDENTIALS",
              style = MaterialTheme.typography.labelLarge,
              color = ElectricCyan
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = name,
              onValueChange = { name = it },
              label = { Text("Player Name / Hunter Title") },
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("onboarding_name_input"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricCyan,
                unfocusedBorderColor = HologramCardBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedLabelColor = ElectricCyan,
                unfocusedLabelColor = TextSecondary
              )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
              OutlinedTextField(
                value = currentWeightStr,
                onValueChange = { currentWeightStr = it },
                label = { Text("Current Wt (kg)") },
                singleLine = true,
                modifier = Modifier
                  .weight(1f)
                  .testTag("onboarding_weight_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = ElectricCyan,
                  unfocusedBorderColor = HologramCardBorder,
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedLabelColor = ElectricCyan,
                  unfocusedLabelColor = TextSecondary
                )
              )

              Spacer(modifier = Modifier.width(10.dp))

              OutlinedTextField(
                value = targetWeightStr,
                onValueChange = { targetWeightStr = it },
                label = { Text("Target Wt (kg)") },
                singleLine = true,
                modifier = Modifier
                  .weight(1f)
                  .testTag("onboarding_target_weight_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = ElectricCyan,
                  unfocusedBorderColor = HologramCardBorder,
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedLabelColor = ElectricCyan,
                  unfocusedLabelColor = TextSecondary
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Daily Routine Card
        SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
          Column {
            Text(
              text = "ACADEMIC & RECOVERY ROUTINE",
              style = MaterialTheme.typography.labelLarge,
              color = ElectricCyan
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = collegeTiming,
              onValueChange = { collegeTiming = it },
              label = { Text("College Lectures & Labs Timing") },
              singleLine = true,
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricCyan,
                unfocusedBorderColor = HologramCardBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedLabelColor = ElectricCyan,
                unfocusedLabelColor = TextSecondary
              )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
              OutlinedTextField(
                value = wakeTime,
                onValueChange = { wakeTime = it },
                label = { Text("Wake-up Time") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = ElectricCyan,
                  unfocusedBorderColor = HologramCardBorder,
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedLabelColor = ElectricCyan,
                  unfocusedLabelColor = TextSecondary
                )
              )

              Spacer(modifier = Modifier.width(10.dp))

              OutlinedTextField(
                value = sleepTime,
                onValueChange = { sleepTime = it },
                label = { Text("Sleep Time") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = ElectricCyan,
                  unfocusedBorderColor = HologramCardBorder,
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedLabelColor = ElectricCyan,
                  unfocusedLabelColor = TextSecondary
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // GATE Target Slider Card
        SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "DAILY GATE ME STUDY TARGET",
                style = MaterialTheme.typography.labelLarge,
                color = ElectricCyan
              )
              Text(
                text = "${"%.1f".format(gateHours)} Hours",
                style = MaterialTheme.typography.titleMedium,
                color = ElectricCyan,
                fontWeight = FontWeight.Bold
              )
            }

            Slider(
              value = gateHours,
              onValueChange = { gateHours = it },
              valueRange = 1f..6f,
              steps = 9,
              colors = SliderDefaults.colors(
                thumbColor = ElectricCyan,
                activeTrackColor = ElectricCyan,
                inactiveTrackColor = HologramCardBorder
              ),
              modifier = Modifier.testTag("gate_hours_slider")
            )

            Text(
              text = "System allocates PYQs, formula revision, and deep work blocks according to this target.",
              style = MaterialTheme.typography.bodySmall,
              color = TextMuted
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        GlowButton(
          text = "AWAKEN THE SYSTEM",
          onClick = { hasAwakenedAnimation = true },
          modifier = Modifier.fillMaxWidth(),
          icon = Icons.Default.Bolt,
          testTag = "awaken_system_button"
        )

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
