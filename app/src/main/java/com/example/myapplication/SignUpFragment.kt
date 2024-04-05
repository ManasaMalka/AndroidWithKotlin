package com.example.myapplication

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SignUpFragment : Fragment() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fullNameEditText = view.findViewById(R.id.fullNameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        phoneEditText = view.findViewById(R.id.phoneEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        signUpButton = view.findViewById(R.id.signUpButton)

        signUpButton.setOnClickListener {

            val fullName = fullNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val phoneNumber = phoneEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()


            if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
                showAlert("Please fill in all fields.")
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showAlert("Please enter a valid email address.")
                return@setOnClickListener
            }

            if (phoneNumber.length != 10) {
                showAlert("Please enter a 10-digit phone number.")
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                showAlert("Password must have at least 1 lowercase, 1 uppercase, 1 digit, 1 special character, and be at least 8 characters long.")
                return@setOnClickListener
            }
            findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
        }
    }

    private fun showAlert(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
        return password.matches(passwordRegex.toRegex())
    }
}
