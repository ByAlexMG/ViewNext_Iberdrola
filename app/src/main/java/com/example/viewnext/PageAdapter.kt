package com.example.viewnext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager, behaviorResumeOnlyCurrentFragment: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> InstalacionFragment()
            1 -> EnergiaFragment()
            2 -> DetallesFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }

    override fun getCount(): Int {
        return 3 // Número total de fragmentos
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Titulos para las pestañas
        return when (position) {
            0 -> "Mi Instalación"
            1 -> "Energía"
            2 -> "Detalles"
            else -> null
        }
    }
}