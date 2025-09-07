package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity

class AuthorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author)

        val textInfo = findViewById<TextView>(R.id.textInfo)
        val photo = findViewById<ImageView>(R.id.photo)
        val btnMain = findViewById<Button>(R.id.btnMain)

        textInfo.text = """
            Автор: Ілларіонов Микита Іванович
            Факультет: ФІТ
            Курс: 3
            Група: ІПЗ-23007Б
        """.trimIndent()

        photo.setImageResource(R.drawable.my_photo) //  my_photo.png у res/drawable
        btnMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

}
