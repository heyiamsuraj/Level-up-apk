package com.example.ui.screens.fitness

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
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
import com.example.data.model.NutritionLogEntity
import com.example.data.model.PlayerEntity
import com.example.data.model.WaterLogEntity
import com.example.data.model.WeightLogEntity
import com.example.data.model.WorkoutLogEntity
import com.example.ui.components.GlowButton
import com.example.ui.components.SystemHologramCard
import com.example.ui.components.SystemProgressBar
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HologramCardBg
import com.example.ui.theme.HologramCardBorder
import com.example.ui.theme.MonarchViolet
import com.example.ui.theme.QuestCompletedColor
import com.example.ui.theme.RankS_Color
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VoidBlack

@Composable
fun FitnessHubScreen(
  player: PlayerEntity,
  workoutLogs: List<WorkoutLogEntity>,
  weightLogs: List<WeightLogEntity>,
  nutritionLogs: List<NutritionLogEntity>,
  waterLogs: List<WaterLogEntity>,
  generatedWorkoutPlan: com.example.data.fitness.GeneratedWorkoutPlan?,
  onGenerateWorkoutRoutine: (equipment: String, duration: Int, muscles: String, rpgClass: String) -> Unit,
  onClearGeneratedWorkout: () -> Unit,
  onAcceptWorkoutPlan: (com.example.data.fitness.GeneratedWorkoutPlan) -> Unit,
  onLogWorkout: (category: String, title: String, duration: Int, details: String, calories: Int, intensity: String) -> Unit,
  onLogWeight: (weight: Float, waist: Float, notes: String) -> Unit,
  onAddFood: (name: String, serving: String, calories: Int, protein: Float, carbs: Float, fat: Float, mealType: String) -> Unit,
  onDeleteFood: (Long) -> Unit,
  onLogWater: (amountMl: Int) -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var showWorkoutDialog by remember { mutableStateOf(false) }
  var showWorkoutGeneratorDialog by remember { mutableStateOf(false) }
  var showWeightDialog by remember { mutableStateOf(false) }
  var showFoodDialog by remember { mutableStateOf(false) }

  val tabs = listOf("TRAINING", "BODY METRICS", "VEG NUTRITION")

  Box(modifier = Modifier.fillMaxSize().background(VoidBlack).testTag("fitness_hub_screen")) {
    Column(modifier = Modifier.fillMaxSize()) {
      Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
          text = "[ PHYSICAL FORGE & VITALITY ]",
          style = MaterialTheme.typography.labelSmall,
          color = ElectricCyan,
          letterSpacing = 2.sp
        )
        Text(
          text = "HUNTER PHYSIQUE",
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
                fontSize = 11.sp,
                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                color = if (selectedTab == index) ElectricCyan else TextSecondary
              )
            }
          )
        }
      }

      when (selectedTab) {
        0 -> WorkoutTabContent(
          workoutLogs = workoutLogs,
          generatedWorkoutPlan = generatedWorkoutPlan,
          onOpenGenerator = { showWorkoutGeneratorDialog = true },
          onOpenLogDialog = { showWorkoutDialog = true },
          onAcceptWorkoutPlan = onAcceptWorkoutPlan,
          onClearGeneratedWorkout = onClearGeneratedWorkout
        )
        1 -> WeightTabContent(
          player = player,
          weightLogs = weightLogs,
          onOpenLogDialog = { showWeightDialog = true }
        )
        2 -> VegetarianNutritionTabContent(
          nutritionLogs = nutritionLogs,
          waterLogs = waterLogs,
          onOpenFoodDialog = { showFoodDialog = true },
          onDeleteFood = onDeleteFood,
          onLogWater = onLogWater,
          onQuickAddFood = onAddFood
        )
      }
    }

    if (showWorkoutGeneratorDialog) {
      WorkoutGeneratorDialog(
        onDismiss = { showWorkoutGeneratorDialog = false },
        onGenerate = { equipment, duration, muscles, rpgClass ->
          onGenerateWorkoutRoutine(equipment, duration, muscles, rpgClass)
          showWorkoutGeneratorDialog = false
        }
      )
    }

    if (showWorkoutDialog) {
      LogWorkoutDialog(
        onDismiss = { showWorkoutDialog = false },
        onSave = { cat, title, dur, det, cal, inten ->
          onLogWorkout(cat, title, dur, det, cal, inten)
          showWorkoutDialog = false
        }
      )
    }

    if (showWeightDialog) {
      LogWeightDialog(
        currentWeight = player.currentWeight,
        onDismiss = { showWeightDialog = false },
        onSave = { wt, waist, notes ->
          onLogWeight(wt, waist, notes)
          showWeightDialog = false
        }
      )
    }

    if (showFoodDialog) {
      AddCustomFoodDialog(
        onDismiss = { showFoodDialog = false },
        onSave = { name, serv, cal, p, c, f, meal ->
          onAddFood(name, serv, cal, p, c, f, meal)
          showFoodDialog = false
        }
      )
    }
  }
}

