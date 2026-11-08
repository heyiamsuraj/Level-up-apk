package com.example.data.fitness

data class WorkoutExercise(
  val name: String,
  val sets: Int,
  val reps: String,
  val restSeconds: Int,
  val tier: String, // Tier 1 (Core Lifts), Tier 2 (Skill Branches), Tier 3 (Finisher)
  val formNotes: String
)

data class WorkoutPhase(
  val phaseName: String,
  val description: String,
  val exercises: List<WorkoutExercise>
)

data class GeneratedWorkoutPlan(
  val id: String = System.currentTimeMillis().toString(),
  val title: String,
  val rpgClass: String, // Iron Monarch, Shadow Assassin, Titan Endurance, Ascendant Calisthenics
  val equipment: String,
  val durationMinutes: Int,
  val targetMuscles: String,
  val phases: List<WorkoutPhase>,
  val xpReward: Int,
  val strReward: Int,
  val endReward: Int,
  val vitReward: Int
)

object WorkoutGenerator {

  fun generateRoutine(
    equipment: String, // "Full Gym", "Bodyweight Only", "Dumbbells & Bands", "Outdoor / Park"
    durationMinutes: Int, // 20, 30, 45, 60, 90
    targetMuscles: String, // "Full Body", "Push (Chest/Shoulders/Triceps)", "Pull (Back/Biceps)", "Legs & Core", "Upper Body"
    rpgClass: String // "Iron Monarch", "Shadow Assassin", "Titan Endurance", "Ascendant Calisthenics"
  ): GeneratedWorkoutPlan {
    val isGym = equipment == "Full Gym"
    val isBodyweight = equipment == "Bodyweight Only"

    val title = when (rpgClass) {
      "Iron Monarch" -> "IRON MONARCH: HEAVY COMPOUND ASCENT"
      "Shadow Assassin" -> "SHADOW ASSASSIN: DENSITY & AGILITY PROTOCOL"
      "Titan Endurance" -> "TITAN ENDURANCE: METABOLIC STAMINA TRIAL"
      else -> "ASCENDANT CALISTHENICS: GRAVITY CONQUEST"
    }

    val phases = mutableListOf<WorkoutPhase>()

    // Phase 1: Warmup & Awakening
    phases.add(
      WorkoutPhase(
        phaseName = "PHASE 0: NEURAL AWAKENING & JOINT MOBILITY",
        description = "Activate nervous system, lubricate joint capsules, and elevate core body temperature.",
        exercises = listOf(
          WorkoutExercise(
            name = "Shoulder Dislocations & Thoracic Rotations",
            sets = 2,
            reps = "15 reps",
            restSeconds = 30,
            tier = "Warmup",
            formNotes = "Keep core braced; smooth continuous arc."
          ),
          WorkoutExercise(
            name = "Deep Bodyweight Squat to Hip Pry",
            sets = 2,
            reps = "10 reps",
            restSeconds = 30,
            tier = "Warmup",
            formNotes = "Hold bottom position 3 seconds; chest elevated."
          )
        )
      )
    )

    // Phase 2: Main Compound Quests
    val primaryExercises = when {
      targetMuscles.contains("Push") && isGym -> listOf(
        WorkoutExercise("Barbell Bench Press", 4, "6-8 reps", 120, "Tier 1: Core", "Lower under control (3s eccentric), drive up explosively."),
        WorkoutExercise("Seated Overhead Barbell Military Press", 3, "8-10 reps", 90, "Tier 1: Core", "Full extension overhead without excessive arch.")
      )
      targetMuscles.contains("Push") -> listOf(
        WorkoutExercise("Decline Deficit Push-Ups", 4, "12-15 reps", 60, "Tier 1: Core", "Feet elevated on chair/bench; full chest stretch."),
        WorkoutExercise("Handstand Push-Up Wall Progression / Pike Push-Ups", 4, "8-10 reps", 75, "Tier 1: Core", "Head taps mat gently in tripod position.")
      )
      targetMuscles.contains("Pull") && isGym -> listOf(
        WorkoutExercise("Conventional Barbell Deadlifts / Rack Pulls", 4, "5-6 reps", 150, "Tier 1: Core", "Reset each rep on floor; lats tightly engaged."),
        WorkoutExercise("Chest-Supported T-Bar / Barbell Row", 4, "8-10 reps", 90, "Tier 1: Core", "Squeeze scapulae together at peak contraction.")
      )
      targetMuscles.contains("Pull") -> listOf(
        WorkoutExercise("Dead-Hang Pronated Pull-Ups", 4, "Max clean reps", 90, "Tier 1: Core", "Full dead hang at bottom, chin clearing bar."),
        WorkoutExercise("Inverted Horizontal Rows (Table / Low Bar)", 4, "10-12 reps", 60, "Tier 1: Core", "Body rigid as a plank; drive elbows back.")
      )
      targetMuscles.contains("Legs") && isGym -> listOf(
        WorkoutExercise("Barbell Back Squats", 4, "6-8 reps", 120, "Tier 1: Core", "Break parallel; push knees outward in line with toes."),
        WorkoutExercise("Romanian Deadlifts (RDLs)", 3, "8-10 reps", 90, "Tier 1: Core", "Hinge from hips; feel deep hamstring stretch.")
      )
      targetMuscles.contains("Legs") -> listOf(
        WorkoutExercise("Bulgarian Split Squats (Rear Foot Elevated)", 4, "10-12 reps / leg", 60, "Tier 1: Core", "Keep front shin vertical; torso upright."),
        WorkoutExercise("Explosive Jump Squats into Nordic Curl Negative", 3, "12 reps", 60, "Tier 1: Core", "Soft landing; absorb impact into thighs.")
      )
      else -> {
        // Full Body
        if (isGym) {
          listOf(
            WorkoutExercise("Barbell Front Squats", 4, "6-8 reps", 120, "Tier 1: Core", "Upright torso; elbows high and parallel to ground."),
            WorkoutExercise("Standing Overhead Barbell Press", 3, "8 reps", 90, "Tier 1: Core", "Lock knees and glutes; press vertically.")
          )
        } else {
          listOf(
            WorkoutExercise("Explosive Archer / Diamond Push-Ups", 4, "10-12 reps", 60, "Tier 1: Core", "Strict body tension; touch chest to thumbs."),
            WorkoutExercise("Deep Pistol Squat Progression", 3, "6-8 reps / leg", 75, "Tier 1: Core", "Counterbalance arms forward; knee tracks toe.")
          )
        }
      }
    }

    phases.add(
      WorkoutPhase(
        phaseName = "PHASE 1: MAIN CONFLICT (CORE LIFTS)",
        description = "Highest neural priority exercises. Execute with uncompromising form and progressive weight.",
        exercises = primaryExercises
      )
    )

    // Phase 3: Secondary Skill Tree Exercises (Hypertrophy / Skill branches)
    val secondaryExercises = when {
      targetMuscles.contains("Push") && isGym -> listOf(
        WorkoutExercise("Incline Dumbbell Flyes to Press", 3, "10-12 reps", 60, "Tier 2: Skill", "Constant tension on upper clavicular fibers."),
        WorkoutExercise("Triceps Rope Pushdown Super-set with Lateral Raises", 3, "12-15 reps", 45, "Tier 2: Skill", "Flared tricep extension at lock.")
      )
      targetMuscles.contains("Push") -> listOf(
        WorkoutExercise("Parallel Bar / Chair Dips", 3, "12-15 reps", 60, "Tier 2: Skill", "Lean slightly forward to bias chest; don't shrug shoulders."),
        WorkoutExercise("Tricep Bench Dip Dropset to Floor Hold", 3, "15 reps", 45, "Tier 2: Skill", "Elbows tucked close.")
      )
      targetMuscles.contains("Pull") && isGym -> listOf(
        WorkoutExercise("Neutral-Grip Lat Pulldown", 3, "10-12 reps", 60, "Tier 2: Skill", "Lead with elbows down into back pockets."),
        WorkoutExercise("Incline Dumbbell Bicep Curl & Hammer Curl combo", 3, "10-12 reps", 45, "Tier 2: Skill", "Full stretch at bottom; supinate wrists.")
      )
      targetMuscles.contains("Pull") -> listOf(
        WorkoutExercise("Towel / Doorframe Pulls & Isometric Chin Hang", 3, "Hold 25s", 45, "Tier 2: Skill", "Challenging forearm grip & upper lat isometric."),
        WorkoutExercise("Prone Cobra / Superman Arch Holds", 3, "15 reps", 30, "Tier 2: Skill", "Posterior chain spine erector conditioning.")
      )
      else -> listOf(
        WorkoutExercise("Walking Dumbbell Lunges / Alternating Jump Lunges", 3, "20 total paces", 60, "Tier 2: Skill", "Knee kisses floor gently on each pace."),
        WorkoutExercise("Hanging Knee-to-Elbow Raises / Dragon Flag Holds", 3, "10-12 reps", 45, "Tier 2: Skill", "Posterior pelvic tilt; curl pelvis up.")
      )
    }

    phases.add(
      WorkoutPhase(
        phaseName = "PHASE 2: SKILL TREE ACCESSORIES (HYPERTROPHY)",
        description = "Targeted muscular hypertrophy and structural balance. Minimize momentum; maximize time under tension.",
        exercises = secondaryExercises
      )
    )

    // Phase 4: Finisher / Boss Battle
    phases.add(
      WorkoutPhase(
        phaseName = "PHASE 3: DUNGEON FINISHER (BURNOUT PROTOCOL)",
        description = "Final burst of metabolic output to stimulate mitochondrial density and endurance.",
        exercises = listOf(
          WorkoutExercise(
            name = if (isGym) "Assault Bike / Rowing Machine Sprint or Farmer's Walks" else "Burpees into Hollow-Body Hold",
            sets = 3,
            reps = "30s max effort / 30s rest",
            restSeconds = 30,
            tier = "Tier 3: Finisher",
            formNotes = "Empty the tank; relentless pace until buzzer."
          )
        )
      )
    )

    val xp = (durationMinutes * 1.5f).toInt() + 40
    val str = when (rpgClass) {
      "Iron Monarch" -> 35
      "Ascendant Calisthenics" -> 30
      else -> 20
    }
    val end = if (rpgClass == "Titan Endurance" || rpgClass == "Shadow Assassin") 30 else 20
    val vit = 15

    return GeneratedWorkoutPlan(
      title = title,
      rpgClass = rpgClass,
      equipment = equipment,
      durationMinutes = durationMinutes,
      targetMuscles = targetMuscles,
      phases = phases,
      xpReward = xp,
      strReward = str,
      endReward = end,
      vitReward = vit
    )
  }
}
