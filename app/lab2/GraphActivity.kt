package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.io.File

class GraphActivity : ComponentActivity() {

    private val filename = "results.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        val chart = findViewById<LineChart>(R.id.lineChart)

        val file = File(filesDir, filename)
        if (!file.exists()) return

        val entries = mutableListOf<Entry>()
        file.forEachLine { line ->
            val parts = line.split(" ")
            if (parts.size == 2) {
                val x = parts[0].toFloatOrNull()
                val k = parts[1].toFloatOrNull()
                if (x != null && k != null) entries.add(Entry(x, k))
            }
        }

        val dataSet = LineDataSet(entries, "K(x)")
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate() // оновити графік
    }
}
