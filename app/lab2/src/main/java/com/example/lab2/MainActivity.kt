package com.example.lab2
import androidx.viewpager2.widget.ViewPager2
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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

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
        val btnGraph = findViewById<Button>(R.id.btnGraph)
        val resultView = findViewById<TextView>(R.id.resultView)

        val a = 2.0
        val b = 3.0
        val filename = "results.txt"

        // Адаптер для вкладок
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter


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
                    val numerator = sqrt(xi + b - a) + ln(y!!)
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
            resultView.text = file.readText()
        }

        btnGraph.setOnClickListener {
            startActivity(Intent(this, GraphActivity::class.java))
        }

        btnAuthor.setOnClickListener {
            startActivity(Intent(this, AuthorActivity::class.java))
        }
    }
}
