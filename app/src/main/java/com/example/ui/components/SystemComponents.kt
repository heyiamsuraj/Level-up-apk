package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HologramCardBg
import com.example.ui.theme.HologramCardBorder
import com.example.ui.theme.MonarchViolet
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.RankA_Color
import com.example.ui.theme.RankB_Color
import com.example.ui.theme.RankC_Color
import com.example.ui.theme.RankD_Color
import com.example.ui.theme.RankE_Color
import com.example.ui.theme.RankNational_Color
import com.example.ui.theme.RankS_Color
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VoidBlack

@Composable
fun SystemHologramCard(
  modifier: Modifier = Modifier,
  borderColor: Color = HologramCardBorder,
  backgroundColor: Color = HologramCardBg,
  glowAlpha: Float = 0.3f,
  cutCorners: Boolean = true,
  onClick: (() -> Unit)? = null,
  content: @Composable () -> Unit
) {
  val shape = if (cutCorners) CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp, topEnd = 2.dp, bottomStart = 2.dp) else RoundedCornerShape(12.dp)
  val clickableModifier = if (onClick != null) modifier.clickable { onClick() } else modifier

  Card(
    modifier = clickableModifier,
    shape = shape,
    colors = CardDefaults.cardColors(containerColor = backgroundColor),
    border = BorderStroke(1.2.dp, borderColor)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .drawBehind {
          // Draw subtle glowing line on top
          drawLine(
            brush = Brush.horizontalGradient(
              listOf(Color.Transparent, borderColor.copy(alpha = glowAlpha), Color.Transparent)
            ),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 3f
          )
        }
        .padding(14.dp)
    ) {
      content()
    }
  }
}

@Composable
fun RankBadge(
  rank: String,
  modifier: Modifier = Modifier,
  fontSize: Int = 12
) {
  val (badgeColor, textColor) = when (rank) {
    "National-Level Hunter" -> RankNational_Color to Color.White
    "S-Rank" -> RankS_Color to VoidBlack
    "A-Rank" -> RankA_Color to Color.White
    "B-Rank" -> RankB_Color to Color.White
    "C-Rank" -> RankC_Color to VoidBlack
    "D-Rank" -> RankD_Color to VoidBlack
    else -> RankE_Color to VoidBlack
  }

  Surface(
    modifier = modifier,
    shape = CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp),
    color = badgeColor.copy(alpha = 0.25f),
    border = BorderStroke(1.dp, badgeColor)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = Icons.Default.Shield,
        contentDescription = "Rank Icon",
        tint = badgeColor,
        modifier = Modifier.size(14.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = rank.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize.sp,
        color = badgeColor
      )
    }
  }
}

@Composable
fun SystemProgressBar(
  progress: Float,
  modifier: Modifier = Modifier,
  color: Color = ElectricCyan,
  trackColor: Color = HologramCardBg,
  height: Dp = 8.dp
) {
  val safeProgress = progress.coerceIn(0f, 1f)
  Box(
    modifier = modifier
      .height(height)
      .fillMaxWidth()
      .clip(CutCornerShape(topStart = 3.dp, bottomEnd = 3.dp))
      .background(trackColor)
      .border(0.8.dp, color.copy(alpha = 0.4f), CutCornerShape(topStart = 3.dp, bottomEnd = 3.dp))
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth(fraction = safeProgress)
        .height(height)
        .background(
          Brush.horizontalGradient(
            listOf(color.copy(alpha = 0.7f), color)
          )
        )
    )
  }
}

@Composable
fun GlowButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = "glow_button",
  isPrimary: Boolean = true,
  icon: ImageVector? = null,
  color: Color = ElectricCyan
) {
  Button(
    onClick = onClick,
    modifier = modifier.testTag(testTag),
    shape = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = if (isPrimary) color else Color.Transparent,
      contentColor = if (isPrimary) VoidBlack else color
    ),
    border = if (!isPrimary) BorderStroke(1.5.dp, color) else BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
  ) {
    if (icon != null) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier.size(16.dp),
        tint = if (isPrimary) VoidBlack else color
      )
      Spacer(modifier = Modifier.width(6.dp))
    }
    Text(
      text = text.uppercase(),
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold,
      color = if (isPrimary) VoidBlack else color
    )
  }
}

