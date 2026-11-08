package com.example.ui.screens.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.DashboardConfigEntity
import com.example.data.model.GateSubjectEntity
import com.example.data.model.PlayerEntity
import com.example.data.model.QuestEntity
import com.example.data.model.QuoteEntity
import com.example.ui.components.GlowButton
import com.example.ui.components.RankBadge
import com.example.ui.components.SystemHologramCard
import com.example.ui.components.SystemProgressBar
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HologramCardBg
import com.example.ui.theme.HologramCardBorder
import com.example.ui.theme.MonarchViolet
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.QuestCompletedColor
import com.example.ui.theme.QuestInProgressColor
import com.example.ui.theme.RankS_Color
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VoidBlack

@Composable
fun DashboardScreen(
  player: PlayerEntity,
  dashboardConfig: DashboardConfigEntity,
  todayQuests: List<QuestEntity>,
  gateSubjects: List<GateSubjectEntity>,
  quotes: List<QuoteEntity>,
  onCompleteQuest: (Long) -> Unit,
  onNavigateToTab: (String) -> Unit,
  onStartFocus: () -> Unit,
  onSaveDashboardConfig: (String, String, String, Float, Float) -> Unit,
  onToggleQuoteFavorite: (QuoteEntity) -> Unit,
  onCompleteSurvivalQuest: () -> Unit
) {
  var showCustomizationSheet by remember { mutableStateOf(false) }

  val completedQuestsCount = todayQuests.count { it.status == "Completed" }
  val totalQuestsCount = todayQuests.size
  val completionRate = if (totalQuestsCount > 0) (completedQuestsCount.toFloat() / totalQuestsCount.toFloat()) else 0f

  val todayQuote = remember(quotes) {
    if (quotes.isNotEmpty()) quotes.first() else null
  }

  Box(modifier = Modifier.fillMaxSize().testTag("dashboard_screen")) {
    // 1. Wallpaper Background
    when {
      dashboardConfig.wallpaperChoice == "bg_dungeon_gate" -> {
        Image(
          painter = painterResource(id = R.drawable.bg_dungeon_gate),
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }
      dashboardConfig.wallpaperChoice == "bg_shadow_monarch" -> {
        Image(
          painter = painterResource(id = R.drawable.bg_shadow_monarch),
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }
      dashboardConfig.wallpaperChoice == "custom_gallery" && dashboardConfig.customWallpaperUri.isNotBlank() -> {
        AsyncImage(
          model = dashboardConfig.customWallpaperUri,
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }
      else -> {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                listOf(VoidBlack, Color(0xFF070E1B), Color(0xFF03070E))
              )
            )
        )
      }
    }

    // 2. Dimming Overlay
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(VoidBlack.copy(alpha = dashboardConfig.overlayDimming))
    )

    // 3. Scrollable Dashboard Content
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(8.dp))
        // Top System HUD Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "● SYSTEM INITIALIZED",
              style = MaterialTheme.typography.labelSmall,
              color = ElectricCyan,
              letterSpacing = 1.5.sp
            )
            Text(
              text = player.name.uppercase(),
              style = MaterialTheme.typography.headlineLarge,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            RankBadge(rank = player.rank, fontSize = 12)
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
              onClick = { showCustomizationSheet = true },
              modifier = Modifier
                .size(38.dp)
                .background(HologramCardBg, CutCornerShape(4.dp))
                .border(1.dp, HologramCardBorder, CutCornerShape(4.dp))
                .testTag("open_customization_button")
            ) {
              Icon(
                Icons.Default.Settings,
                contentDescription = "Customize HUD",
                tint = ElectricCyan,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }

      // Player Level & XP HUD Card
      item {
        SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Bottom
            ) {
              Column {
                Text(
                  text = "PLAYER LEVEL",
                  style = MaterialTheme.typography.labelSmall,
                  color = TextMuted
                )
                Text(
                  text = "LEVEL ${player.level}",
                  style = MaterialTheme.typography.displaySmall,
                  color = ElectricCyan,
                  fontWeight = FontWeight.Bold
                )
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "XP: ${player.currentXp} / ${player.xpRequiredForNextLevel}",
                  style = MaterialTheme.typography.labelSmall,
                  color = TextSecondary
                )
                Text(
                  text = "MODE: ${player.planningMode.uppercase()}",
                  style = MaterialTheme.typography.labelSmall,
                  color = MonarchViolet,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            SystemProgressBar(
              progress = player.xpProgressRatio,
              color = ElectricCyan,
              height = 10.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Stats Hexagon / Grid
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              AttributePill("STR", player.str, Color(0xFFFF5964))
              AttributePill("INT", player.intVal, ElectricCyan)
              AttributePill("VIT", player.vit, Color(0xFF06D6A0))
              AttributePill("DIS", player.dis, MonarchViolet)
              AttributePill("END", player.endVal, Color(0xFFFFB703))
              AttributePill("FOC", player.foc, NeonBlue)
            }
          }
        }
      }

      // Daily Survival Quest Card
      item {
        val isSurvivalCompleted = player.dailySurvivalCompletedDate.isNotBlank()
        SystemHologramCard(
          borderColor = if (isSurvivalCompleted) QuestCompletedColor else RankS_Color,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Bolt,
                  contentDescription = null,
                  tint = if (isSurvivalCompleted) QuestCompletedColor else RankS_Color,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "DAILY SURVIVAL QUEST",
                  style = MaterialTheme.typography.labelLarge,
                  color = if (isSurvivalCompleted) QuestCompletedColor else RankS_Color,
                  fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = if (isSurvivalCompleted) "COMPLETED! ALL ATTRIBUTES +15 & 100 XP CLAIMED" else "GATE Study + College + Workout + 3L Water + Adequate Sleep",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
              )
            }

            if (!isSurvivalCompleted) {
              GlowButton(
                text = "CLAIM",
                onClick = onCompleteSurvivalQuest,
                color = RankS_Color,
                testTag = "claim_survival_quest_button"
              )
            } else {
              Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Completed",
                tint = QuestCompletedColor,
                modifier = Modifier.size(28.dp)
              )
            }
          }
        }
      }

      // Quick Actions Row
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          QuickActionButton(
            label = "DUNGEON FOCUS",
            icon = Icons.Default.PlayArrow,
            color = ElectricCyan,
            modifier = Modifier.weight(1f),
            onClick = onStartFocus
          )
          QuickActionButton(
            label = "ALL QUESTS",
            icon = Icons.Default.CheckCircle,
            color = MonarchViolet,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateToTab("quests") }
          )
          QuickActionButton(
            label = "GATE ME HUB",
            icon = Icons.Default.MenuBook,
            color = NeonBlue,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateToTab("study") }
          )
          QuickActionButton(
            label = "SYSTEM AI",
            icon = Icons.Default.SmartToy,
            color = RankS_Color,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateToTab("chat") }
          )
        }
      }

      // Today's Quests Header
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "TODAY'S QUEST LOG",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "($completedQuestsCount/$totalQuestsCount)",
              style = MaterialTheme.typography.labelSmall,
              color = ElectricCyan
            )
          }

          Text(
            text = "VIEW ALL →",
            style = MaterialTheme.typography.labelSmall,
            color = ElectricCyan,
            modifier = Modifier.clickable { onNavigateToTab("quests") }
          )
        }
      }

      // List of Top Quests
      if (todayQuests.isEmpty()) {
        item {
          SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
            Text(
              text = "No quests logged for today. Tap 'Quests' to add or generate daily quests.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextSecondary
            )
          }
        }
      } else {
        items(todayQuests.take(5)) { quest ->
          QuestItemDashboardCard(
            quest = quest,
            onComplete = { onCompleteQuest(quest.id) }
          )
        }
      }

      // Daily Motivational Quote Card
      if (todayQuote != null) {
        item {
          SystemHologramCard(
            borderColor = MonarchViolet.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    Icons.Default.FormatQuote,
                    contentDescription = null,
                    tint = MonarchViolet,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = todayQuote.category.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MonarchViolet,
                    fontWeight = FontWeight.Bold
                  )
                }

                IconButton(
                  onClick = { onToggleQuoteFavorite(todayQuote) },
                  modifier = Modifier.size(24.dp)
                ) {
                  Icon(
                    imageVector = if (todayQuote.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Favorite",
                    tint = if (todayQuote.isFavorite) RankS_Color else TextMuted,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = "\"${todayQuote.text}\"",
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                lineHeight = 22.sp
              )

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = "— ${todayQuote.author}",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                modifier = Modifier.align(Alignment.End)
              )
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }

    // Customization Sheet
    if (showCustomizationSheet) {
      DashboardCustomizationSheet(
        currentConfig = dashboardConfig,
        onSaveConfig = onSaveDashboardConfig,
        onDismiss = { showCustomizationSheet = false }
      )
    }
  }
}

