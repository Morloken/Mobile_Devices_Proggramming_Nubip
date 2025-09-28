package com.example.imageviewer

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AuthorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author)

        val authorName = findViewById<TextView>(R.id.authorName)
        val authorPhoto = findViewById<ImageView>(R.id.authorPhoto)
        val btnBack = findViewById<Button>(R.id.btnBack)

        authorName.text = "Автор: Микита Ілларіонов"
        authorPhoto.setImageResource(R.drawable.author_photo)


        btnBack.setOnClickListener {
            finish()
        }
    }
}