@Composable
fun WorkoutTabContent(
  workoutLogs: List<WorkoutLogEntity>,
  generatedWorkoutPlan: com.example.data.fitness.GeneratedWorkoutPlan?,
  onOpenGenerator: () -> Unit,
  onOpenLogDialog: () -> Unit,
  onAcceptWorkoutPlan: (com.example.data.fitness.GeneratedWorkoutPlan) -> Unit,
  onClearGeneratedWorkout: () -> Unit
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
              Text(text = "TOTAL TRAINING SESSIONS", style = MaterialTheme.typography.labelSmall, color = TextMuted)
              Text(text = "${workoutLogs.size} SESSIONS LOGGED", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ElectricCyan)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            GlowButton(
              text = "⚡ GENERATE WORKOUT",
              onClick = onOpenGenerator,
              modifier = Modifier.weight(1f),
              testTag = "open_workout_generator_button"
            )
            GlowButton(
              text = "+ LOG WORKOUT",
              onClick = onOpenLogDialog,
              isPrimary = false,
              modifier = Modifier.weight(1f),
              testTag = "open_log_workout_button"
            )
          }
        }
      }
    }

    // Generated Workout Plan Card (RPG Quest View)
    if (generatedWorkoutPlan != null) {
      item {
        GeneratedWorkoutPlanCard(
          plan = generatedWorkoutPlan,
          onAccept = { onAcceptWorkoutPlan(generatedWorkoutPlan) },
          onDismiss = onClearGeneratedWorkout,
          onReRoll = onOpenGenerator
        )
      }
    }

    if (workoutLogs.isEmpty()) {
      item {
        SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "No workouts logged yet. Build Strength & Endurance by logging your gym or home exercise sessions.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
          )
        }
      }
    } else {
      items(workoutLogs, key = { it.id }) { log ->
        SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  shape = CutCornerShape(3.dp),
                  color = ElectricCyan.copy(alpha = 0.2f),
                  border = BorderStroke(0.8.dp, ElectricCyan)
                ) {
                  Text(
                    text = log.category.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = ElectricCyan,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                  text = log.routineTitle,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              }

              Text(
                text = "${log.durationMinutes} MINS",
                style = MaterialTheme.typography.labelMedium,
                color = RankS_Color,
                fontWeight = FontWeight.Bold
              )
            }

            if (log.exercisesDetail.isNotBlank()) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = log.exercisesDetail,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = log.dateStr, style = MaterialTheme.typography.bodySmall, color = TextMuted)
              Text(text = "${log.caloriesBurned} kcal burned", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
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
fun WeightTabContent(
  player: PlayerEntity,
  weightLogs: List<WeightLogEntity>,
  onOpenLogDialog: () -> Unit
) {
  val remainingKg = (player.currentWeight - player.targetWeight).coerceAtLeast(0f)

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
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "CURRENT WEIGHT", style = MaterialTheme.typography.labelSmall, color = TextMuted)
              Text(text = "${player.currentWeight} KG", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = ElectricCyan)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text(text = "TARGET WEIGHT", style = MaterialTheme.typography.labelSmall, color = TextMuted)
              Text(text = "${player.targetWeight} KG", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = RankS_Color)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "BMI: ${"%.1f".format(player.bmi)}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Text(text = "${"%.1f".format(remainingKg)} kg to goal", style = MaterialTheme.typography.bodyMedium, color = QuestCompletedColor, fontWeight = FontWeight.Bold)
          }

          Spacer(modifier = Modifier.height(12.dp))

          GlowButton(
            text = "+ LOG WEIGHT TODAY",
            onClick = onOpenLogDialog,
            modifier = Modifier.fillMaxWidth(),
            testTag = "open_log_weight_button"
          )
        }
      }
    }

    item {
      Text(text = "WEIGHT HISTORY & LOGS", style = MaterialTheme.typography.labelLarge, color = ElectricCyan)
    }

    if (weightLogs.isEmpty()) {
      item {
        SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
          Text(text = "No weigh-ins logged yet. Consistent tracking ensures sustainable progress.", color = TextSecondary)
        }
      }
    } else {
      items(weightLogs, key = { it.id }) { log ->
        SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(text = log.dateStr, style = MaterialTheme.typography.labelSmall, color = TextMuted)
              Text(text = "${log.weightKg} kg", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
              if (log.notes.isNotBlank()) {
                Text(text = log.notes, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
              }
            }

            if (log.waistCm > 0) {
              Text(text = "Waist: ${log.waistCm} cm", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
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
fun VegetarianNutritionTabContent(
  nutritionLogs: List<NutritionLogEntity>,
  waterLogs: List<WaterLogEntity>,
  onOpenFoodDialog: () -> Unit,
  onDeleteFood: (Long) -> Unit,
  onLogWater: (amountMl: Int) -> Unit,
  onQuickAddFood: (name: String, serving: String, calories: Int, protein: Float, carbs: Float, fat: Float, mealType: String) -> Unit
) {
  val totalCalories = nutritionLogs.sumOf { it.calories }
  val totalProtein = nutritionLogs.sumOf { it.proteinGrams.toDouble() }.toFloat()
  val targetProtein = 85.0f
  val targetCalories = 2100

  val totalWaterMl = waterLogs.sumOf { it.amountMl }
  val targetWaterMl = 3000

  // Indian Vegetarian Quick Food Presets
  val quickFoods = listOf(
    QuickFoodPreset("Roti (Chapati)", "2 pieces", 240, 6.0f, "Lunch"),
    QuickFoodPreset("Steamed Rice", "1 bowl", 150, 3.0f, "Lunch"),
    QuickFoodPreset("Yellow Dal", "1 bowl", 150, 9.0f, "Lunch"),
    QuickFoodPreset("Paneer", "100g", 265, 18.0f, "Dinner"),
    QuickFoodPreset("Soya Chunks", "50g", 170, 26.0f, "Dinner"),
    QuickFoodPreset("Milk / Curd", "1 glass/cup", 140, 8.0f, "Breakfast"),
    QuickFoodPreset("Rolled Oats", "50g", 190, 7.0f, "Breakfast"),
    QuickFoodPreset("Moong Sprouts", "1 cup", 100, 7.0f, "Snacks")
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))
      // Nutrition Macro Card
      SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "TODAY'S VEGETARIAN PROTEIN", style = MaterialTheme.typography.labelSmall, color = TextMuted)
              Text(
                text = "${"%.1f".format(totalProtein)}g / ${targetProtein.toInt()}g",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = ElectricCyan
              )
            }
            Column(horizontalAlignment = Alignment.End) {
              Text(text = "CALORIES", style = MaterialTheme.typography.labelSmall, color = TextMuted)
              Text(
                text = "$totalCalories / $targetCalories kcal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = RankS_Color
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          SystemProgressBar(progress = totalProtein / targetProtein, height = 8.dp)
        }
      }
    }

    // Water Hydration Card
    item {
      SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.LocalDrink, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "HYDRATION: $totalWaterMl / $targetWaterMl ML",
                style = MaterialTheme.typography.labelLarge,
                color = ElectricCyan,
                fontWeight = FontWeight.Bold
              )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Surface(
                shape = CutCornerShape(3.dp),
                color = ElectricCyan.copy(alpha = 0.2f),
                border = BorderStroke(0.8.dp, ElectricCyan),
                modifier = Modifier.clickable { onLogWater(250) }
              ) {
                Text(
                  text = "+250 ML",
                  style = MaterialTheme.typography.labelSmall,
                  color = ElectricCyan,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
              Surface(
                shape = CutCornerShape(3.dp),
                color = ElectricCyan.copy(alpha = 0.2f),
                border = BorderStroke(0.8.dp, ElectricCyan),
                modifier = Modifier.clickable { onLogWater(500) }
              ) {
                Text(
                  text = "+500 ML",
                  style = MaterialTheme.typography.labelSmall,
                  color = ElectricCyan,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          SystemProgressBar(progress = totalWaterMl.toFloat() / targetWaterMl.toFloat(), height = 6.dp)
        }
      }
    }

    // Quick Add Indian Staples
    item {
      Text(text = "QUICK ADD INDIAN VEG STAPLES", style = MaterialTheme.typography.labelLarge, color = ElectricCyan)
      Spacer(modifier = Modifier.height(6.dp))
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(quickFoods) { item ->
          Surface(
            shape = CutCornerShape(4.dp),
            color = HologramCardBg,
            border = BorderStroke(0.8.dp, HologramCardBorder),
            modifier = Modifier.clickable {
              onQuickAddFood(item.name, item.serving, item.calories, item.protein, 0f, 0f, item.mealType)
            }
          ) {
            Column(modifier = Modifier.padding(8.dp)) {
              Text(text = item.name, style = MaterialTheme.typography.labelSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
              Text(text = "${item.protein}g P • ${item.calories} kcal", style = MaterialTheme.typography.bodySmall, color = ElectricCyan)
            }
          }
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "TODAY'S FOOD LOG", style = MaterialTheme.typography.labelLarge, color = ElectricCyan)
        GlowButton(
          text = "+ CUSTOM FOOD",
          onClick = onOpenFoodDialog,
          testTag = "open_add_food_button"
        )
      }
    }

    if (nutritionLogs.isEmpty()) {
      item {
        SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
          Text(text = "No food logged yet today. Tap any quick staple above to log your vegetarian meals.", color = TextSecondary)
        }
      }
    } else {
      items(nutritionLogs, key = { it.id }) { food ->
        SystemHologramCard(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(text = food.foodName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
              Text(text = "${food.quantityServing} • ${food.mealType}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Column(horizontalAlignment = Alignment.End) {
                Text(text = "${food.proteinGrams}g P", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = ElectricCyan)
                Text(text = "${food.calories} kcal", style = MaterialTheme.typography.bodySmall, color = TextMuted)
              }
              Spacer(modifier = Modifier.width(6.dp))
              IconButton(onClick = { onDeleteFood(food.id) }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
              }
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

data class QuickFoodPreset(
  val name: String,
  val serving: String,
  val calories: Int,
  val protein: Float,
  val mealType: String
)

@Composable
fun LogWorkoutDialog(
  onDismiss: () -> Unit,
  onSave: (cat: String, title: String, dur: Int, det: String, cal: Int, inten: String) -> Unit
) {
  var category by remember { mutableStateOf("Gym") }
  var title by remember { mutableStateOf("Chest & Back Progressive Overload") }
  var durationStr by remember { mutableStateOf("45") }
  var details by remember { mutableStateOf("Bench Press 4x8, Lat Pulldowns 4x10, Incline DB Press 3x10") }
  var caloriesStr by remember { mutableStateOf("280") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = CutCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = VoidBlack),
      border = BorderStroke(1.2.dp, ElectricCyan),
      modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("log_workout_dialog")
    ) {
      Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(text = "LOG PHYSICAL TRAINING", style = MaterialTheme.typography.titleMedium, color = ElectricCyan, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Workout Title / Routine") },
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category (Gym/Home/Walk)") },
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
          )
          Spacer(modifier = Modifier.width(8.dp))
          OutlinedTextField(
            value = durationStr,
            onValueChange = { durationStr = it },
            label = { Text("Duration (Mins)") },
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
          )
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = details,
          onValueChange = { details = it },
          label = { Text("Exercises, Sets & Reps") },
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
          TextButton(onClick = onDismiss) { Text("CANCEL", color = TextSecondary) }
          Spacer(modifier = Modifier.width(8.dp))
          GlowButton(
            text = "RECORD TRAINING",
            onClick = {
              onSave(
                category,
                title,
                durationStr.toIntOrNull() ?: 45,
                details,
                caloriesStr.toIntOrNull() ?: 280,
                "Moderate"
              )
            },
            testTag = "submit_workout_button"
          )
        }
      }
    }
  }
}

@Composable
fun LogWeightDialog(
  currentWeight: Float,
  onDismiss: () -> Unit,
  onSave: (wt: Float, waist: Float, notes: String) -> Unit
) {
  var weightStr by remember { mutableStateOf(currentWeight.toString()) }
  var waistStr by remember { mutableStateOf("") }
  var notes by remember { mutableStateOf("Morning weigh-in after hydration") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = CutCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = VoidBlack),
      border = BorderStroke(1.2.dp, ElectricCyan),
      modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("log_weight_dialog")
    ) {
      Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(text = "LOG BODY WEIGHT", style = MaterialTheme.typography.titleMedium, color = ElectricCyan, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = weightStr,
          onValueChange = { weightStr = it },
          label = { Text("Weight in Kilograms (kg)") },
          modifier = Modifier.fillMaxWidth().testTag("weight_input"),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = waistStr,
          onValueChange = { waistStr = it },
          label = { Text("Waist Measurement (cm, optional)") },
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text("Notes / Observations") },
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
          TextButton(onClick = onDismiss) { Text("CANCEL", color = TextSecondary) }
          Spacer(modifier = Modifier.width(8.dp))
          GlowButton(
            text = "SAVE WEIGHT",
            onClick = {
              val w = weightStr.toFloatOrNull() ?: currentWeight
              onSave(w, waistStr.toFloatOrNull() ?: 0f, notes)
            },
            testTag = "submit_weight_button"
          )
        }
      }
    }
  }
}

