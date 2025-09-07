package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlin.math.atan
import kotlin.math.ln
import kotlin.math.sqrt
import kotlin.math.abs

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputY = findViewById<EditText>(R.id.inputY)
        val btnCalc = findViewById<Button>(R.id.btnCalc)
        val btnAuthor = findViewById<Button>(R.id.btnAuthor)
        val resultView = findViewById<TextView>(R.id.resultView)

        // Константи
        val x = 5.0
        val a = 2.0
        val b = 3.0

        btnCalc.setOnClickListener {
            val y = inputY.text.toString().toDoubleOrNull()
            val domainErrors = buildList {
                if (y == null) add("y не число")
                if (y != null && y <= 0.0) add("y має бути > 0 (бо ln(y))")
                if (x + b - a < 0.0) add("x + b - a має бути ≥ 0 (бо √)")
                if (abs(atan(b + a)) < 1e-12) add("arctg(b+a) ≈ 0 → ділення на нуль")
            }

            if (domainErrors.isNotEmpty()) {
                resultView.text = "Помилка: " + domainErrors.joinToString("; ")
                return@setOnClickListener
            }


            val yVal = y!!
            val numerator = sqrt(x + b - a) + ln(yVal)
            val denominator = atan(b + a)
            val k = numerator / denominator
            resultView.text = "K = $k"
        }

        btnAuthor.setOnClickListener {
            startActivity(Intent(this, AuthorActivity::class.java))
        }
    }
}
