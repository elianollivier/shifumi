package com.example.shifumi.strategy

import kotlin.random.Random

object StrategyEngine {
    private val userHistory = mutableListOf<String>()
    private val computerHistory = mutableListOf<String>()

    fun pickUserWeapon(): String {
        return if (userHistory.isEmpty()) {
            "Pierre"
        } else {
            val last = userHistory.last()
            when (last) {
                "Pierre" -> "Feuille"
                "Feuille" -> "Ciseaux"
                "Ciseaux" -> "Pierre"
                else -> "Pierre"
            }
        }.also { chosen ->
            userHistory.add(chosen)
        }
    }

    fun pickComputerWeapon(): String {
        val chance = Random.nextFloat()  // dans [0,1)
        val compChoice = if (userHistory.isEmpty()) {
            "Pierre"
        } else {
            val lastUser = userHistory.last()
            if (chance < 0.7f) {
                beatWeapon(lastUser)
            } else {
                pickSuboptimal(lastUser)
            }
        }

        computerHistory.add(compChoice)
        return compChoice
    }

    private fun beatWeapon(weapon: String): String {
        return when (weapon) {
            "Pierre" -> "Feuille"
            "Feuille" -> "Ciseaux"
            "Ciseaux" -> "Pierre"
            else -> "Pierre"
        }
    }

    private fun pickSuboptimal(weapon: String): String {
        return when (weapon) {
            "Pierre" -> listOf("Pierre", "Ciseaux").random()
            "Feuille" -> listOf("Feuille", "Pierre").random()
            "Ciseaux" -> listOf("Ciseaux", "Feuille").random()
            else -> "Pierre"
        }
    }

    fun compare(user: String, computer: String): String {
        if (user == computer) return "Égalité"
        return when {
            user == "Pierre" && computer == "Ciseaux" -> "Gagné"
            user == "Ciseaux" && computer == "Feuille" -> "Gagné"
            user == "Feuille" && computer == "Pierre" -> "Gagné"
            else -> "Perdu"
        }
    }
}
