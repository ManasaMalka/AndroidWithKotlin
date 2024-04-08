package com.example.myapplication

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        loginButton = view.findViewById(R.id.loginButton)
        signUpTextView = view.findViewById(R.id.signUpTextView)

        signUpTextView.setOnClickListener {

                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)

        }



        loginButton.setOnClickListener {
            val emailOrPhone = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (emailOrPhone.isEmpty() || password.isEmpty()) {
                showAlert("Please fill in all fields.")
            } else if (!isValidEmailOrPhone(emailOrPhone)) {
                showAlert("Invalid email or phone number.")
            } else if (!isValidPassword(password)) {
                showAlert("Invalid password. Password must be at least 8 characters long and contain at least one lowercase letter, one uppercase letter, one digit, and one special character.")
            } else {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }


    }

    private fun showAlert(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Alert")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun isValidEmailOrPhone(input: String): Boolean {
        return if (input.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(input).matches()
        } else {
            input.length == 10 && input.all { it.isDigit() }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+\$).{8,}\$"
        return password.matches(passwordRegex.toRegex())
    }

}
