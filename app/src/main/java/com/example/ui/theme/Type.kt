package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val OrbitronFontFamily = FontFamily(
  Font(R.font.orbitron, FontWeight.Normal),
  Font(R.font.orbitron, FontWeight.Bold)
)

val RajdhaniFontFamily = FontFamily(
  Font(R.font.rajdhani, FontWeight.Normal),
  Font(R.font.rajdhani, FontWeight.Medium),
  Font(R.font.rajdhani, FontWeight.Bold)
)

val Typography = Typography(
  displayLarge = TextStyle(
    fontFamily = OrbitronFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 38.sp,
    letterSpacing = 1.sp,
    color = TextPrimary
  ),
  displayMedium = TextStyle(
    fontFamily = OrbitronFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 26.sp,
    lineHeight = 32.sp,
    letterSpacing = 0.8.sp,
    color = TextPrimary
  ),
  displaySmall = TextStyle(
    fontFamily = OrbitronFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 26.sp,
    letterSpacing = 0.5.sp,
    color = TextPrimary
  ),
  headlineLarge = TextStyle(
    fontFamily = OrbitronFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    color = TextPrimary
  ),
  headlineMedium = TextStyle(
    fontFamily = OrbitronFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    color = TextPrimary
  ),
  titleLarge = TextStyle(
    fontFamily = RajdhaniFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 26.sp,
    letterSpacing = 0.5.sp,
    color = TextPrimary
  ),
  titleMedium = TextStyle(
    fontFamily = RajdhaniFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 17.sp,
    lineHeight = 22.sp,
    letterSpacing = 0.4.sp,
    color = TextPrimary
  ),
  titleSmall = TextStyle(
    fontFamily = RajdhaniFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.3.sp,
    color = TextSecondary
  ),
  bodyLarge = TextStyle(
    fontFamily = RajdhaniFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 22.sp,
    color = TextPrimary
  ),
  bodyMedium = TextStyle(
    fontFamily = RajdhaniFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    color = TextSecondary
  ),
  bodySmall = TextStyle(
    fontFamily = RajdhaniFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    color = TextMuted
  ),
  labelLarge = TextStyle(
    fontFamily = OrbitronFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    letterSpacing = 1.sp,
    color = ElectricCyan
  ),
  labelMedium = TextStyle(
    fontFamily = OrbitronFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 15.sp,
    letterSpacing = 0.8.sp,
    color = ElectricCyan
  )
)
