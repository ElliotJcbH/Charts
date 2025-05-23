package com.example.charts

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowInsets
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var drawer: NavigationView
    private var backPressedTime = 0L
    private val backPressThreshold = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        drawer = findViewById(R.id.profile_drawer)

        setStatusBarColor(window, R.color.accent_gray)
        Log.d("Session", supabase.auth.currentSessionOrNull().toString())
        lifecycleScope.launch {
            val user = get_my_record()

            MyUserObject.myUserId = user.id.toString()
            MyUserObject.myUsername = user.username.toString()
            MyUserObject.myEmail = user.email.toString()

            val drawerHeader = drawer.findViewById<ConstraintLayout>(R.id.drawer_header)
            val profileName: TextView = drawerHeader.findViewById(R.id.profile_name)
            val profileEmail: TextView = drawerHeader.findViewById(R.id.profile_email)
            val profileImage: ImageView = drawerHeader.findViewById(R.id.profile_picture)

            profileName.text = MyUserObject.myUsername
            profileEmail.text = MyUserObject.myEmail
            profileImage.setOnClickListener {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
        }


        val isNew = intent.getBooleanExtra("isNew", false)

        if(isNew == true) {
            WelcomeDialogFragment().show(supportFragmentManager, "WelcomeDialog")
        }

        if (savedInstanceState == null) {
            loadFragment(Home())
        }

        val notifications: AppCompatImageButton = findViewById(R.id.notifications)
        val reviewBottomSheet = DialogReviewBottomSheet()
        notifications.setOnClickListener{
            reviewBottomSheet.show(supportFragmentManager, DialogReviewBottomSheet.TAG)
        }

        drawer.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.myReviews -> {
                    loadFragment(MyReviewsFragment()) // Create this fragment
                    true
                }
                R.id.Friends -> {
//                    loadFragment(FriendsFragment()) // Create this fragment
                    true
                }
                R.id.Connections -> {
                    loadFragment(ConnectionsFragment()) // Create this fragment
                    true
                }
                else -> false
            }.also { handled ->
                if (handled) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        }

        bottomNav = findViewById(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(Home())
                R.id.nav_explore -> loadFragment(Explore())
//                R.id.nav_profile -> loadFragment(Profile())
                R.id.nav_settings -> loadFragment(Settings())
            }
            true
        }

        val menuButton = findViewById<ImageButton>(R.id.menu)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val logoutButton = findViewById<AppCompatButton>(R.id.logout_button)
        logoutButton.setOnClickListener {
            LogoutDialogFragment().show(supportFragmentManager, "LogoutDialog")
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (backPressedTime + backPressThreshold > System.currentTimeMillis()) {
                super.onBackPressed()
            } else {
                ExitDialogFragment().show(supportFragmentManager, "ExitDialog")
                backPressedTime = System.currentTimeMillis()
            }
        }
    }

    fun setStatusBarColor(window: Window, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                view.setBackgroundColor(color)

                // Adjust padding to avoid overlap
                view.setPadding(0, 0, 0, 0)
                insets
            }
        } else {
            // For Android 14 and below
            window.statusBarColor = color
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}

class WelcomeDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the custom layout
        val view = LayoutInflater.from(requireContext()).inflate(
            R.layout.welcome_dialog,
            null
        )

//        val negativeButton = view.findViewById<AppCompatButton>(R.id.negative)
        val positiveButton = view.findViewById<AppCompatButton>(R.id.positive)

        positiveButton.setOnClickListener {
            dismiss()
        }

//        negativeButton.setOnClickListener {
//            dismiss()
//        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog

    }
}

class LogoutDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the custom layout
        val view = LayoutInflater.from(requireContext()).inflate(
            R.layout.logout_dialog,
            null
        )

        val negativeButton = view.findViewById<AppCompatButton>(R.id.negative)
        val positiveButton = view.findViewById<AppCompatButton>(R.id.positive)

        positiveButton.setOnClickListener {
            var intent = Intent(requireActivity(), Register::class.java)
            startActivity(intent)

            lifecycleScope.launch {
                val res = signout_user()

                if(res != null) {
                    requireActivity().finish()
                }
            }
        }

        negativeButton.setOnClickListener {
            dismiss()
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog

    }
}

class ExitDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the custom layout
        val view = LayoutInflater.from(requireContext()).inflate(
            R.layout.exit_dialog,
            null
        )

        val negativeButton = view.findViewById<AppCompatButton>(R.id.negative)
        val positiveButton = view.findViewById<AppCompatButton>(R.id.positive)

        positiveButton.setOnClickListener {
            requireActivity().finish()
        }

        negativeButton.setOnClickListener {
            dismiss()
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog

    }
}