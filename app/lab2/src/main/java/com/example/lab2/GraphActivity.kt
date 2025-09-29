package com.example.lab2

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.io.File

class GraphActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        // --- графік ---
        val chart = findViewById<LineChart>(R.id.lineChart)
        val file = File(filesDir, "results.txt")

        if (!file.exists()) {
            chart.clear()
            chart.setNoDataText("Файл results.txt не знайдено")
        } else {
            val entries = mutableListOf<Entry>()
            file.forEachLine { line ->
                val parts = line.trim().split(Regex("\\s+"))
                if (parts.size >= 2) {
                    val x = parts[0].toFloatOrNull()
                    val y = parts[1].toFloatOrNull()
                    if (x != null && y != null) {
                        entries.add(Entry(x, y))
                    }
                }
            }

            if (entries.isEmpty()) {
                chart.clear()
                chart.setNoDataText("Нема валідних точок у results.txt")
            } else {
                val dataSet = LineDataSet(entries, "K(x)").apply {
                    color = Color.BLUE
                    valueTextColor = Color.BLACK
                    lineWidth = 2f
                    setDrawCircles(true)
                    setDrawValues(false)

                }
                chart.data = LineData(dataSet)
                chart.description.isEnabled = false
                chart.invalidate()
            }
        }

        // --- кнопка Назад ---
        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }
}
