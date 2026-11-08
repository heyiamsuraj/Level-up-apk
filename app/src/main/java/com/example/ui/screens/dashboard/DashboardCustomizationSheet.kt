package com.example.ui.screens.dashboard

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DashboardConfigEntity
import com.example.ui.components.GlowButton
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HologramCardBg
import com.example.ui.theme.HologramCardBorder
import com.example.ui.theme.MonarchViolet
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.RankS_Color
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VoidBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardCustomizationSheet(
  currentConfig: DashboardConfigEntity,
  onSaveConfig: (
    themePreset: String,
    wallpaper: String,
    accentHex: String,
    dimming: Float,
    blur: Float
  ) -> Unit,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  var selectedPreset by remember { mutableStateOf(currentConfig.themePreset) }
  var selectedWallpaper by remember { mutableStateOf(currentConfig.wallpaperChoice) }
  var selectedAccent by remember { mutableStateOf(currentConfig.accentColorHex) }
  var dimming by remember { mutableFloatStateOf(currentConfig.overlayDimming) }
  var customUri by remember { mutableStateOf(currentConfig.customWallpaperUri) }

  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri ->
    if (uri != null) {
      customUri = uri.toString()
      selectedWallpaper = "custom_gallery"
    }
  }

  val presets = listOf(
    "SYSTEM_DEFAULT" to "System Blue",
    "SHADOW_HUNTER" to "Shadow Monarch",
    "DUNGEON_MODE" to "Neon Dungeon",
    "MINIMAL_SYSTEM" to "Minimal Dark",
    "AMOLED" to "Pure AMOLED"
  )

  val accentColors = listOf(
    "#00F0FF" to ElectricCyan,
    "#9D4EDD" to MonarchViolet,
    "#0077FE" to NeonBlue,
    "#FFD166" to RankS_Color,
    "#06D6A0" to Color(0xFF06D6A0),
    "#FF3366" to Color(0xFFFF3366),
    "#FF9E00" to Color(0xFFFF9E00),
    "#E0E1DD" to Color(0xFFE0E1DD)
  )

  var customHexInput by remember { mutableStateOf(currentConfig.accentColorHex) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = VoidBlack,
    contentColor = TextPrimary,
    modifier = Modifier.testTag("dashboard_customization_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp)
        .verticalScroll(rememberScrollState())
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "[ HUD CUSTOMIZATION ]",
            style = MaterialTheme.typography.labelSmall,
            color = ElectricCyan,
            letterSpacing = 2.sp
          )
          Text(
            text = "SYSTEM INTERFACE ENGINE",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Presets
      Text(
        text = "THEME PRESETS",
        style = MaterialTheme.typography.labelMedium,
        color = ElectricCyan
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        presets.forEach { (presetKey, label) ->
          val isSelected = selectedPreset == presetKey
          Surface(
            shape = CutCornerShape(4.dp),
            color = if (isSelected) ElectricCyan.copy(alpha = 0.2f) else HologramCardBg,
            border = BorderStroke(1.dp, if (isSelected) ElectricCyan else HologramCardBorder),
            modifier = Modifier
              .weight(1f)
              .clickable {
                selectedPreset = presetKey
                when (presetKey) {
                  "SHADOW_HUNTER" -> {
                    selectedWallpaper = "bg_shadow_monarch"
                    selectedAccent = "#9D4EDD"
                  }
                  "DUNGEON_MODE" -> {
                    selectedWallpaper = "bg_dungeon_gate"
                    selectedAccent = "#00F0FF"
                  }
                  "AMOLED" -> {
                    selectedWallpaper = "solid_dark"
                    selectedAccent = "#00F0FF"
                    dimming = 0.9f
                  }
                  else -> {
                    selectedWallpaper = "bg_dungeon_gate"
                    selectedAccent = "#00F0FF"
                  }
                }
              }
              .padding(vertical = 8.dp, horizontal = 4.dp)
          ) {
            Text(
              text = label,
              style = MaterialTheme.typography.labelSmall,
              fontSize = 9.sp,
              color = if (isSelected) ElectricCyan else TextSecondary,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Wallpaper Selection
      Text(
        text = "SYSTEM BACKGROUND WALLPAPER",
        style = MaterialTheme.typography.labelMedium,
        color = ElectricCyan
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        listOf(
          "bg_dungeon_gate" to "Dungeon Gate",
          "bg_shadow_monarch" to "Shadow Realm",
          "solid_dark" to "Deep Void"
        ).forEach { (wallKey, wallLabel) ->
          val isSelected = selectedWallpaper == wallKey
          Surface(
            shape = CutCornerShape(6.dp),
            color = if (isSelected) ElectricCyan.copy(alpha = 0.25f) else HologramCardBg,
            border = BorderStroke(1.dp, if (isSelected) ElectricCyan else HologramCardBorder),
            modifier = Modifier
              .weight(1f)
              .clickable { selectedWallpaper = wallKey }
              .padding(10.dp)
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = wallLabel,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) ElectricCyan else TextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Custom Gallery Wallpaper Button
      Surface(
        shape = CutCornerShape(6.dp),
        color = if (selectedWallpaper == "custom_gallery") ElectricCyan.copy(alpha = 0.2f) else HologramCardBg,
        border = BorderStroke(1.dp, if (selectedWallpaper == "custom_gallery") ElectricCyan else HologramCardBorder),
        modifier = Modifier
          .fillMaxWidth()
          .clickable {
            photoPickerLauncher.launch(
              PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
          }
          .padding(12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(Icons.Default.Image, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (customUri.isNotBlank()) "CUSTOM IMAGE SELECTED (TAP TO CHANGE)" else "CHOOSE CUSTOM WALLPAPER FROM GALLERY",
            style = MaterialTheme.typography.labelSmall,
            color = ElectricCyan,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Accent Color
      Text(
        text = "HOLOGRAPHIC ACCENT COLOR (SYSTEM THEME)",
        style = MaterialTheme.typography.labelMedium,
        color = ElectricCyan
      )
      Spacer(modifier = Modifier.height(8.dp))
      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(accentColors) { (hex, col) ->
          val isSelected = selectedAccent.equals(hex, ignoreCase = true)
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(col)
              .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) Color.White else Color.Transparent,
                shape = CircleShape
              )
              .clickable {
                selectedAccent = hex
                customHexInput = hex
              },
            contentAlignment = Alignment.Center
          ) {
            if (isSelected) {
              Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Custom Hex Input
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = customHexInput,
          onValueChange = { customHexInput = it.take(7) },
          label = { Text("Custom HEX (#RRGGBB)", fontSize = 10.sp) },
          singleLine = true,
          modifier = Modifier.weight(1f),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ElectricCyan,
            unfocusedBorderColor = HologramCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        GlowButton(
          text = "APPLY HEX",
          onClick = {
            if (customHexInput.matches(Regex("^#([A-Fa-f0-9]{6})$"))) {
              selectedAccent = customHexInput
            }
          },
          testTag = "apply_custom_hex_button"
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Dimming Slider
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "BACKGROUND OVERLAY DARKNESS",
          style = MaterialTheme.typography.labelMedium,
          color = ElectricCyan
        )
        Text(
          text = "${(dimming * 100).toInt()}%",
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary
        )
      }
      Slider(
        value = dimming,
        onValueChange = { dimming = it },
        valueRange = 0.2f..0.95f,
        colors = SliderDefaults.colors(
          thumbColor = ElectricCyan,
          activeTrackColor = ElectricCyan,
          inactiveTrackColor = HologramCardBorder
        )
      )

      Spacer(modifier = Modifier.height(20.dp))

      GlowButton(
        text = "APPLY CONFIGURATION",
        onClick = {
          onSaveConfig(selectedPreset, selectedWallpaper, selectedAccent, dimming, 0f)
          onDismiss()
        },
        modifier = Modifier.fillMaxWidth(),
        testTag = "apply_customization_button"
      )

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
