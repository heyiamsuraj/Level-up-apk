package com.example.data.db

import com.example.data.model.AchievementEntity
import com.example.data.model.AssignmentEntity
import com.example.data.model.AttributeEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.CollegeTimetableEntity
import com.example.data.model.DashboardConfigEntity
import com.example.data.model.GateSubjectEntity
import com.example.data.model.HabitEntity
import com.example.data.model.PlayerEntity
import com.example.data.model.ProjectMilestoneEntity
import com.example.data.model.QuestEntity
import com.example.data.model.QuoteEntity
import com.example.data.model.UserEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PrepopulateData {

  fun todayDateStr(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
  }

  val defaultUser = UserEntity(
    id = 1L,
    username = "Hunter",
    email = "hunter@system.awakened",
    level = 1,
    currentXp = 0,
    rank = "E-Rank",
    title = "The Awakened Mechanical Engineer",
    collegeTiming = "09:00 - 16:30",
    wakeTime = "06:30",
    sleepTime = "23:00",
    currentWeight = 76.5f,
    targetWeight = 68.0f,
    heightCm = 175f,
    gateStudyTargetHours = 3.5f,
    preparationLevel = "Intermediate",
    streakDays = 1,
    isSystemAwakened = true
  )

  val defaultAttributes = listOf(
    AttributeEntity(code = "STR", name = "Strength", baseValue = 12, hexColor = "#FF3366", description = "Physical power developed through resistance training, calisthenics, and progressive overload."),
    AttributeEntity(code = "AGI", name = "Agility", baseValue = 11, hexColor = "#06D6A0", description = "Speed of execution, task switching, punctual attendance, and swift habit completion."),
    AttributeEntity(code = "INT", name = "Intelligence", baseValue = 14, hexColor = "#00F0FF", description = "Academic mastery, GATE Mechanical Engineering syllabus completion, and numerical precision."),
    AttributeEntity(code = "VIT", name = "Vitality", baseValue = 12, hexColor = "#9D4EDD", description = "Health, hydration, vegetarian nutrition, recovery, and consistent 7-8 hours sleep hygiene."),
    AttributeEntity(code = "END", name = "Endurance", baseValue = 10, hexColor = "#FFB703", description = "Mental and cardiovascular stamina during 3+ hour mock tests and lengthy college project blocks."),
    AttributeEntity(code = "FOC", name = "Focus", baseValue = 13, hexColor = "#0077FE", description = "Unbroken concentration in Dungeon Pomodoro sessions without distraction."),
    AttributeEntity(code = "DIS", name = "Discipline", baseValue = 11, hexColor = "#FFD166", description = "Daily streak continuity, consistency over perfection, and honoring daily contracts.")
  )

  val initialChatMessage = ChatMessageEntity(
    sender = "model",
    message = "[SYSTEM INITIALIZATION COMPLETE]\n\nGreetings, Hunter. I am your Awakened System AI Guide.\n\nYour real life is now your Main Quest. I have loaded your B.E. Mechanical Engineering syllabus, GATE target milestones, and fitness parameters.\n\nYou may consult me anytime for:\n• High-yield GATE Mechanical problem solving & conceptual derivations\n• Hostel-friendly vegetarian nutrition & progressive overload training\n• College assignment deadlines & B.E. project scheduling\n• Daily discipline & focus tactics\n\nCommand your directive.",
    roleName = "System Guide",
    modelName = "gemini-3.8-flash"
  )

  val defaultPlayer = PlayerEntity(
    id = 1,
    name = "Hunter",
    level = 1,
    currentXp = 0,
    rank = "E-Rank",
    str = 12,
    intVal = 14,
    vit = 12,
    dis = 11,
    endVal = 10,
    foc = 13,
    streakDays = 1,
    currentWeight = 76.5f,
    targetWeight = 68.0f,
    heightCm = 175f,
    gateStudyTargetHours = 3.5f,
    collegeTiming = "09:00 - 16:30",
    wakeTime = "06:30",
    sleepTime = "23:00",
    isOnboarded = false,
    planningMode = "Normal College Day",
    targetLevel = "Target Day"
  )

  val defaultDashboardConfig = DashboardConfigEntity(
    id = 1,
    themePreset = "SYSTEM_DEFAULT",
    wallpaperChoice = "bg_dungeon_gate",
    customWallpaperUri = "",
    accentColorHex = "#00F0FF",
    overlayDimming = 0.65f,
    blurRadius = 0f,
    showSurvivalQuestWidget = true,
    showAttributeWidget = true,
    showGateWidget = true,
    showCollegeWidget = true,
    showFitnessWidget = true,
    showQuoteWidget = true
  )

  fun getDefaultQuests(dateStr: String): List<QuestEntity> = listOf(
    // Main Quests
    QuestEntity(
      title = "Attend College Classes & Lab Sessions",
      description = "Attend all scheduled Mechanical Engineering lectures and manufacturing lab. Timetable adherence.",
      category = "College",
      priority = "High",
      difficulty = "D",
      xpReward = 45,
      disReward = 20,
      intReward = 10,
      isMainQuest = true,
      dateStr = dateStr,
      startTime = "09:00",
      deadline = "16:30",
      isSurvivalQuestItem = true
    ),
    QuestEntity(
      title = "GATE ME: Thermodynamics & Heat Transfer Deep Work",
      description = "Solve 20 Previous Year Questions (PYQs) and revise entropy/exergy formulas.",
      category = "GATE",
      priority = "Urgent",
      difficulty = "C",
      xpReward = 75,
      intReward = 50,
      focReward = 25,
      isMainQuest = true,
      dateStr = dateStr,
      startTime = "17:30",
      deadline = "19:45",
      isSurvivalQuestItem = true
    ),
    QuestEntity(
      title = "Final-Year B.E. Project: FEA / CAD Simulation Progress",
      description = "Refine 3D geometry mesh and run static structural stress distribution simulations.",
      category = "Project",
      priority = "High",
      difficulty = "D",
      xpReward = 50,
      intReward = 25,
      disReward = 20,
      isMainQuest = true,
      dateStr = dateStr,
      startTime = "20:00",
      deadline = "21:15",
      isSurvivalQuestItem = true
    ),
    QuestEntity(
      title = "Daily Physical Training: Strength & Cardio",
      description = "Complete 40-minute resistance training or brisk walking in campus/hostel grounds.",
      category = "Fitness",
      priority = "Medium",
      difficulty = "D",
      xpReward = 50,
      strReward = 30,
      endReward = 20,
      isMainQuest = true,
      dateStr = dateStr,
      startTime = "06:45",
      deadline = "07:45",
      isSurvivalQuestItem = true
    ),
    QuestEntity(
      title = "Adequate Sleep Recovery Protocol",
      description = "Wind down screen-free at 22:45. Target 7.5 hours of restorative sleep.",
      category = "Health",
      priority = "High",
      difficulty = "E",
      xpReward = 30,
      vitReward = 30,
      isMainQuest = true,
      dateStr = dateStr,
      startTime = "23:00",
      deadline = "06:30",
      isSurvivalQuestItem = true
    ),
    // Side Quests
    QuestEntity(
      title = "Hydration Protocol: 3 Liters Water",
      description = "Drink pure water consistently throughout lectures, study blocks, and workouts.",
      category = "Nutrition",
      priority = "Medium",
      difficulty = "E",
      xpReward = 25,
      vitReward = 20,
      isMainQuest = false,
      dateStr = dateStr,
      isSurvivalQuestItem = true
    ),
    QuestEntity(
      title = "Vegetarian Protein Goal: 80g+",
      description = "Include paneer, soya chunks, dal, curd, or sprouts across hostel meals.",
      category = "Nutrition",
      priority = "Medium",
      difficulty = "E",
      xpReward = 30,
      vitReward = 20,
      strReward = 15,
      isMainQuest = false,
      dateStr = dateStr
    ),
    QuestEntity(
      title = "Quick Formula Sheet Flash Revision",
      description = "15-minute active recall review of Strength of Materials Mohr's circle and deflection formulas.",
      category = "GATE",
      priority = "Low",
      difficulty = "E",
      xpReward = 25,
      intReward = 20,
      focReward = 10,
      isMainQuest = false,
      dateStr = dateStr
    ),
    QuestEntity(
      title = "Evening Reflection & Next Day Planning",
      description = "Reflect on today's progress, verify incomplete quests, and set tomorrow's focus priorities.",
      category = "Habit",
      priority = "Low",
      difficulty = "E",
      xpReward = 20,
      disReward = 15,
      isMainQuest = false,
      dateStr = dateStr
    )
  )

  val gateSubjects = listOf(
    GateSubjectEntity(
      name = "Engineering Mathematics",
      code = "EM",
      weightageMarks = 13.0f,
      totalTopics = 22,
      completedTopics = 9,
      pyqsSolved = 65,
      pyqTarget = 150,
      notes = "Linear Algebra, Calculus, Diff Equations, Complex Variables, Probability & Statistics, Numerical Methods.",
      isWeakArea = false
    ),
    GateSubjectEntity(
      name = "Strength of Materials",
      code = "SOM",
      weightageMarks = 10.0f,
      totalTopics = 18,
      completedTopics = 11,
      pyqsSolved = 90,
      pyqTarget = 160,
      notes = "Stress & Strain, Mohr's circle, Thin Cylinders, Bending & Shear stresses, Deflection, Columns.",
      isWeakArea = false
    ),
    GateSubjectEntity(
      name = "Thermodynamics",
      code = "TD",
      weightageMarks = 9.0f,
      totalTopics = 16,
      completedTopics = 10,
      pyqsSolved = 82,
      pyqTarget = 140,
      notes = "Zeroth, First & Second Law, Entropy, Exergy/Availability, Pure Substances, Property Relations.",
      isWeakArea = false
    ),
    GateSubjectEntity(
      name = "Fluid Mechanics & Turbomachinery",
      code = "FM",
      weightageMarks = 8.5f,
      totalTopics = 20,
      completedTopics = 7,
      pyqsSolved = 45,
      pyqTarget = 150,
      notes = "Fluid Kinematics, Dynamics, Bernoulli, Boundary Layer, Pipe Flow, Turbines and Pumps.",
      isWeakArea = true
    ),
    GateSubjectEntity(
      name = "Heat Transfer",
      code = "HT",
      weightageMarks = 7.5f,
      totalTopics = 15,
      completedTopics = 8,
      pyqsSolved = 55,
      pyqTarget = 120,
      notes = "Conduction (Fourier), Fins, Transient, Convection (Nu, Pr, Re relations), Radiation, Heat Exchangers (LMTD & NTU).",
      isWeakArea = false
    ),
    GateSubjectEntity(
      name = "Theory of Machines & Vibrations",
      code = "TOM",
      weightageMarks = 8.0f,
      totalTopics = 18,
      completedTopics = 6,
      pyqsSolved = 40,
      pyqTarget = 130,
      notes = "Kinematic linkages, Velocity & Acceleration analysis, Cams, Gears & Gear Trains, Flywheel, Governors, Balancing, Vibrations.",
      isWeakArea = true
    ),
    GateSubjectEntity(
      name = "Manufacturing Engineering",
      code = "ME",
      weightageMarks = 14.0f,
      totalTopics = 26,
      completedTopics = 12,
      pyqsSolved = 110,
      pyqTarget = 200,
      notes = "Casting, Metal Forming, Welding, Machining & Tool Life (Taylor), Metrology, CNC, Additive Mfg.",
      isWeakArea = false
    ),
    GateSubjectEntity(
      name = "Industrial Engineering & OR",
      code = "IE",
      weightageMarks = 6.0f,
      totalTopics = 14,
      completedTopics = 8,
      pyqsSolved = 50,
      pyqTarget = 100,
      notes = "Forecasting, Inventory (EOQ), CPM/PERT, Linear Programming (Simplex), Queuing Theory, Transportation.",
      isWeakArea = false
    ),
    GateSubjectEntity(
      name = "Applied Thermodynamics",
      code = "ATD",
      weightageMarks = 6.0f,
      totalTopics = 16,
      completedTopics = 5,
      pyqsSolved = 35,
      pyqTarget = 110,
      notes = "Rankine Cycle, Gas Turbines (Brayton), IC Engines, Steam Turbines, Compressors.",
      isWeakArea = false
    ),
    GateSubjectEntity(
      name = "Refrigeration and Air Conditioning",
      code = "RAC",
      weightageMarks = 4.0f,
      totalTopics = 10,
      completedTopics = 4,
      pyqsSolved = 28,
      pyqTarget = 80,
      notes = "Vapor Compression (VCRS), Vapor Absorption, Psychrometric Charts & Air Conditioning Processes.",
      isWeakArea = false
    ),
    GateSubjectEntity(
      name = "Machine Design",
      code = "MD",
      weightageMarks = 5.0f,
      totalTopics = 12,
      completedTopics = 5,
      pyqsSolved = 30,
      pyqTarget = 90,
      notes = "Static & Dynamic failure theories, Fatigue (S-N curve, Goodman, Soderberg), Shafts, Keys, Bearings, Clutches, Brakes.",
      isWeakArea = false
    ),
    GateSubjectEntity(
      name = "Engineering Mechanics",
      code = "EMech",
      weightageMarks = 4.0f,
      totalTopics = 12,
      completedTopics = 7,
      pyqsSolved = 42,
      pyqTarget = 90,
      notes = "Free Body Diagrams, Equilibrium, Trusses, Friction, Virtual Work, Kinematics & Kinetics of Rigid Bodies.",
      isWeakArea = false
    ),
    GateSubjectEntity(
      name = "Materials Science",
      code = "MS",
      weightageMarks = 3.0f,
      totalTopics = 10,
      completedTopics = 4,
      pyqsSolved = 25,
      pyqTarget = 70,
      notes = "Crystal Structures (FCC, BCC, HCP), Phase Diagrams (Fe-C), Heat Treatment (Annealing, Quenching, TTT curve), Stress-Strain behaviors.",
      isWeakArea = false
    )
  )

  val defaultTimetable = listOf(
    CollegeTimetableEntity(dayOfWeek = "Monday", subject = "Power Plant Engineering", timeSlot = "09:30 - 10:30", room = "Room 302", attendedCount = 20, totalClassesCount = 24),
    CollegeTimetableEntity(dayOfWeek = "Monday", subject = "Advanced Manufacturing Lab", timeSlot = "10:30 - 12:30", room = "Workshop B", attendedCount = 11, totalClassesCount = 12),
    CollegeTimetableEntity(dayOfWeek = "Monday", subject = "Automobile Engineering", timeSlot = "14:00 - 15:00", room = "Room 304", attendedCount = 19, totalClassesCount = 22),
    CollegeTimetableEntity(dayOfWeek = "Tuesday", subject = "Operations Research", timeSlot = "09:30 - 10:30", room = "Room 301", attendedCount = 21, totalClassesCount = 23),
    CollegeTimetableEntity(dayOfWeek = "Tuesday", subject = "CAD/CAM Lab", timeSlot = "10:30 - 12:30", room = "Computer Center 2", attendedCount = 12, totalClassesCount = 12),
    CollegeTimetableEntity(dayOfWeek = "Tuesday", subject = "Power Plant Engineering", timeSlot = "14:00 - 15:00", room = "Room 302", attendedCount = 18, totalClassesCount = 22),
    CollegeTimetableEntity(dayOfWeek = "Wednesday", subject = "Automobile Engineering", timeSlot = "09:30 - 10:30", room = "Room 304", attendedCount = 20, totalClassesCount = 23),
    CollegeTimetableEntity(dayOfWeek = "Wednesday", subject = "Operations Research", timeSlot = "10:30 - 11:30", room = "Room 301", attendedCount = 19, totalClassesCount = 21),
    CollegeTimetableEntity(dayOfWeek = "Wednesday", subject = "Project Work & Guide Meeting", timeSlot = "14:00 - 16:30", room = "Design Dept", attendedCount = 8, totalClassesCount = 8),
    CollegeTimetableEntity(dayOfWeek = "Thursday", subject = "Power Plant Engineering", timeSlot = "09:30 - 10:30", room = "Room 302", attendedCount = 19, totalClassesCount = 23),
    CollegeTimetableEntity(dayOfWeek = "Thursday", subject = "Thermal Engineering Lab", timeSlot = "10:30 - 12:30", room = "Heat Engines Lab", attendedCount = 11, totalClassesCount = 12),
    CollegeTimetableEntity(dayOfWeek = "Thursday", subject = "Automobile Engineering", timeSlot = "14:00 - 15:00", room = "Room 304", attendedCount = 20, totalClassesCount = 23),
    CollegeTimetableEntity(dayOfWeek = "Friday", subject = "Operations Research", timeSlot = "09:30 - 10:30", room = "Room 301", attendedCount = 20, totalClassesCount = 22),
    CollegeTimetableEntity(dayOfWeek = "Friday", subject = "Seminar & Technical Presentation", timeSlot = "10:30 - 12:30", room = "Seminar Hall 1", attendedCount = 10, totalClassesCount = 10),
    CollegeTimetableEntity(dayOfWeek = "Friday", subject = "Library / GATE Self Study", timeSlot = "14:00 - 16:30", room = "Central Library", attendedCount = 15, totalClassesCount = 15),
    CollegeTimetableEntity(dayOfWeek = "Saturday", subject = "GATE Full Length Mock Test", timeSlot = "09:30 - 12:30", room = "Study Desk", attendedCount = 7, totalClassesCount = 8)
  )

  val defaultProjectMilestones = listOf(
    ProjectMilestoneEntity(title = "Stage 1: Exhaustive Literature Review & Problem Formulation", stage = "Literature Review", deadlineDate = "2026-09-15", isCompleted = true, notes = "Analyzed 18 IEEE/Elsevier research papers on composite laminate stress concentration.", orderIndex = 1),
    ProjectMilestoneEntity(title = "Stage 2: Methodology & Mathematical Model Selection", stage = "Methodology", deadlineDate = "2026-10-10", isCompleted = true, notes = "Selected First-order Shear Deformation Theory (FSDT) and validated boundary constraints.", orderIndex = 2),
    ProjectMilestoneEntity(title = "Stage 3: CAD Modeling & Mesh Independence Study", stage = "Experimental Work", deadlineDate = "2026-11-20", isCompleted = true, notes = "Created ANSYS / SolidWorks parametric geometry; convergence achieved at 0.5mm element size.", orderIndex = 3),
    ProjectMilestoneEntity(title = "Stage 4: Experimental Testing & Finite Element Data Collection", stage = "Data Collection", deadlineDate = "2026-12-15", isCompleted = false, notes = "Record strain gauge readouts and compare with Von-Mises FEA predictions.", orderIndex = 4),
    ProjectMilestoneEntity(title = "Stage 5: Data Analysis & Statistical Error Quantification", stage = "Analysis", deadlineDate = "2027-01-10", isCompleted = false, notes = "Evaluate discrepancies between simulated vs experimental tensile yield points.", orderIndex = 5),
    ProjectMilestoneEntity(title = "Stage 6: Final Results Synthesis & Optimization Insights", stage = "Results", deadlineDate = "2027-01-25", isCompleted = false, notes = "Graph topological weight reduction vs factor of safety curves.", orderIndex = 6),
    ProjectMilestoneEntity(title = "Stage 7: Thesis Documentation & Plagiarism Clearance", stage = "Documentation", deadlineDate = "2027-02-15", isCompleted = false, notes = "Format thesis according to university guidelines; run Turnitin similarity check.", orderIndex = 7),
    ProjectMilestoneEntity(title = "Stage 8: Final Viva Voce & External Defense Presentation", stage = "Presentation", deadlineDate = "2027-03-05", isCompleted = false, notes = "Prepare 25-slide concise defense presentation with live prototype/simulation demo.", orderIndex = 8)
  )

  val defaultAssignments = listOf(
    AssignmentEntity(subject = "Power Plant Eng.", title = "Supercritical Rankine cycle heat balance sheet calculation", deadlineDate = "2026-11-18", isSubmitted = false, priority = "High"),
    AssignmentEntity(subject = "Operations Research", title = "Dual Simplex & Transportation Vogel Approximation method problems", deadlineDate = "2026-11-22", isSubmitted = false, priority = "Medium"),
    AssignmentEntity(subject = "Automobile Eng.", title = "Electronic Fuel Injection (EFI) vs CRDI comparative report", deadlineDate = "2026-11-28", isSubmitted = false, priority = "Low")
  )

  val defaultHabits = listOf(
    HabitEntity(title = "Wake up at 06:30 AM without snooze", category = "Discipline", attributeAffected = "DIS", xpReward = 20),
    HabitEntity(title = "Attend 100% of today's college lectures", category = "Discipline", attributeAffected = "DIS", xpReward = 25),
    HabitEntity(title = "Complete 3+ Hours GATE Focused Study", category = "Study", attributeAffected = "INT", xpReward = 35),
    HabitEntity(title = "Physical workout or 8,000 brisk steps", category = "Fitness", attributeAffected = "STR", xpReward = 30),
    HabitEntity(title = "Drink 3 Liters of Water", category = "Health", attributeAffected = "VIT", xpReward = 20),
    HabitEntity(title = "15-minute formula flashcard revision", category = "Study", attributeAffected = "FOC", xpReward = 20),
    HabitEntity(title = "Mindful vegetarian protein-rich nutrition", category = "Health", attributeAffected = "VIT", xpReward = 20),
    HabitEntity(title = "Zero mindless short-form scrolling before bed", category = "Discipline", attributeAffected = "DIS", xpReward = 25),
    HabitEntity(title = "In bed lights out by 23:00", category = "Health", attributeAffected = "VIT", xpReward = 25)
  )

  val defaultAchievements = listOf(
    AchievementEntity(id = "awakening", title = "THE SYSTEM AWAKENS", description = "Completed player onboarding and took the first step on the path of real-life ascension.", rankRequirement = "E-Rank", category = "Awakening", xpReward = 100, isUnlocked = true, unlockedDate = "2026-11-07", progressCurrent = 1, progressTarget = 1),
    AchievementEntity(id = "first_quest", title = "First Dungeon Cleared", description = "Completed your very first main quest with verified focus.", rankRequirement = "E-Rank", category = "Quest", xpReward = 50, isUnlocked = false, progressCurrent = 0, progressTarget = 1),
    AchievementEntity(id = "grind_10h", title = "The Grind Begins", description = "Logged 10 total hours of deep engineering & GATE preparation.", rankRequirement = "E-Rank", category = "Study", xpReward = 150, isUnlocked = false, progressCurrent = 4, progressTarget = 10),
    AchievementEntity(id = "pyq_100", title = "Knowledge Hunter", description = "Accurately solved and cataloged 100 GATE Mechanical PYQs.", rankRequirement = "D-Rank", category = "Study", xpReward = 200, isUnlocked = false, progressCurrent = 35, progressTarget = 100),
    AchievementEntity(id = "streak_7", title = "Iron Will", description = "Maintained a continuous 7-day habit discipline streak.", rankRequirement = "D-Rank", category = "Discipline", xpReward = 250, isUnlocked = false, progressCurrent = 2, progressTarget = 7),
    AchievementEntity(id = "workout_20", title = "Shadow Monarch's Physique", description = "Completed 20 physical training sessions with progressive overload.", rankRequirement = "C-Rank", category = "Fitness", xpReward = 300, isUnlocked = false, progressCurrent = 6, progressTarget = 20),
    AchievementEntity(id = "dungeon_10", title = "Dungeon Master", description = "Completed 10 distraction-free Focus Dungeon training timers.", rankRequirement = "C-Rank", category = "Study", xpReward = 250, isUnlocked = false, progressCurrent = 3, progressTarget = 10),
    AchievementEntity(id = "syllabus_50", title = "GATE ME Scholar", description = "Completed 50% of the comprehensive GATE Mechanical syllabus topics.", rankRequirement = "B-Rank", category = "Study", xpReward = 500, isUnlocked = false, progressCurrent = 38, progressTarget = 50),
    AchievementEntity(id = "attendance_85", title = "Academic Titan", description = "Maintained an attendance record above 85% across all college courses.", rankRequirement = "B-Rank", category = "Discipline", xpReward = 350, isUnlocked = false, progressCurrent = 82, progressTarget = 85),
    AchievementEntity(id = "weight_loss_goal", title = "Transformation Protocol", description = "Reduced weight sustainably toward target while gaining lean strength.", rankRequirement = "A-Rank", category = "Fitness", xpReward = 600, isUnlocked = false, progressCurrent = 3, progressTarget = 8),
    AchievementEntity(id = "project_master", title = "Master of Innovation", description = "Completed final-year project experimental analysis and thesis writeup.", rankRequirement = "A-Rank", category = "Study", xpReward = 800, isUnlocked = false, progressCurrent = 3, progressTarget = 8),
    AchievementEntity(id = "s_rank_ascent", title = "S-Rank Awakening", description = "Ascended to S-Rank hunter status through unrelenting daily mastery.", rankRequirement = "S-Rank", category = "Awakening", xpReward = 1500, isUnlocked = false, progressCurrent = 1, progressTarget = 40)
  )

  // 100+ Original Motivational Messages across 9 rich categories
  val motivationalQuotes = listOf(
    // 1. Discipline
    QuoteEntity(text = "Discipline is choosing between what you want now and what you want most.", author = "System Axiom", category = "Discipline"),
    QuoteEntity(text = "When motivation fades, discipline steps forward to carry the burden.", author = "Hunter Doctrine", category = "Discipline"),
    QuoteEntity(text = "The pain of discipline weighs ounces; the pain of regret weighs tons.", author = "Monarch Creed", category = "Discipline"),
    QuoteEntity(text = "You do not rise to the level of your goals. You fall to the level of your systems.", author = "System Core", category = "Discipline"),
    QuoteEntity(text = "Small disciplines repeated with consistency every day lead to great achievements gained slowly over time.", author = "Daily Directive", category = "Discipline"),
    QuoteEntity(text = "Discipline is the bridge between goals and accomplishment.", author = "System Axiom", category = "Discipline"),
    QuoteEntity(text = "Do what must be done, especially when every fiber in your body pleads for comfort.", author = "Hunter Oath", category = "Discipline"),
    QuoteEntity(text = "True freedom begins the moment you master your impulses.", author = "System Protocol", category = "Discipline"),
    QuoteEntity(text = "Your daily routine is your greatest weapon in the dungeon of life.", author = "Solo Leveling Protocol", category = "Discipline"),
    QuoteEntity(text = "The warrior who conquers his morning has already conquered the battlefield.", author = "Hunter Creed", category = "Discipline"),
    QuoteEntity(text = "Consistency does not require perfection; it requires showing up when nobody is watching.", author = "System Guide", category = "Discipline"),
    QuoteEntity(text = "Excuses will always be available. Opportunities to forge greatness will not.", author = "System Axiom", category = "Discipline"),

    // 2. GATE Preparation & Intelligence
    QuoteEntity(text = "Every derivation mastered and every PYQ solved is an attribute point carved into your intellect.", author = "GATE Commander", category = "GATE Prep"),
    QuoteEntity(text = "Thermodynamics teaches that entropy always increases unless work is done. Put in the work.", author = "Engineering Law", category = "GATE Prep"),
    QuoteEntity(text = "A top GATE rank is not born from genius; it is forged across thousands of solved numericals.", author = "GATE ME Protocol", category = "GATE Prep"),
    QuoteEntity(text = "Master your fundamental concepts. Tough exam questions are simply basic concepts woven in disguise.", author = "GATE Mentor", category = "GATE Prep"),
    QuoteEntity(text = "Do not fear difficult problems; fear the habit of giving up before testing your limits.", author = "System Intellect", category = "GATE Prep"),
    QuoteEntity(text = "Formula sheets are maps, but solving PYQs is how you walk the terrain.", author = "GATE Strategy", category = "GATE Prep"),
    QuoteEntity(text = "In Strength of Materials, materials bend before they fracture. Be like high-yield steel: resilient and ductile under immense stress.", author = "Mechanical Wisdom", category = "GATE Prep"),
    QuoteEntity(text = "Fluid mechanics reminds us: find the path of least resistance in calculation, but never in effort.", author = "Engineering Law", category = "GATE Prep"),
    QuoteEntity(text = "Every mistake made during mock test practice is a mark saved on exam day.", author = "Test Analytics", category = "GATE Prep"),
    QuoteEntity(text = "The mind that tackles calculus every morning views ordinary obstacles as trivial.", author = "System Directive", category = "GATE Prep"),
    QuoteEntity(text = "Consistency in revision converts volatile memory into permanent muscle reflex.", author = "GATE Protocol", category = "GATE Prep"),
    QuoteEntity(text = "Do not wait for inspiration to study. Sit down, open your notes, and let the momentum build.", author = "Intellect Hunter", category = "GATE Prep"),

    // 3. Academic Success & Project Mastery
    QuoteEntity(text = "Your final-year engineering project is your thesis to the world that you can turn ideas into reality.", author = "Academic Council", category = "Academic Success"),
    QuoteEntity(text = "Classroom attendance is not passive presence; it is an active investment in your engineering foundation.", author = "Dean's Principle", category = "Academic Success"),
    QuoteEntity(text = "Great engineers are not distinguished by never failing, but by diagnosing failure with precision.", author = "Engineering Creed", category = "Academic Success"),
    QuoteEntity(text = "Step by step, line by line, simulation by simulation: the final submission will stand tall.", author = "Project Protocol", category = "Academic Success"),
    QuoteEntity(text = "Respect the engineering process. Good design requires iteration, testing, and patient refinement.", author = "System Architect", category = "Academic Success"),
    QuoteEntity(text = "Every assignment submitted on time is a testament to your reliability as a professional.", author = "Academic Axiom", category = "Academic Success"),
    QuoteEntity(text = "Knowledge is the only asset that multiplies when applied to challenging problems.", author = "Scholar Directive", category = "Academic Success"),
    QuoteEntity(text = "Turn your academic syllabus into a quest log and conquer each module with intent.", author = "System Guide", category = "Academic Success"),
    QuoteEntity(text = "The lab is where theory meets raw physical truth. Pay attention to the details.", author = "Mechanical Engineer", category = "Academic Success"),
    QuoteEntity(text = "Exams test your preparation, but your projects prove your ingenuity.", author = "Engineering Oath", category = "Academic Success"),
    QuoteEntity(text = "Approach complex engineering systems by deconstructing them into solvable elements.", author = "System Logic", category = "Academic Success"),

    // 4. Fitness & Weight Loss
    QuoteEntity(text = "Your body is the avatar through which you conquer the world. Upgrade it relentlessly.", author = "Physical System", category = "Fitness"),
    QuoteEntity(text = "Weight loss is not a sprint of starvation; it is a marathon of mindful nutritional discipline.", author = "Vitality Core", category = "Fitness"),
    QuoteEntity(text = "The iron never lies. Two hundred pounds will always weigh two hundred pounds. It tells you exactly who you are.", author = "Strength Hunter", category = "Fitness"),
    QuoteEntity(text = "Sweat is just your old self leaving room for the new hunter to emerge.", author = "Training Grounds", category = "Fitness"),
    QuoteEntity(text = "Choose wholesome plant-based nourishment. Fuel your cells like high-octane engineering machinery.", author = "Vitality Directive", category = "Fitness"),
    QuoteEntity(text = "Even a 30-minute walk through hostel corridors beats an hour of sedentary regret.", author = "Endurance Protocol", category = "Fitness"),
    QuoteEntity(text = "Progress on the scale may fluctuate, but the strength built into your sinews never disappears.", author = "Fitness System", category = "Fitness"),
    QuoteEntity(text = "You do not need an elite gym to build an elite will. Bodyweight and gravity are free.", author = "Calisthenics Hunter", category = "Fitness"),
    QuoteEntity(text = "Hydration is the silent multiplier of every metabolic reaction in your physique.", author = "Vitality Core", category = "Fitness"),
    QuoteEntity(text = "Respect your recovery. Muscles are broken down in training, nourished by food, and rebuilt in sleep.", author = "Recovery Protocol", category = "Fitness"),
    QuoteEntity(text = "One clean meal will not transform you, but 100 clean meals will change your life.", author = "Nutrition Axiom", category = "Fitness"),
    QuoteEntity(text = "Every rep fought for when fatigue whispers to quit is an attribute point added to Strength.", author = "Hunter Training", category = "Fitness"),

    // 5. Confidence & Mindset
    QuoteEntity(text = "You are not an E-Rank by destiny; you are an E-Rank only until you decide to awaken.", author = "Solo Leveling Reflection", category = "Confidence"),
    QuoteEntity(text = "Doubt kills more dreams than failure ever will.", author = "Monarch Principle", category = "Confidence"),
    QuoteEntity(text = "The secret of becoming powerful is realizing nobody is going to save you. You must save yourself.", author = "Awakening Directive", category = "Confidence"),
    QuoteEntity(text = "Stand upright with your shoulders back. You have conquered harder days than today.", author = "Hunter Posture", category = "Confidence"),
    QuoteEntity(text = "Fear is just data. Acknowledge the danger, calculate the strategy, and advance.", author = "System Guide", category = "Confidence"),
    QuoteEntity(text = "Do not seek approval from those who never dared to step into the dungeon.", author = "Solo Creed", category = "Confidence"),
    QuoteEntity(text = "The strongest weapon in your arsenal is an unshakeable belief in your ability to learn.", author = "Intellect Hunter", category = "Confidence"),
    QuoteEntity(text = "Comparison is the thief of your unique progression curve. Compete only against yesterday's self.", author = "System Axiom", category = "Confidence"),
    QuoteEntity(text = "When the system assigns you a difficult quest, it means your level is high enough to clear it.", author = "System Directive", category = "Confidence"),
    QuoteEntity(text = "Wear your humble beginnings like armor, so nothing can ever pierce your pride.", author = "Hunter Legacy", category = "Confidence"),
    QuoteEntity(text = "Quiet confidence outperforms loud arrogance in every high-stakes challenge.", author = "Monarch Mind", category = "Confidence"),

    // 6. Consistency
    QuoteEntity(text = "Consistency over perfection. A 70% day done beats a 100% day planned but postponed.", author = "System Philosophy", category = "Consistency"),
    QuoteEntity(text = "The river cuts through rock not because of its sheer force, but because of its persistence.", author = "Natural Law", category = "Consistency"),
    QuoteEntity(text = "Show up on the days when it is raining, when you are tired, and when progress seems invisible.", author = "Hunter Code", category = "Consistency"),
    QuoteEntity(text = "The compounding effect of 1% daily improvement yields a 37-fold elevation in one year.", author = "Mathematical Truth", category = "Consistency"),
    QuoteEntity(text = "Do not break the chain. If you miss once, treat it as an anomaly; never miss twice.", author = "Habit Directive", category = "Consistency"),
    QuoteEntity(text = "Success is nothing more than a few simple disciplines practiced every single day.", author = "Daily Protocol", category = "Consistency"),
    QuoteEntity(text = "The most dangerous illusion is that someday conditions will be ideal. Start now.", author = "System Prompt", category = "Consistency"),
    QuoteEntity(text = "Routine creates freedom. When basics become automatic, energy is freed for brilliance.", author = "System Logic", category = "Consistency"),
    QuoteEntity(text = "Stack small wins until they form an insurmountable fortress of capability.", author = "Hunter Progression", category = "Consistency"),
    QuoteEntity(text = "The difference between an amateur and a master is that the master enjoys the daily repetition.", author = "Craftsman Creed", category = "Consistency"),
    QuoteEntity(text = "Your streak is not just a number on a screen; it is evidence of an iron character.", author = "Discipline Tracker", category = "Consistency"),

    // 7. Resilience
    QuoteEntity(text = "You were not born to remain at the bottom. Fall down seven times, stand up eight.", author = "Ancient Proverb", category = "Resilience"),
    QuoteEntity(text = "Rock bottom became the solid foundation upon which I rebuilt my life.", author = "Hunter Awakening", category = "Resilience"),
    QuoteEntity(text = "When the dungeon ceiling crumbles around you, keep moving toward the exit.", author = "Raid Protocol", category = "Resilience"),
    QuoteEntity(text = "A wounded hunter who survives becomes the fiercest warrior in the next raid.", author = "Battle Wisdom", category = "Resilience"),
    QuoteEntity(text = "Turn every setback into an engineering case study for future prevention.", author = "System Diagnostics", category = "Resilience"),
    QuoteEntity(text = "Exhaustion is temporary. Quitting is a stain that lingers.", author = "Hunter Oath", category = "Resilience"),
    QuoteEntity(text = "No storm lasts forever. Hold the line until the sky clears.", author = "System Guidance", category = "Resilience"),
    QuoteEntity(text = "The darkest hour of the night is always just before dawn.", author = "Universal Law", category = "Resilience"),
    QuoteEntity(text = "Scars are proof that you survived what was meant to destroy you.", author = "Warrior Truth", category = "Resilience"),
    QuoteEntity(text = "Do not pray for an easy life; pray for the strength to endure a difficult one.", author = "Timeless Wisdom", category = "Resilience"),
    QuoteEntity(text = "Resilience is muscle memory for the soul.", author = "System Core", category = "Resilience"),

    // 8. Solo Leveling & Anime-Inspired Wisdom
    QuoteEntity(text = "The weak may remain prey, or they can level up until they become the hunters.", author = "Solo Leveling Protocol", category = "Solo Leveling"),
    QuoteEntity(text = "I will protect my tomorrow with the blood and sweat of today.", author = "Hunter Awakening", category = "Solo Leveling"),
    QuoteEntity(text = "Arise! Step out of the shadows of your former limitations.", author = "Shadow Monarch Creed", category = "Solo Leveling"),
    QuoteEntity(text = "No one noticed when he was the weakest E-Rank. Everyone silenced when he stood as S-Rank.", author = "System Chronicles", category = "Solo Leveling"),
    QuoteEntity(text = "Hard work is worthless for those who do not believe in themselves.", author = "Will of Fire", category = "Anime Wisdom"),
    QuoteEntity(text = "If you do not like your destiny, do not accept it. Instead, have the courage to rewrite it.", author = "Shinobi Creed", category = "Anime Wisdom"),
    QuoteEntity(text = "A person grows once he is able to defeat his own previous version.", author = "Grand Line Wisdom", category = "Anime Wisdom"),
    QuoteEntity(text = "It is not about having time; it is about making time for what truly matters to you.", author = "Hunter Instinct", category = "Anime Wisdom"),
    QuoteEntity(text = "The future belongs to those who continue to run even with bleeding feet.", author = "Courage Protocol", category = "Anime Wisdom"),
    QuoteEntity(text = "There are no shortcuts to any place worth going.", author = "Expedition Axiom", category = "Anime Wisdom"),
    QuoteEntity(text = "Your real life is the main quest. Never forget who holds the controller.", author = "System Message", category = "Solo Leveling"),
    QuoteEntity(text = "Every day you open your eyes, the System asks: Will you grow, or will you remain stagnant?", author = "System Awakens", category = "Solo Leveling")
  )
}
