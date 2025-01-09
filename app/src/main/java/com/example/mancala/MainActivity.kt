package com.example.mancala

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
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
        // todo hacer que no se pierdan los datos al girar la pantalla
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttons = arrayOf(
            binding.hole1Bttn,
            binding.hole2Bttn,
            binding.hole3Bttn,
            binding.hole4Bttn,
            binding.rumaBttn,
        )

        buttons.forEach { button -> button.textSize = 32f }

        initializeGame()
    }

    private fun initializeGame() {
        // Pedir al usuario el número de semillas:
        val numbers = arrayOf("2", "6", "7")
        var selectedNumber = numbers[0] // Valor por defecto
        AlertDialog.Builder(this).apply {
            setTitle("Elige el número de semillas por hoyo.")
            setSingleChoiceItems(numbers, 0) { _, selected ->
                selectedNumber = numbers[selected]
            }

            setPositiveButton("Elegir") { _, _ ->
                game =
                    MancalaGame(buttons.size, selectedNumber.toInt(), ::plantAnimation, ::updateUI)
                configureButtons()
            }

            setCancelable(false)
        }.show()
    }

    /**
     * Configura los botones con la lógica del juego.
     */
    private fun configureButtons() {
        // Se recorren todos menos el último, ya que es la ruma y no tiene interacción.
        buttons.dropLast(1).forEachIndexed { index, button ->
            button.text = "${game.holes[index]}"
            button.setBackgroundColor(MancalaGame.Colors.DEFAULT.value)
            button.setOnClickListener {
                if (!game.loading && game.holes[index] != 0) {
                    lifecycleScope.launch {
                        when (game.play(index, game.holes[index])) {
                            MancalaGame.Status.WIN -> winEvent()
                            MancalaGame.Status.LOOSE -> looseEvent()
                            MancalaGame.Status.CONTINUE -> continueEvent()
                        }
                    }
                }
            }
        }

        buttons.last().text = "${game.holes.last()}"
    }

    private fun plantAnimation() {
        game.apply {
            highlightButton(buttons[currentHole])
            animateSeed(
                fromButton = buttons[currentHole],
                toButton = buttons[(currentHole + 1) % buttons.size],
                seedCount = currentSeeds
            ) // Mover semillas al siguiente hoyo.
            buttons[currentHole].text = "${holes[currentHole]}" // Actualizar número de semillas.
        }
    }

    private fun updateUI() {
        buttons.forEachIndexed { index, button ->
            button.text = "${game.holes[index]}" // Actualizar número de semillas.
        }
    }

    private fun winEvent() {
        endEvent("Has Ganado!")
    }

    private fun looseEvent() {
        endEvent("Has Perdido!")
    }

    private fun endEvent(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(message)
            setMessage("Quieres seguir jugando?")
            setPositiveButton("Si") { _, _ ->
                initializeGame()
            }
            setNegativeButton("Salir") { _, _ ->
                finish()
            }
            setCancelable(false)
        }.show()
    }

    private fun continueEvent() {
        // todo
    }

    private fun animateSeed(fromButton: Button, toButton: Button, seedCount: Int) {
        val seed = createSeedTextView(seedCount)  // Crea el TextView con el número de semillas

        binding.main.addView(seed)  // Añadir al contenedor principal

        // Configurar posición inicial
        val startX = fromButton.x
        val startY = fromButton.y
        seed.x = startX
        seed.y = startY

        // Calcular posición final
        val endX = toButton.x
        val endY = toButton.y

        // Crear animación
        val animatorX = ObjectAnimator.ofFloat(seed, "x", endX)
        val animatorY = ObjectAnimator.ofFloat(seed, "y", endY)

        AnimatorSet().apply {
            playTogether(animatorX, animatorY)
            duration = game.delay
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
            textSize = 32f                   // Tamaño de texto
            setTextColor(Color.WHITE)        // Color del texto
            gravity = android.view.Gravity.CENTER  // Centrar el texto dentro del círculo
            background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.seed_image_foreground,
                null
            )  // Usar el fondo circular
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
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

