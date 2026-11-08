package com.example

import com.example.data.model.AttributeEntity
import com.example.data.model.PlayerEntity
import com.example.data.model.QuestEntity
import com.example.data.model.UserEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun verify_hunter_rank_calculations() {
    assertEquals("E-Rank", PlayerEntity.calculateRank(1))
    assertEquals("D-Rank", PlayerEntity.calculateRank(5))
    assertEquals("C-Rank", PlayerEntity.calculateRank(10))
    assertEquals("B-Rank", PlayerEntity.calculateRank(15))
    assertEquals("A-Rank", PlayerEntity.calculateRank(25))
    assertEquals("S-Rank", PlayerEntity.calculateRank(40))
    assertEquals("National-Level Hunter", PlayerEntity.calculateRank(60))
  }

  @Test
  fun verify_user_entity_calculations() {
    val user = UserEntity(
      username = "Jinwoo",
      level = 2,
      currentXp = 250,
      currentWeight = 75.0f,
      heightCm = 175.0f
    )

    // xpRequiredForNextLevel = level * 200 + 100 = 2 * 200 + 100 = 500
    assertEquals(500, user.xpRequiredForNextLevel)
    assertEquals(0.5f, user.xpProgressRatio, 0.001f)

    // BMI: 75 / (1.75 * 1.75) = 24.489796
    assertEquals(24.49f, user.bmi, 0.05f)
  }

  @Test
  fun verify_attribute_entity() {
    val attribute = AttributeEntity(
      code = "INT",
      name = "Intelligence",
      baseValue = 18,
      bonusValue = 4,
      hexColor = "#2196F3",
      description = "Mental aptitude for GATE Mech calculations"
    )

    assertEquals("INT", attribute.code)
    assertEquals(22, attribute.totalValue)
    assertEquals("Intelligence", attribute.name)
    assertEquals("#2196F3", attribute.hexColor)
  }

  @Test
  fun verify_quest_entity() {
    val quest = QuestEntity(
      title = "Solve 20 GATE PYQs on Thermodynamics",
      description = "Practice first law and second law problems",
      category = "GATE",
      difficulty = "B",
      priority = "High",
      xpReward = 150,
      intReward = 2,
      status = "Available",
      dateStr = "2026-11-08"
    )

    assertEquals("GATE", quest.category)
    assertEquals(150, quest.xpReward)
    assertEquals("Available", quest.status)

    val completedQuest = quest.copy(status = "Completed")
    assertEquals("Completed", completedQuest.status)
  }
}