@Composable
fun HunterBottomBar(
  currentRoute: String,
  onNavigate: (String) -> Unit
) {
  val items = listOf(
    NavigationItem("dashboard", "Dashboard", Icons.Outlined.Home, Icons.Outlined.Home),
    NavigationItem("quests", "Quests", Icons.Outlined.CheckCircle, Icons.Filled.CheckCircle),
    NavigationItem("study", "Study", Icons.Outlined.MenuBook, Icons.Filled.MenuBook),
    NavigationItem("fitness", "Fitness", Icons.Outlined.FitnessCenter, Icons.Filled.FitnessCenter),
    NavigationItem("chat", "System AI", Icons.Outlined.SmartToy, Icons.Filled.SmartToy),
    NavigationItem("player", "Player", Icons.Outlined.Person, Icons.Filled.Person)
  )

  NavigationBar(
    containerColor = VoidBlack.copy(alpha = 0.95f),
    contentColor = TextSecondary,
    tonalElevation = 8.dp,
    modifier = Modifier
      .border(BorderStroke(0.8.dp, HologramCardBorder.copy(alpha = 0.3f)))
      .testTag("hunter_bottom_bar")
  ) {
    items.forEach { item ->
      val selected = currentRoute == item.route
      NavigationBarItem(
        selected = selected,
        onClick = { onNavigate(item.route) },
        icon = {
          Icon(
            imageVector = if (selected) item.activeIcon else item.icon,
            contentDescription = item.label,
            modifier = Modifier.size(22.dp)
          )
        },
        label = {
          Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 10.sp
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = ElectricCyan,
          selectedTextColor = ElectricCyan,
          indicatorColor = ElectricCyan.copy(alpha = 0.15f),
          unselectedIconColor = TextMuted,
          unselectedTextColor = TextMuted
        ),
        modifier = Modifier.testTag("nav_item_${item.route}")
      )
    }
  }
}

data class NavigationItem(
  val route: String,
  val label: String,
  val icon: ImageVector,
  val activeIcon: ImageVector
)

@Composable
fun LevelUpCelebrationDialog(
  oldLevel: Int,
  newLevel: Int,
  newRank: String,
  gainedXp: Int,
  onDismiss: () -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "levelup_glow")
  val glowAlpha by infiniteTransition.animateFloat(
    initialValue = 0.3f,
    targetValue = 0.9f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "glow"
  )

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp),
      colors = CardDefaults.cardColors(containerColor = VoidBlack),
      border = BorderStroke(2.dp, ElectricCyan.copy(alpha = glowAlpha)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("level_up_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Holographic header
        Text(
          text = "[ SYSTEM NOTIFICATION ]",
          style = MaterialTheme.typography.labelMedium,
          color = ElectricCyan,
          letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          tint = ElectricCyan,
          modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "LEVEL UP!",
          style = MaterialTheme.typography.displayMedium,
          fontWeight = FontWeight.Bold,
          color = ElectricCyan,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "YOUR EFFORTS HAVE BEEN RECOGNIZED.",
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Level change display
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(HologramCardBg, CutCornerShape(8.dp))
            .border(1.dp, HologramCardBorder, CutCornerShape(8.dp))
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "PREVIOUS", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Text(text = "LVL $oldLevel", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
          }

          Icon(
            imageVector = Icons.Default.Bolt,
            contentDescription = null,
            tint = ElectricCyan,
            modifier = Modifier.size(28.dp)
          )

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "CURRENT", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
            Text(text = "LVL $newLevel", style = MaterialTheme.typography.headlineMedium, color = ElectricCyan, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        RankBadge(rank = newRank, fontSize = 14)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "+$gainedXp XP GRANTED • ALL ATTRIBUTES ENHANCED",
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        GlowButton(
          text = "CLAIM REWARD & ARISE",
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth(),
          testTag = "claim_levelup_button"
        )
      }
    }
  }
}
