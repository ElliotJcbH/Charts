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

class IgorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_igor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle play button click
        val playButton = view.findViewById<AppCompatImageView>(R.id.btnPlay)
        playButton?.visibility = View.VISIBLE // Show the play button only for this fragment

        playButton?.setOnClickListener {
            openSpotifyAlbum()
        }
    }

    private fun openSpotifyAlbum() {
        // Use the HTTP link instead of the spotify: URI scheme
        val albumUrl = "https://open.spotify.com/album/5zi7WsKlIiUXv09tbGLKsE?si=NoqxU0SwQSKaGGadDpLCwA"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(albumUrl))
        intent.setPackage("com.spotify.music") // Try to open in Spotify app

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
