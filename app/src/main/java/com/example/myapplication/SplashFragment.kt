package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_splash, container, false)
        rootView.setBackgroundColor(resources.getColor(android.R.color.white))
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            navigateToLoginFragment()
        }, 2000)
    }

    private fun navigateToLoginFragment() {
        try {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