@Composable
fun AddCustomFoodDialog(
  onDismiss: () -> Unit,
  onSave: (name: String, serv: String, cal: Int, p: Float, c: Float, f: Float, meal: String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var serving by remember { mutableStateOf("1 serving") }
  var caloriesStr by remember { mutableStateOf("200") }
  var proteinStr by remember { mutableStateOf("10") }
  var mealType by remember { mutableStateOf("Lunch") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = CutCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = VoidBlack),
      border = BorderStroke(1.2.dp, ElectricCyan),
      modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("add_food_dialog")
    ) {
      Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(text = "ADD VEGETARIAN FOOD", style = MaterialTheme.typography.titleMedium, color = ElectricCyan, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Food Item Name") },
          modifier = Modifier.fillMaxWidth().testTag("food_name_input"),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = proteinStr,
            onValueChange = { proteinStr = it },
            label = { Text("Protein (g)") },
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
          )
          Spacer(modifier = Modifier.width(8.dp))
          OutlinedTextField(
            value = caloriesStr,
            onValueChange = { caloriesStr = it },
            label = { Text("Calories (kcal)") },
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
          )
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = mealType,
          onValueChange = { mealType = it },
          label = { Text("Meal (Breakfast/Lunch/Dinner/Snacks)") },
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan, unfocusedBorderColor = HologramCardBorder)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
          TextButton(onClick = onDismiss) { Text("CANCEL", color = TextSecondary) }
          Spacer(modifier = Modifier.width(8.dp))
          GlowButton(
            text = "LOG ITEM",
            onClick = {
              if (name.isNotBlank()) {
                onSave(
                  name,
                  serving,
                  caloriesStr.toIntOrNull() ?: 200,
                  proteinStr.toFloatOrNull() ?: 10f,
                  0f,
                  0f,
                  mealType
                )
              }
            },
            testTag = "submit_food_button"
          )
        }
      }
    }
  }
}

