package com.example.ui.screens.player

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.AchievementEntity
import com.example.data.model.FocusSessionEntity
import com.example.data.model.HabitEntity
import com.example.data.model.PlayerEntity
import com.example.data.model.QuoteEntity
import com.example.data.model.WorkoutLogEntity
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
import com.example.ui.theme.RankS_Color
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VoidBlack

@Composable
fun PlayerStatusScreen(
  player: PlayerEntity,
  habits: List<HabitEntity>,
  achievements: List<AchievementEntity>,
  workoutLogs: List<WorkoutLogEntity>,
  focusSessions: List<FocusSessionEntity>,
  quotes: List<QuoteEntity>,
  dashboardConfig: com.example.data.model.DashboardConfigEntity? = null,
  onSetSystemColor: ((String) -> Unit)? = null,
  onToggleHabit: (HabitEntity) -> Unit,
  onToggleQuoteFavorite: (QuoteEntity) -> Unit,
  onAddCustomQuote: (text: String, author: String, category: String) -> Unit,
  onDeleteQuote: (Long) -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("STATUS & STATS", "HABITS", "ACHIEVEMENTS", "SYSTEM GUIDE")

  Box(modifier = Modifier.fillMaxSize().background(VoidBlack).testTag("player_status_screen")) {
    Column(modifier = Modifier.fillMaxSize()) {
      Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
          text = "[ HUNTER PROFILE & ATTRIBUTES ]",
          style = MaterialTheme.typography.labelSmall,
          color = ElectricCyan,
          letterSpacing = 2.sp
        )
        Text(
          text = "PLAYER STATUS MATRIX",
          style = MaterialTheme.typography.displaySmall,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
      }

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

      when (selectedTab) {
        0 -> StatusAndAttributesContent(player, dashboardConfig, onSetSystemColor)
        1 -> HabitsContent(habits, onToggleHabit)
        2 -> AchievementsContent(achievements)
        3 -> SystemGuideContent(
          player = player,
          quotes = quotes,
          onToggleFavorite = onToggleQuoteFavorite,
          onAddQuote = onAddCustomQuote,
          onDeleteQuote = onDeleteQuote
        )
      }
    }
  }
}

