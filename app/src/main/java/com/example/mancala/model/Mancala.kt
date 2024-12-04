package com.example.mancala.model

import android.widget.Button

class Mancala(private val holes: Array<Button>) {
    init {
        holes.forEachIndexed { index, hole ->
            if (index != holes.lastIndex) {
                hole.text = "2"
                hole.setOnClickListener {
                    val seeds = hole.text.toString().toIntOrNull() ?: 0
                    if (seeds > 0) {
                        plant(index, seeds)
                    }
                }
            }
        }
    }

    fun plant(index: Int, seedsCount: Int) {
        var position: Int = index
        var seeds: Int = seedsCount

        holes[position].text = "0" // Quitar las semillas del hoyo seleccionado.

        while (seeds > 0) { // Ir avanzando por los hoyos rellenando de una en una.
            if (++position >= holes.size) position = 0 // Recorrer los hoyos circularmente.
            // Aumentar semillas del siguiente hoyo.
            val count = (holes[position].text.toString().toIntOrNull() ?: 0) + 1
            holes[position].text = "$count" // Aumentar las semillas del hoyo.
            seeds-- // Decrementar las semillas.
        }
    }
}