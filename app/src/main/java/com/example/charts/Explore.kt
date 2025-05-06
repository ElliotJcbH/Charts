package com.example.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class Explore : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        view.findViewById<ImageView>(R.id.btnPlay).setOnClickListener { replaceFragment(IgorFragment()) }
        view.findViewById<ImageView>(R.id.btnPlay2).setOnClickListener { replaceFragment(BullyFragment()) }
        view.findViewById<ImageView>(R.id.btnPlay3).setOnClickListener { replaceFragment(NectarFragment()) }
        view.findViewById<ImageView>(R.id.btnPlay4).setOnClickListener { replaceFragment(GnxFragment()) }
        view.findViewById<ImageView>(R.id.btnPlay5).setOnClickListener { replaceFragment(LflFragment()) }
        view.findViewById<ImageView>(R.id.btnPlay6).setOnClickListener { replaceFragment(StarboyFragment()) }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}

