package com.example.lab2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // Тут зберігаємо список фрагментів
    private val fragments = listOf(
        ValuesFragment(),   // вкладка "Таблиця значень"
        GraphFragment()     // вкладка "Графік"
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
