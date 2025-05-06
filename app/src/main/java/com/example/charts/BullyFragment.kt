package com.example.charts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.AppCompatImageView

class BullyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bully, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle play button click
        val playButton = view.findViewById<AppCompatImageView>(R.id.btnPlay2)

        // Make the play button visible in this fragment
        playButton?.visibility = View.VISIBLE

        playButton?.setOnClickListener {
            openSpotifyAlbum()
        }
    }

    private fun openSpotifyAlbum() {
        val albumUrl = "https://open.spotify.com/album/41GuZcammIkupMPKH2OJ6I?si=8zerCw5xRbO1lKkWm5jrJw"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(albumUrl))
        intent.setPackage("com.spotify.music")

        val packageManager = requireActivity().packageManager
        val isSpotifyInstalled = intent.resolveActivity(packageManager) != null

        if (isSpotifyInstalled) {
            startActivity(intent)
        } else {
            // Fallback: open in browser
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(albumUrl))
            startActivity(browserIntent)
        }
    }
}
