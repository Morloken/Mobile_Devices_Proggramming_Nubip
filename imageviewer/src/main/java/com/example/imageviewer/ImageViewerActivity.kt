package com.example.imageviewer

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import androidx.viewpager2.widget.ViewPager2
import com.github.chrisbanes.photoview.PhotoView
import android.graphics.Color


class ImageViewerActivity : AppCompatActivity() {

    private lateinit var imageUris: ArrayList<String>
    private lateinit var viewPager: ViewPager2
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var btnHome: Button
    private lateinit var btnAuto: Button

    private val handler = Handler(Looper.getMainLooper())
    private val interval = 2000L // секунди
    private var autoPlay = false

    private val slideRunnable = object : Runnable {
        override fun run() {
            if (imageUris.isNotEmpty() && autoPlay) {
                val nextIndex = (viewPager.currentItem + 1) % imageUris.size
                viewPager.setCurrentItem(nextIndex, true)
                handler.postDelayed(this, interval)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        viewPager = findViewById(R.id.viewPager)
        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)
        btnHome = findViewById(R.id.btnHome)

        // Отримуємо список URI з MainActivity
        imageUris = intent.getStringArrayListExtra("imageList") ?: arrayListOf()

        // Налаштовуємо адаптер ViewPager2
        val adapter = ImageAdapter(imageUris)
        viewPager.adapter = adapter

        btnPrev.setOnClickListener {
            val prevIndex = if (viewPager.currentItem - 1 < 0) imageUris.size - 1 else viewPager.currentItem - 1
            viewPager.setCurrentItem(prevIndex, true)
        }

        btnNext.setOnClickListener {
            val nextIndex = (viewPager.currentItem + 1) % imageUris.size
            viewPager.setCurrentItem(nextIndex, true)
        }

        btnHome.setOnClickListener { finish() }

        // Старт автоперегляду
        autoPlay = true
        handler.postDelayed(slideRunnable, interval)


        btnAuto = findViewById(R.id.btnAuto)
        btnAuto.setOnClickListener {
            autoPlay = !autoPlay
            if (autoPlay) {
                btnAuto.text = "Стоп автоперегляду"
                btnAuto.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))) // червоний
                handler.postDelayed(slideRunnable, interval)
            } else {
                btnAuto.text = "Старт автоперегляду"
                btnAuto.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))) // зелений
                handler.removeCallbacks(slideRunnable)
            }
        }



    }

    override fun onPause() {
        super.onPause()
        autoPlay = false
        handler.removeCallbacks(slideRunnable)
    }

    override fun onResume() {
        super.onResume()
        if (!autoPlay) {
            autoPlay = true
            handler.postDelayed(slideRunnable, interval)
        }
    }

}


