package com.example.shifumi.score

object ScoresManager {
    var currentStreak = 0
    var bestStreak = 0

    fun onWin(): Boolean {
        currentStreak++
        if (currentStreak > bestStreak) {
            bestStreak = currentStreak
            return true
        }
        return false
    }

    fun onLose() {
        currentStreak = 0
    }
}
