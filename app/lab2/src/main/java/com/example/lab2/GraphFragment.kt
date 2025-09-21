package com.example.lab2

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.io.File

class GraphFragment : Fragment() {

    private val filename = "results.txt"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_graph, container, false)
        val chart = root.findViewById<LineChart>(R.id.lineChart)

        val file = File(requireContext().filesDir, filename)
        if (!file.exists()) {
            chart.setNoDataText("Файл results.txt не знайдено")
            return root
        }

        val entries = mutableListOf<Entry>()
        file.forEachLine { line ->
            val parts = line.trim().split(Regex("\\s+"))
            if (parts.size >= 2) {
                val x = parts[0].toFloatOrNull()
                val y = parts[1].toFloatOrNull()
                if (x != null && y != null) entries.add(Entry(x, y))
            }
        }

        if (entries.isEmpty()) {
            chart.setNoDataText("Нема валідних точок у results.txt")
            return root
        }

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

        return root
    }
}
