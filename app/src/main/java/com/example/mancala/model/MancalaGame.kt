package com.example.mancala.model

class MancalaGame(size: Int) {
    val holes: IntArray = IntArray(size)
    init {
        holes.forEachIndexed { index, _ ->
            if (index != holes.lastIndex) {
                holes[index] = (2..4).random()
            } else {
                holes[index] = 0
            }
        }
    }

    fun plant(index: Int, seedsCount: Int): Status {
        var position: Int = index
        var seeds: Int = seedsCount

        holes[position] = 0 // Vaciar hoyo inicial.

        // Plantar semillas:
        while (seeds > 0) { // Ir avanzando por los hoyos rellenando de una en una.
            if (++position >= holes.size) position = 0 // Recorrer los hoyos circularmente.
            if (position != holes.lastIndex && seeds == 1 && holes[position] == 0) return Status.LOOSE
            holes[position]++ // Aumentar semillas del siguiente hoyo.
            seeds-- // Decrementar las semillas.
        }
        return Status.CONTINUE
    }

    enum class Status {
        CONTINUE, WIN, LOOSE
    }
}