package com.example.mancala.model

import android.graphics.Color
import android.util.Log
import kotlinx.coroutines.delay

class MancalaGame(
    size: Int,
    private val seedsPerHole: Int,
    private val updateUIBeforePlant: () -> Unit,
    private val updateUIAfterPlant: () -> Unit
) {
    val holes: IntArray = IntArray(size)
    var loading: Boolean = false
    var currentSeeds = 0
    var currentHole = -1
    val delay: Long = 2000

    init {
        holes.forEachIndexed { index, _ ->
            if (index != holes.lastIndex) {
                holes[index] = seedsPerHole
            } else {
                holes[index] = 0
            }
        }
    }

    suspend fun play(index: Int, seedsCount: Int): Status {
        loading = true
        currentHole = index
        currentSeeds = seedsCount
        var status = Status.CONTINUE

        holes[currentHole] = 0 // Vaciar hoyo inicial.

        // Plantar semillas:
        while (currentSeeds > 0 && status == Status.CONTINUE) { // Ir avanzando por los hoyos rellenando de una en una.
            updateUIBeforePlant() // Actualizar la interfáz mediante la función percibida.
            delay(delay)

            if (++currentHole >= holes.size) currentHole = 0 // Recorrer los hoyos circularmente.

            if (currentSeeds != 1) { // Cuando no es la última semilla:
                putSeed()
            } else if (currentHole != holes.lastIndex) { // La última semilla no cae en la ruma
                // Si la ultima semilla cae en un hoyo vacio se pierde
                if (holes[currentHole] == 0) {
                    status = Status.LOOSE
                } else {
                    // Si cae en un hoyo con semillas se cogen todas y se sigue sembrando
                    currentSeeds += holes[currentHole]
                    holes[currentHole] = 0 // vaciar hoyo
                }
            } else { // La última semilla cae en la ruma
                putSeed()
                Log.i("pruebas", "${holes.last()}")
                if (holes.last() == seedsPerHole * holes.lastIndex) { // Victoria
                    // Cúando la última semilla se planta en la ruma y estan todas en ella:
                    status = Status.WIN
                }
            }

            updateUIAfterPlant()
        }

        loading = false
        return status
    }

    private fun putSeed() {
        holes[currentHole]++ // Aumentar semillas del hoyo actual.
        currentSeeds-- // Decrementar las semillas que se plantan.
    }

    enum class Status {
        CONTINUE, WIN, LOOSE
    }

    enum class Colors(val value: Int) {
        SELECTED(Color.rgb(193,105,5)),
        DEFAULT(Color.rgb(103, 80, 164));
    }
}