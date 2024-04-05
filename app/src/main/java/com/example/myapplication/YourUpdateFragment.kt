package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class YourUpdateFragment : Fragment() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var spinnerRole: Spinner
    private lateinit var btnUpdate: Button
    private lateinit var userList: MutableList<User> // Define userList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_your_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etFullName = view.findViewById(R.id.etFullName)
        etEmail = view.findViewById(R.id.etEmail)
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber)
        spinnerGender = view.findViewById(R.id.spinnerGender)
        spinnerRole = view.findViewById(R.id.spinnerRole)
        btnUpdate = view.findViewById(R.id.btnUpdate)

        setupSpinners()

        val userId = arguments?.getInt("userId") ?: 0
        val userDetails = getUserDetailsFromSharedPreferences(userId)
        userDetails?.let {
            etFullName.setText(it.fullName)
            etEmail.setText(it.email)
            etPhoneNumber.setText(it.phoneNumber)
            // Bind other fields as needed
            spinnerGender.setSelection(getGenderPosition(it.gender))
            spinnerRole.setSelection(getRolePosition(it.role))
        }

        btnUpdate.setOnClickListener {
            updateUserDetails(userId)
        }
    }

    private fun setupSpinners() {
        // Gender spinner

        val genders = arrayOf("Select Gender", "Male", "Female", "Other") // Include "Select Gender" as the first item
        val genderAdapter = CustomSpinnerAdapter(requireContext(), R.layout.spinner_item, genders)
        spinnerGender.adapter = genderAdapter
//        val genders = arrayOf("Select Gender", "Male", "Female", "Other") // Include "Select Gender" as the first item
//        val genderAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genders)
//        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerGender.adapter = genderAdapter

        // Role spinner

        // Gender spinner


// Role spinner
        val roles = arrayOf("Select Role", "Employee", "Employer") // Include "Select Role" as the first item
        val roleAdapter = CustomSpinnerAdapter(requireContext(), R.layout.spinner_item, roles)
        spinnerRole.adapter = roleAdapter

//        val roles = arrayOf("Select Role", "Employee", "Employer") // Include "Select Role" as the first item
//        val roleAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)
//        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerRole.adapter = roleAdapter
    }

    private fun getUserDetailsFromSharedPreferences(userId: Int): User? {
        val sharedPreferences = requireActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val userListJson = sharedPreferences.getString("userList", "[]")
        val userListType = object : TypeToken<List<User>>() {}.type
        userList = Gson().fromJson(userListJson, userListType) // Initialize userList
        return userList.find { it.id == userId }
    }

    private fun updateUserDetails(userId: Int) {
        val fullName = etFullName.text.toString()
        val email = etEmail.text.toString()
        val phoneNumber = etPhoneNumber.text.toString()
        val gender = spinnerGender.selectedItem.toString()
        val role = spinnerRole.selectedItem.toString()

        val validationErrors = mutableListOf<String>()

        if (!isFullNameValid(fullName)) {
            validationErrors.add("Please enter a valid full name (between 4 and 50 characters)")
        }

        if (!isEmailValid(email)) {
            validationErrors.add("Please enter a valid email address")
        }

        if (!isPhoneNumberValid(phoneNumber)) {
            validationErrors.add("Please enter a valid phone number (10 digits)")
        }

        if (gender == "Select Gender") {
            validationErrors.add("Please select a gender")
        }

        if (role == "Select Role") {
            validationErrors.add("Please select a role")
        }

        if (validationErrors.isNotEmpty()) {
            val message = validationErrors.joinToString("\n")
            displayAlert(message)
            return
        }

        // If all validations pass, proceed with updating user details
        val userIndex = userList.indexOfFirst { it.id == userId }
        if (userIndex != -1) {
            userList[userIndex] = userList[userIndex].copy(
                fullName = fullName,
                email = email,
                phoneNumber = phoneNumber,
                gender = gender,
                role = role
            )
            val sharedPreferences = requireActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("userList", Gson().toJson(userList))
            editor.apply()
            Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayAlert(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()
    }

    private fun isFullNameValid(fullName: String): Boolean {
        return fullName.length in 4..50
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.length == 10 && phoneNumber.matches("[0-9]+".toRegex())
    }

    private fun getGenderPosition(gender: String): Int {
        val genders = arrayOf("Male", "Female", "Other")
        return genders.indexOf(gender) + 1 // Adding 1 to account for "Select Gender" as the first item
    }

    private fun getRolePosition(role: String): Int {
        val roles = arrayOf("Employee", "Employer")
        return roles.indexOf(role) + 1 // Adding 1 to account for "Select Role" as the first item
    }
}
