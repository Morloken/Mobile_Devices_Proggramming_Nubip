package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.io.File
import kotlin.math.atan
import kotlin.math.ln
import kotlin.math.sqrt
import kotlin.math.abs

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputY = findViewById<EditText>(R.id.inputY)
        val inputStart = findViewById<EditText>(R.id.inputStart)
        val inputEnd = findViewById<EditText>(R.id.inputEnd)
        val inputStep = findViewById<EditText>(R.id.inputStep)
        val btnCalc = findViewById<Button>(R.id.btnCalc)
        val btnReadFile = findViewById<Button>(R.id.btnReadFile)
        val btnAuthor = findViewById<Button>(R.id.btnAuthor)
        val resultView = findViewById<TextView>(R.id.resultView)

        val x = 5.0
        val a = 2.0
        val b = 3.0

        val filename = "results.txt"

        btnCalc.setOnClickListener {
            val y = inputY.text.toString().toDoubleOrNull()
            val start = inputStart.text.toString().toDoubleOrNull()
            val end = inputEnd.text.toString().toDoubleOrNull()
            val step = inputStep.text.toString().toDoubleOrNull()

            val errors = buildList {
                if (y == null) add("y не число")
                if (start == null) add("Початок проміжку не число")
                if (end == null) add("Кінець проміжку не число")
                if (step == null || step <= 0) add("Крок > 0")
                if (y != null && y <= 0.0) add("y має бути > 0 (бо ln(y))")
                if (x + b - a < 0.0) add("x + b - a ≥ 0 (бо √)")
                if (abs(atan(b + a)) < 1e-12) add("arctg(b+a) ≈ 0 → ділення на нуль")
            }

            if (errors.isNotEmpty()) {
                resultView.text = "Помилка: ${errors.joinToString("; ")}"
                return@setOnClickListener
            }

            val file = File(filesDir, filename)
            file.printWriter().use { out ->
                var xi = start!!
                while (xi <= end!!) {
                    val numerator = sqrt(x + b - a) + ln(y!!)
                    val denominator = atan(b + a)
                    val k = numerator / denominator
                    out.println("$xi $k")
                    xi += step!!
                }
            }

            resultView.text = "Таблиця обчислень записана у файл $filename"
        }

        btnReadFile.setOnClickListener {
            val file = File(filesDir, filename)
            if (!file.exists()) {
                resultView.text = "Файл ще не створено"
                return@setOnClickListener
            }
            val content = file.readText()
            resultView.text = content
        }

        btnAuthor.setOnClickListener {
            startActivity(Intent(this, AuthorActivity::class.java))
        }
    }
}