@Composable
fun WorkoutGeneratorDialog(
  onDismiss: () -> Unit,
  onGenerate: (equipment: String, duration: Int, muscles: String, rpgClass: String) -> Unit
) {
  var selectedEquipment by remember { mutableStateOf("Full Gym") }
  var selectedDuration by remember { mutableIntStateOf(45) }
  var selectedMuscles by remember { mutableStateOf("Push (Chest/Shoulders/Triceps)") }
  var selectedClass by remember { mutableStateOf("Iron Monarch") }

  val equipmentOptions = listOf("Full Gym", "Bodyweight Only", "Dumbbells & Bands", "Outdoor / Park")
  val durationOptions = listOf(20, 30, 45, 60, 90)
  val muscleOptions = listOf(
    "Full Body",
    "Push (Chest/Shoulders/Triceps)",
    "Pull (Back/Biceps)",
    "Legs & Core",
    "Upper Body"
  )
  val classOptions = listOf(
    "Iron Monarch" to "Strength & Power (90-120s rest)",
    "Shadow Assassin" to "Agility & High Reps (45-60s rest)",
    "Titan Endurance" to "Cardio-Muscular Stamina (60s rest)",
    "Ascendant Calisthenics" to "Pure Bodyweight Mastery"
  )

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = CutCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = VoidBlack),
      border = BorderStroke(1.2.dp, ElectricCyan),
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .testTag("workout_generator_dialog")
    ) {
      val scrollState = rememberScrollState()
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
          .verticalScroll(scrollState)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.FitnessCenter,
            contentDescription = null,
            tint = ElectricCyan,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "RPG WORKOUT GENERATOR",
            style = MaterialTheme.typography.titleMedium,
            color = ElectricCyan,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Select equipment, target duration, and skill tree class to forge a dynamic routine quest.",
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Equipment choices
        Text(text = "AVAILABLE EQUIPMENT", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          items(equipmentOptions) { eq ->
            val isSelected = selectedEquipment == eq
            Surface(
              shape = CutCornerShape(4.dp),
              color = if (isSelected) ElectricCyan.copy(alpha = 0.25f) else HologramCardBg,
              border = BorderStroke(1.dp, if (isSelected) ElectricCyan else HologramCardBorder),
              modifier = Modifier.clickable { selectedEquipment = eq }
            ) {
              Text(
                text = eq.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = if (isSelected) ElectricCyan else TextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Duration choices
        Text(text = "SESSION DURATION", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          durationOptions.forEach { dur ->
            val isSelected = selectedDuration == dur
            Surface(
              shape = CutCornerShape(4.dp),
              color = if (isSelected) MonarchViolet.copy(alpha = 0.25f) else HologramCardBg,
              border = BorderStroke(1.dp, if (isSelected) MonarchViolet else HologramCardBorder),
              modifier = Modifier
                .weight(1f)
                .clickable { selectedDuration = dur }
            ) {
              Text(
                text = "${dur}M",
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) MonarchViolet else TextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(vertical = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Target Muscles
        Text(text = "TARGET MUSCLE GROUP", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
        Spacer(modifier = Modifier.height(6.dp))
        muscleOptions.forEach { m ->
          val isSelected = selectedMuscles == m
          Surface(
            shape = CutCornerShape(4.dp),
            color = if (isSelected) ElectricCyan.copy(alpha = 0.2f) else HologramCardBg,
            border = BorderStroke(1.dp, if (isSelected) ElectricCyan else HologramCardBorder),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { selectedMuscles = m }
              .padding(bottom = 4.dp)
          ) {
            Text(
              text = m,
              style = MaterialTheme.typography.bodySmall,
              color = if (isSelected) ElectricCyan else TextSecondary,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // RPG Skill Tree Class
        Text(text = "SKILL TREE ARCHETYPE", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
        Spacer(modifier = Modifier.height(6.dp))
        classOptions.forEach { (clsName, desc) ->
          val isSelected = selectedClass == clsName
          Surface(
            shape = CutCornerShape(4.dp),
            color = if (isSelected) RankS_Color.copy(alpha = 0.2f) else HologramCardBg,
            border = BorderStroke(1.dp, if (isSelected) RankS_Color else HologramCardBorder),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { selectedClass = clsName }
              .padding(bottom = 4.dp)
          ) {
            Column(modifier = Modifier.padding(8.dp)) {
              Text(
                text = clsName.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) RankS_Color else TextPrimary,
                fontWeight = FontWeight.Bold
              )
              Text(text = desc, style = MaterialTheme.typography.bodySmall, fontSize = 10.sp, color = TextMuted)
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
          TextButton(onClick = onDismiss) { Text("CANCEL", color = TextSecondary) }
          Spacer(modifier = Modifier.width(8.dp))
          GlowButton(
            text = "FORGE ROUTINE QUEST",
            onClick = {
              onGenerate(selectedEquipment, selectedDuration, selectedMuscles, selectedClass)
            },
            testTag = "submit_generate_workout_button"
          )
        }
      }
    }
  }
}

@Composable
fun GeneratedWorkoutPlanCard(
  plan: com.example.data.fitness.GeneratedWorkoutPlan,
  onAccept: () -> Unit,
  onDismiss: () -> Unit,
  onReRoll: () -> Unit
) {
  var isAccepted by remember { mutableStateOf(false) }

  SystemHologramCard(
    borderColor = RankS_Color,
    modifier = Modifier
      .fillMaxWidth()
      .testTag("generated_workout_plan_card")
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Surface(
            shape = CutCornerShape(3.dp),
            color = RankS_Color.copy(alpha = 0.2f),
            border = BorderStroke(0.8.dp, RankS_Color)
          ) {
            Text(
              text = "QUEST FORGED • ${plan.rpgClass.uppercase()}",
              style = MaterialTheme.typography.labelSmall,
              fontSize = 8.sp,
              color = RankS_Color,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = plan.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        }

        IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
          Icon(androidx.compose.material.icons.Icons.Default.Delete, contentDescription = "Dismiss", tint = TextMuted)
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "${plan.equipment} • ${plan.durationMinutes} Mins • ${plan.targetMuscles}",
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary
        )
        Text(
          text = "+${plan.xpReward} XP",
          style = MaterialTheme.typography.labelSmall,
          color = RankS_Color,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Phases & Exercises List
      plan.phases.forEach { phase ->
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(HologramCardBg.copy(alpha = 0.6f), CutCornerShape(4.dp))
            .border(0.6.dp, HologramCardBorder.copy(alpha = 0.5f), CutCornerShape(4.dp))
            .padding(8.dp)
        ) {
          Text(
            text = phase.phaseName,
            style = MaterialTheme.typography.labelSmall,
            color = ElectricCyan,
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          phase.exercises.forEach { ex ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "• ${ex.name}",
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.SemiBold,
                  color = TextPrimary
                )
                Text(
                  text = ex.formNotes,
                  style = MaterialTheme.typography.bodySmall,
                  fontSize = 10.sp,
                  color = TextMuted
                )
              }
              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "${ex.sets} sets × ${ex.reps}",
                  style = MaterialTheme.typography.labelSmall,
                  color = ElectricCyan,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "Rest: ${ex.restSeconds}s",
                  style = MaterialTheme.typography.bodySmall,
                  fontSize = 9.sp,
                  color = RankS_Color
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (isAccepted) {
        Surface(
          shape = CutCornerShape(3.dp),
          color = QuestCompletedColor.copy(alpha = 0.2f),
          border = BorderStroke(0.8.dp, QuestCompletedColor),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = QuestCompletedColor, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "WORKOUT QUEST ACCEPTED & RECORDED TO LOG",
              style = MaterialTheme.typography.labelSmall,
              color = QuestCompletedColor,
              fontWeight = FontWeight.Bold
            )
          }
        }
      } else {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          GlowButton(
            text = "ACCEPT QUEST",
            onClick = {
              onAccept()
              isAccepted = true
            },
            modifier = Modifier.weight(1f),
            testTag = "accept_generated_workout_button"
          )
          GlowButton(
            text = "RE-ROLL",
            onClick = onReRoll,
            isPrimary = false,
            modifier = Modifier.weight(0.5f),
            testTag = "reroll_workout_button"
          )
        }
      }
    }
  }
}

