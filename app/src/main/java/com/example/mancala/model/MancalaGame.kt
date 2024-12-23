package com.example.mancala.model

import android.graphics.Color
import kotlinx.coroutines.delay

class MancalaGame(size: Int, private val updateUI: () -> Unit) {
    val holes: IntArray = IntArray(size)
    var loading: Boolean = false
    var currentSeeds = 0
    var currentHole = -1

    init {
        holes.forEachIndexed { index, _ ->
            if (index != holes.lastIndex) {
                holes[index] = (2..4).random()
            } else {
                holes[index] = 0
            }
        }
    }

    suspend fun plant(index: Int, seedsCount: Int): Status {
        loading = true
        currentHole = index
        currentSeeds = seedsCount

        holes[currentHole] = 0 // Vaciar hoyo inicial.

        // Plantar semillas:
        while (currentSeeds > 0) { // Ir avanzando por los hoyos rellenando de una en una.
            updateUI() // Actualizar la interfáz mediante la función percibida.
            delay(1000);
            if (++currentHole >= holes.size) currentHole = 0 // Recorrer los hoyos circularmente.
            if (currentHole != holes.lastIndex && currentSeeds == 1) {
                // Si la ultima semilla cae en un hoyo vacio se pierde
                if (holes[currentHole] == 0) {
                    loading = false
                    return Status.LOOSE
                }
                // Si cae en un hoyo con semillas se cogen todas y se sigue sembrando
                currentSeeds += holes[currentHole]
                holes[currentHole] = 0 // vaciar hoyo
                loading = false
                continue
            }
            holes[currentHole]++ // Aumentar semillas del siguiente hoyo.
            currentSeeds-- // Decrementar las semillas.
        }
        loading = false
        return Status.CONTINUE
    }

    enum class Status {
        CONTINUE, WIN, LOOSE
    }

    enum class Colors(val value: Int) {
        SELECTED(Color.rgb(193,105,5)),
        DEFAULT(Color.rgb(103, 80, 164));
    }
}