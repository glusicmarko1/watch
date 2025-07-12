package com.example.watch.presentation

/**
 * Represents the score for a padel match.
 */
data class ScoreState(
    val pointsA: Int = 0,
    val pointsB: Int = 0,
    val gamesA: Int = 0,
    val gamesB: Int = 0,
    val setsA: Int = 0,
    val setsB: Int = 0
) {
    fun addPointToA(): ScoreState {
        var pA = pointsA + 1
        var pB = pointsB
        var gA = gamesA
        var gB = gamesB
        var sA = setsA
        var sB = setsB

        if (pA >= 4 && pA - pB >= 2) {
            gA++
            pA = 0
            pB = 0
            if (gA >= 6 && gA - gB >= 2) {
                sA++
                gA = 0
                gB = 0
            }
        }
        return copy(pointsA = pA, pointsB = pB, gamesA = gA, gamesB = gB, setsA = sA, setsB = sB)
    }

    fun addPointToB(): ScoreState {
        var pA = pointsA
        var pB = pointsB + 1
        var gA = gamesA
        var gB = gamesB
        var sA = setsA
        var sB = setsB

        if (pB >= 4 && pB - pA >= 2) {
            gB++
            pA = 0
            pB = 0
            if (gB >= 6 && gB - gA >= 2) {
                sB++
                gA = 0
                gB = 0
            }
        }
        return copy(pointsA = pA, pointsB = pB, gamesA = gA, gamesB = gB, setsA = sA, setsB = sB)
    }
}

fun pointsToString(points: Int): String = when (points) {
    0 -> "0"
    1 -> "15"
    2 -> "30"
    3 -> "40"
    else -> "A"
}
