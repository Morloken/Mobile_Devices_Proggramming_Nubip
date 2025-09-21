package com.example.lab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.io.File

class ValuesFragment : Fragment() {

    private val filename = "results.txt"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_values, container, false)
        val textView = root.findViewById<TextView>(R.id.valuesTextView)

        val file = File(requireContext().filesDir, filename)
        if (!file.exists()) {
            textView.text = "Файл results.txt ще не створено"
        } else {
            textView.text = file.readText()
        }

        return root
    }
}
