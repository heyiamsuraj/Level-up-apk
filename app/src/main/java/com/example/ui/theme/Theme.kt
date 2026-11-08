package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val SystemDarkColorScheme = darkColorScheme(
  primary = ElectricCyan,
  onPrimary = VoidBlack,
  primaryContainer = MidnightSurface,
  onPrimaryContainer = ElectricCyan,
  secondary = MonarchViolet,
  onSecondary = Color.White,
  secondaryContainer = DarkPurple,
  onSecondaryContainer = Color.White,
  tertiary = NeonBlue,
  onTertiary = Color.White,
  background = VoidBlack,
  onBackground = TextPrimary,
  surface = DeepNavy,
  onSurface = TextPrimary,
  surfaceVariant = MidnightSurface,
  onSurfaceVariant = TextSecondary,
  outline = HologramCardBorder,
  error = QuestWarningRed,
  onError = Color.White
)

val AmoledDarkColorScheme = darkColorScheme(
  primary = ElectricCyan,
  onPrimary = Color.Black,
  primaryContainer = Color(0xFF0F0F14),
  onPrimaryContainer = ElectricCyan,
  secondary = MonarchViolet,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFF1E0A28),
  onSecondaryContainer = Color.White,
  tertiary = NeonBlue,
  onTertiary = Color.White,
  background = Color.Black,
  onBackground = TextPrimary,
  surface = Color.Black,
  onSurface = TextPrimary,
  surfaceVariant = Color(0xFF111111),
  onSurfaceVariant = TextSecondary,
  outline = HologramCardBorder,
  error = QuestWarningRed,
  onError = Color.White
)

@Composable
fun LevelUpSystemTheme(
  isAmoled: Boolean = false,
  accentColor: Color? = null,
  content: @Composable () -> Unit
) {
  val baseScheme = if (isAmoled) AmoledDarkColorScheme else SystemDarkColorScheme
  val colorScheme = if (accentColor != null) {
    baseScheme.copy(
      primary = accentColor,
      primaryContainer = accentColor.copy(alpha = 0.2f),
      onPrimaryContainer = accentColor
    )
  } else {
    baseScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
