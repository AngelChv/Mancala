package com.example.mancala

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mancala.databinding.ActivityMainBinding
import com.example.mancala.model.MancalaGame

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var game: MancalaGame
    private lateinit var buttons: Array<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Cambiar orientación según la orientación de la pantalla
        if (getResources().configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.root.orientation = LinearLayout.HORIZONTAL
        } else {
            binding.root.orientation = LinearLayout.VERTICAL
        }

        buttons = arrayOf(
            binding.hole1Bttn,
            binding.hole2Bttn,
            binding.hole3Bttn,
            binding.hole4Bttn,
            binding.rumaBttn,
        )

        initializeGame()
    }

    private fun initializeGame() {
        game = MancalaGame(buttons.size)

        // Configurar los botones con la lógica del juego.
        buttons.forEachIndexed { index, button ->
            button.text = "${game.holes[index]}"
            if (index != buttons.lastIndex) { // Todos los botones menos el último:
                button.setOnClickListener {
                    when (game.plant(index, game.holes[index])) {
                        MancalaGame.Status.WIN -> winEvent()
                        MancalaGame.Status.LOOSE -> looseEvent()
                        MancalaGame.Status.CONTINUE -> continueEvent()
                    }
                    updateUI()
                }
            }
        }
    }

    private fun updateUI() {
        buttons.forEachIndexed { index, button ->
            button.text = "${game.holes[index]}"
        }
    }

    private fun winEvent() {
        // todo
    }

    private fun looseEvent() {
        AlertDialog.Builder(this).apply {
            setTitle("Has perdido!")
            setMessage("Quieres seguir jugando?")
            setPositiveButton("Si") { dialog, _ ->
                initializeGame()
            }
            setNegativeButton("Salir") { dialog, _ ->
                finish()
            }
        }.show()
    }

    private fun continueEvent() {
        // todo
    }
}