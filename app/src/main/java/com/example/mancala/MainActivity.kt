package com.example.mancala

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.mancala.databinding.ActivityMainBinding
import com.example.mancala.model.MancalaGame
import kotlinx.coroutines.launch

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
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
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
        game = MancalaGame(buttons.size, ::updateUI)

        // Configurar los botones con la lógica del juego.
        buttons.forEachIndexed { index, button ->
            button.text = "${game.holes[index]}"
            button.setBackgroundColor(MancalaGame.Colors.DEFAULT.value)
            if (index != buttons.lastIndex) { // Todos los botones menos el último:
                button.setOnClickListener {
                    if (!game.loading) {
                        lifecycleScope.launch {
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
        }
    }

    private fun updateUI() {
        buttons.forEachIndexed { index, button ->
            if (index == game.currentHole) {
                highlightButton(button)
                animateSeed(button, buttons[(index + 1) % buttons.size], game.currentSeeds) // Semilla al siguiente.
                button.setBackgroundColor(MancalaGame.Colors.SELECTED.value)
            } else {
                button.setBackgroundColor(MancalaGame.Colors.DEFAULT.value)
            }
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

    private fun animateSeed(fromButton: Button, toButton: Button, seedCount: Int) {
        val seed = createSeedTextView(seedCount)  // Crea el TextView con el número de semillas

        binding.main.addView(seed)  // Añadir al contenedor principal

        // Configurar posición inicial
        val startX = fromButton.x + fromButton.width / 2
        val startY = fromButton.y + fromButton.height / 2
        seed.x = startX
        seed.y = startY

        // Calcular posición final
        val endX = toButton.x + toButton.width / 2
        val endY = toButton.y + toButton.height / 2

        // Crear animación
        val animatorX = ObjectAnimator.ofFloat(seed, "x", endX)
        val animatorY = ObjectAnimator.ofFloat(seed, "y", endY)

        AnimatorSet().apply {
            playTogether(animatorX, animatorY)
            duration = 1000
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator) {
                    binding.main.removeView(seed)  // Quitar la semilla al terminar
                }

                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            start()
        }
    }

    private fun createSeedTextView(seedCount: Int): TextView {
        // Crea un TextView con el número de semillas
        return TextView(this).apply {
            text = "$seedCount"              // Muestra el número de semillas
            textSize = 16f                   // Tamaño de texto
            setTextColor(Color.WHITE)        // Color del texto
            gravity = android.view.Gravity.CENTER  // Centrar el texto dentro del círculo
            background = resources.getDrawable(R.drawable.seed_image_foreground, null)  // Usar el fondo circular
            layoutParams = FrameLayout.LayoutParams(100, 100).apply {
                gravity = android.view.Gravity.CENTER       // Centrar el TextView en el contenedor
            }
        }
    }

    private fun highlightButton(button: Button) {
        val colorFrom = MancalaGame.Colors.DEFAULT.value
        val colorTo = MancalaGame.Colors.SELECTED.value

        val colorAnimation = android.animation.ValueAnimator.ofArgb(colorFrom, colorTo).apply {
            duration = 500
            repeatMode = android.animation.ValueAnimator.REVERSE
            repeatCount = 1
            addUpdateListener { animator ->
                button.setBackgroundColor(animator.animatedValue as Int)
            }
        }
        colorAnimation.start()
    }
}

