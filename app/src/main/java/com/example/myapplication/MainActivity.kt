package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        navigateToSplashFragment()

    }

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as? NavHostFragment
        val currentDestination = navHostFragment?.navController?.currentDestination?.id
        if (currentDestination == R.id.homeFragment || currentDestination == R.id.signUpFragment || currentDestination == R.id.loginFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun navigateToSplashFragment() {
        try {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.splashFragment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
