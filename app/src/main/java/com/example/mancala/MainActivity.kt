package com.example.mancala

import android.content.res.Configuration
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mancala.databinding.ActivityMainBinding
import com.example.mancala.model.Mancala

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
            binding.root.orientation = LinearLayout.HORIZONTAL;
        } else {
            binding.root.orientation = LinearLayout.VERTICAL;
        }

        Mancala(
            holes = arrayOf(
                binding.hole1Bttn,
                binding.hole2Bttn,
                binding.hole3Bttn,
                binding.hole4Bttn,
                binding.rumaBttn,
            )
        )
    }
}