@Composable
fun StatusAndAttributesContent(
  player: PlayerEntity,
  dashboardConfig: com.example.data.model.DashboardConfigEntity? = null,
  onSetSystemColor: ((String) -> Unit)? = null
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))
      SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(text = "TITLE: HUNTER", style = MaterialTheme.typography.labelSmall, color = TextMuted)
              Text(
                text = player.name.uppercase(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            }
            RankBadge(rank = player.rank, fontSize = 14)
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Level: ${player.level}", style = MaterialTheme.typography.titleMedium, color = ElectricCyan, fontWeight = FontWeight.Bold)
            Text(text = "XP: ${player.currentXp} / ${player.xpRequiredForNextLevel}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
          }

          Spacer(modifier = Modifier.height(6.dp))
          SystemProgressBar(progress = player.xpProgressRatio, height = 8.dp)
        }
      }
    }

    item {
      Text(
        text = "CORE RPG ATTRIBUTES",
        style = MaterialTheme.typography.labelLarge,
        color = ElectricCyan
      )
    }

    item {
      AttributeDetailCard(
        code = "STR",
        name = "Strength",
        value = player.str,
        color = Color(0xFFFF5964),
        description = "Developed via resistance gym training, progressive overload, and bodyweight exercise."
      )
    }

    item {
      AttributeDetailCard(
        code = "INT",
        name = "Intelligence",
        value = player.intVal,
        color = ElectricCyan,
        description = "Developed via GATE Mechanical syllabus completion, solving PYQs, and engineering derivations."
      )
    }

    item {
      AttributeDetailCard(
        code = "VIT",
        name = "Vitality",
        value = player.vit,
        color = Color(0xFF06D6A0),
        description = "Developed via vegetarian nutrition, 3L water hydration, recovery, and adequate 7-8h sleep."
      )
    }

    item {
      AttributeDetailCard(
        code = "DIS",
        name = "Discipline",
        value = player.dis,
        color = MonarchViolet,
        description = "Developed via college attendance adherence, completing planned routines, and daily streak consistency."
      )
    }

    item {
      AttributeDetailCard(
        code = "END",
        name = "Endurance",
        value = player.endVal,
        color = Color(0xFFFFB703),
        description = "Developed via sustained effort, cardiovascular conditioning, walking, and lengthy study blocks."
      )
    }

    item {
      AttributeDetailCard(
        code = "FOC",
        name = "Focus",
        value = player.foc,
        color = NeonBlue,
        description = "Developed via distraction-free Pomodoro deep work and Dungeon Focus Training."
      )
    }

    // System Color and Theme Aura Customization
    item {
      SystemAuraColorCard(
        dashboardConfig = dashboardConfig,
        onSetSystemColor = onSetSystemColor
      )
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
fun SystemAuraColorCard(
  dashboardConfig: com.example.data.model.DashboardConfigEntity?,
  onSetSystemColor: ((String) -> Unit)?
) {
  val activeHex = dashboardConfig?.accentColorHex ?: "#00F0FF"
  var customInput by remember { mutableStateOf(activeHex) }

  val presets = listOf(
    "#00F0FF" to ("Cyan" to ElectricCyan),
    "#9D4EDD" to ("Violet" to MonarchViolet),
    "#0077FE" to ("Blue" to NeonBlue),
    "#FFD166" to ("Gold" to RankS_Color),
    "#06D6A0" to ("Emerald" to Color(0xFF06D6A0)),
    "#FF3366" to ("Crimson" to Color(0xFFFF3366)),
    "#FF9E00" to ("Amber" to Color(0xFFFF9E00)),
    "#E0E1DD" to ("Silver" to Color(0xFFE0E1DD))
  )

  SystemHologramCard(
    borderColor = try { Color(android.graphics.Color.parseColor(activeHex)) } catch (e: Exception) { ElectricCyan },
    modifier = Modifier.fillMaxWidth().testTag("system_aura_card")
  ) {
    Column {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Palette, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "[ SYSTEM RESONANCE SPECTRUM ]",
            style = MaterialTheme.typography.labelSmall,
            color = ElectricCyan,
            letterSpacing = 1.5.sp
          )
          Text(
            text = "SYSTEM HUD COLOR & THEME",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Select your system aura color to instantly re-theme all status windows, holographic cards, buttons, and HUD elements across the entire application:",
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(presets) { (hex, pair) ->
          val (label, col) = pair
          val isSelected = activeHex.equals(hex, ignoreCase = true)
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .clip(CutCornerShape(4.dp))
              .background(if (isSelected) col.copy(alpha = 0.2f) else HologramCardBg)
              .border(1.dp, if (isSelected) col else HologramCardBorder, CutCornerShape(4.dp))
              .clickable {
                onSetSystemColor?.invoke(hex)
                customInput = hex
              }
              .padding(6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(col)
                .border(2.dp, if (isSelected) Color.White else Color.Transparent, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = label,
              style = MaterialTheme.typography.labelSmall,
              fontSize = 8.sp,
              color = if (isSelected) col else TextMuted,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = customInput,
          onValueChange = { customInput = it.take(7) },
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
          text = "APPLY COLOR",
          onClick = {
            if (customInput.matches(Regex("^#([A-Fa-f0-9]{6})$"))) {
              onSetSystemColor?.invoke(customInput)
            }
          },
          testTag = "apply_system_color_button"
        )
      }
    }
  }
}

@Composable
fun AttributeDetailCard(
  code: String,
  name: String,
  value: Int,
  color: Color,
  description: String
) {
  SystemHologramCard(
    borderColor = color.copy(alpha = 0.5f),
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
            shape = CutCornerShape(3.dp),
            color = color.copy(alpha = 0.2f),
            border = BorderStroke(0.8.dp, color)
          ) {
            Text(
              text = code,
              style = MaterialTheme.typography.labelSmall,
              fontSize = 9.sp,
              color = color,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = name.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
      }

      Spacer(modifier = Modifier.width(12.dp))

      Text(
        text = value.toString(),
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
        color = color
      )
    }
  }
}

@Composable
fun HabitsContent(
  habits: List<HabitEntity>,
  onToggleHabit: (HabitEntity) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))
      SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
        Column {
          Text(text = "DAILY HABIT PROTOCOL", style = MaterialTheme.typography.labelMedium, color = ElectricCyan)
          Text(text = "CONSISTENCY OVER PERFECTION", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
          Spacer(modifier = Modifier.height(4.dp))
          Text(text = "Completing habits directly fuels player attributes (DIS, VIT, STR, INT, FOC) and daily streaks.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
      }
    }

    items(habits, key = { it.id }) { habit ->
      SystemHologramCard(
        borderColor = if (habit.isCompletedToday) QuestCompletedColor else HologramCardBorder,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Checkbox(
            checked = habit.isCompletedToday,
            onCheckedChange = { onToggleHabit(habit) },
            colors = CheckboxDefaults.colors(checkedColor = QuestCompletedColor)
          )

          Spacer(modifier = Modifier.width(8.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = habit.title,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = if (habit.isCompletedToday) TextMuted else TextPrimary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Streak: ${habit.currentStreak} Days (Best: ${habit.bestStreak})",
                style = MaterialTheme.typography.bodySmall,
                color = ElectricCyan
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "• +${habit.attributeAffected}",
                style = MaterialTheme.typography.labelSmall,
                color = MonarchViolet,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
fun AchievementsContent(achievements: List<AchievementEntity>) {
  val unlockedCount = achievements.count { it.isUnlocked }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))
      SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = "HUNTER HALL OF FAME", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Text(text = "$unlockedCount / ${achievements.size} UNLOCKED", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = RankS_Color)
          }
          Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = RankS_Color, modifier = Modifier.size(36.dp))
        }
      }
    }

    items(achievements, key = { it.id }) { item ->
      SystemHologramCard(
        borderColor = if (item.isUnlocked) RankS_Color else HologramCardBorder,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (item.isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
            contentDescription = null,
            tint = if (item.isUnlocked) RankS_Color else TextMuted,
            modifier = Modifier.size(28.dp)
          )

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (item.isUnlocked) TextPrimary else TextSecondary
              )
              Text(
                text = "+${item.xpReward} XP",
                style = MaterialTheme.typography.labelSmall,
                color = RankS_Color,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = item.description,
              style = MaterialTheme.typography.bodySmall,
              color = TextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            SystemProgressBar(
              progress = item.progressRatio,
              color = if (item.isUnlocked) RankS_Color else ElectricCyan,
              height = 4.dp
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
fun SystemGuideContent(
  player: PlayerEntity,
  quotes: List<QuoteEntity>,
  onToggleFavorite: (QuoteEntity) -> Unit,
  onAddQuote: (String, String, String) -> Unit,
  onDeleteQuote: (Long) -> Unit
) {
  var showAddQuoteDialog by remember { mutableStateOf(false) }
  var selectedCategory by remember { mutableStateOf("All") }

  val categories = listOf("All", "Discipline", "GATE Prep", "Academic Success", "Fitness", "Solo Leveling", "Favorites")

  val filteredQuotes = when (selectedCategory) {
    "Favorites" -> quotes.filter { it.isFavorite }
    "All" -> quotes
    else -> quotes.filter { it.category.equals(selectedCategory, ignoreCase = true) }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))
      // System Guide Strategic Briefing Card
      SystemHologramCard(
        borderColor = ElectricCyan,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Psychology, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "SYSTEM GUIDE: TACTICAL BRIEFING",
              style = MaterialTheme.typography.labelLarge,
              color = ElectricCyan,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "Welcome Hunter ${player.name}. Based on your schedule (${player.planningMode}) and target (${player.gateStudyTargetHours}h GATE), your primary directive today is mastering Fluid & Thermodynamics numericals while securing your college attendance.\nRemember: Progress over pressure.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
          )
        }
      }
    }

    // Categories Row
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "MOTIVATIONAL QUOTES ARCHIVE", style = MaterialTheme.typography.labelLarge, color = ElectricCyan)
        GlowButton(
          text = "+ ADD QUOTE",
          onClick = { showAddQuoteDialog = true },
          testTag = "open_add_quote_button"
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(categories) { cat ->
          val isSelected = selectedCategory == cat
          Surface(
            shape = CutCornerShape(4.dp),
            color = if (isSelected) ElectricCyan.copy(alpha = 0.25f) else HologramCardBg,
            border = BorderStroke(1.dp, if (isSelected) ElectricCyan else HologramCardBorder),
            modifier = Modifier.clickable { selectedCategory = cat }
          ) {
            Text(
              text = cat.uppercase(),
              style = MaterialTheme.typography.labelSmall,
              fontSize = 9.sp,
              color = if (isSelected) ElectricCyan else TextSecondary,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )
          }
        }
      }
    }

    items(filteredQuotes, key = { it.id }) { quote ->
      SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = quote.category.uppercase(),
              style = MaterialTheme.typography.labelSmall,
              fontSize = 9.sp,
              color = MonarchViolet,
              fontWeight = FontWeight.Bold
            )

            Row {
              IconButton(onClick = { onToggleFavorite(quote) }, modifier = Modifier.size(24.dp)) {
                Icon(
                  imageVector = if (quote.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                  contentDescription = "Fav",
                  tint = if (quote.isFavorite) RankS_Color else TextMuted,
                  modifier = Modifier.size(18.dp)
                )
              }
              if (quote.isCustom) {
                IconButton(onClick = { onDeleteQuote(quote.id) }, modifier = Modifier.size(24.dp)) {
                  Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "\"${quote.text}\"",
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "— ${quote.author}",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.align(Alignment.End)
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }

  if (showAddQuoteDialog) {
    AddQuoteDialog(
      onDismiss = { showAddQuoteDialog = false },
      onSave = { txt, auth, cat ->
        onAddQuote(txt, auth, cat)
        showAddQuoteDialog = false
      }
    )
  }
}

@Composable
fun AddQuoteDialog(
  onDismiss: () -> Unit,
  onSave: (text: String, author: String, category: String) -> Unit
) {
  var text by remember { mutableStateOf("") }
  var author by remember { mutableStateOf("Player Hunter") }
  var category by remember { mutableStateOf("Discipline") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = CutCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = VoidBlack),
      border = BorderStroke(1.2.dp, ElectricCyan),
      modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("add_quote_dialog")
    ) {
      Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(text = "FORGE CUSTOM MOTIVATION", style = MaterialTheme.typography.titleMedium, color = ElectricCyan, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = text,
          onValueChange = { text = it },
          label = { Text("Motivational Quote / Mantra") },
          modifier = Modifier.fillMaxWidth().testTag("quote_text_input"),
          maxLines = 4,
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = author,
          onValueChange = { author = it },
          label = { Text("Author / Source") },
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = category,
          onValueChange = { category = it },
          label = { Text("Category (Discipline/GATE Prep/Fitness)") },
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
          TextButton(onClick = onDismiss) { Text("CANCEL", color = TextSecondary) }
          Spacer(modifier = Modifier.width(8.dp))
          GlowButton(
            text = "SAVE QUOTE",
            onClick = {
              if (text.isNotBlank()) {
                onSave(text, author, category)
              }
            },
            testTag = "submit_quote_button"
          )
        }
      }
    }
  }
}