@Composable
fun AttributePill(name: String, value: Int, color: Color) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .background(HologramCardBg, CutCornerShape(4.dp))
      .border(0.8.dp, color.copy(alpha = 0.5f), CutCornerShape(4.dp))
      .padding(horizontal = 6.dp, vertical = 4.dp)
  ) {
    Text(
      text = name,
      style = MaterialTheme.typography.labelSmall,
      fontSize = 9.sp,
      color = color,
      fontWeight = FontWeight.Bold
    )
    Text(
      text = value.toString(),
      style = MaterialTheme.typography.titleSmall,
      color = TextPrimary,
      fontWeight = FontWeight.Bold
    )
  }
}

@Composable
fun QuickActionButton(
  label: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Surface(
    shape = CutCornerShape(6.dp),
    color = HologramCardBg,
    border = BorderStroke(1.dp, color.copy(alpha = 0.6f)),
    modifier = modifier.clickable { onClick() }
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        fontSize = 9.sp,
        color = color,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
fun QuestItemDashboardCard(
  quest: QuestEntity,
  onComplete: () -> Unit
) {
  val isCompleted = quest.status == "Completed"
  val borderColor = if (isCompleted) QuestCompletedColor.copy(alpha = 0.5f) else HologramCardBorder

  SystemHologramCard(
    borderColor = borderColor,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CutCornerShape(2.dp),
            color = ElectricCyan.copy(alpha = 0.15f),
            border = BorderStroke(0.6.dp, ElectricCyan)
          ) {
            Text(
              text = quest.category.uppercase(),
              style = MaterialTheme.typography.labelSmall,
              fontSize = 8.sp,
              color = ElectricCyan,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          Text(
            text = "+${quest.xpReward} XP",
            style = MaterialTheme.typography.labelSmall,
            color = RankS_Color,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = quest.title,
          style = MaterialTheme.typography.bodyMedium,
          color = if (isCompleted) TextMuted else TextPrimary,
          fontWeight = FontWeight.SemiBold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      if (isCompleted) {
        Icon(
          Icons.Default.CheckCircle,
          contentDescription = "Completed",
          tint = QuestCompletedColor,
          modifier = Modifier.size(24.dp)
        )
      } else {
        IconButton(
          onClick = onComplete,
          modifier = Modifier
            .size(32.dp)
            .background(ElectricCyan.copy(alpha = 0.15f), CutCornerShape(4.dp))
            .border(1.dp, ElectricCyan, CutCornerShape(4.dp))
            .testTag("complete_quest_${quest.id}")
        ) {
          Icon(Icons.Default.Check, contentDescription = "Complete", tint = ElectricCyan, modifier = Modifier.size(16.dp))
        }
      }
    }
  }
}
