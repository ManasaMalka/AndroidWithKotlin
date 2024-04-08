package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class YourUpdateFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var userDao: UserDao

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var spinnerRole: Spinner
    private lateinit var btnUpdate: Button
    private var userId: Int = 0

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

        // Initialize ViewModel and UserDao
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userDao = UserDB.getDatabase(requireContext()).userDao()

        // Get user ID from arguments
        userId = arguments?.getInt("userId") ?: 0

        // Existing code for setting up spinners
        setupSpinners()

        // Set click listener for update button
        btnUpdate.setOnClickListener {
            updateUserDetails(userId)
        }

        // Load user details from database and populate the views
        loadUserDetails(userId)
    }

    private fun setupSpinners() {
        // Gender spinner

        val genders = arrayOf("Select Gender", "Male", "Female", "Other") // Include "Select Gender" as the first item
        val genderAdapter = CustomSpinnerAdapter(requireContext(), R.layout.spinner_item, genders)
        spinnerGender.adapter = genderAdapter



// Role spinner
        val roles = arrayOf("Select Role", "Employee", "Employer") // Include "Select Role" as the first item
        val roleAdapter = CustomSpinnerAdapter(requireContext(), R.layout.spinner_item, roles)
        spinnerRole.adapter = roleAdapter


    }

    private fun loadUserDetails(userId: Int) {
        userDao.getUserById(userId).observe(viewLifecycleOwner) { user ->
            user?.let {
                etFullName.setText(user.fullName)
                etEmail.setText(user.email)
                etPhoneNumber.setText(user.phoneNumber)
                spinnerGender.setSelection(getGenderPosition(user.gender))
                spinnerRole.setSelection(getRolePosition(user.role))
            }
        }
    }

    private fun updateUserDetails(userId: Int) {
        // Get user details from views
        val fullName = etFullName.text.toString()
        val email = etEmail.text.toString()
        val phoneNumber = etPhoneNumber.text.toString()
        val gender = spinnerGender.selectedItem.toString()
        val role = spinnerRole.selectedItem.toString()

        // Validate user input
        if (!isFullNameValid(fullName)) {
            displayAlert("Please enter a valid full name (4-50 characters).")
            return
        }

        if (!isEmailValid(email)) {
            displayAlert("Please enter a valid email address.")
            return
        }

        if (!isPhoneNumberValid(phoneNumber)) {
            displayAlert("Please enter a valid phone number (10 digits).")
            return
        }

        if (gender == "Select Gender") {
            displayAlert("Please select a gender.")
            return
        }

        if (role == "Select Role") {
            displayAlert("Please select a role.")
            return
        }

        val updatedUser = User(userId, fullName, email, phoneNumber, gender, role)

        // Update user in the database
        GlobalScope.launch {
            userDao.updateUser(updatedUser)
        }

        // Notify user of successful update
        Toast.makeText(requireContext(), "User details updated", Toast.LENGTH_SHORT).show()

        // Navigate back to previous screen
        requireActivity().supportFragmentManager.popBackStack()
    }


    private fun isFullNameValid(fullName: String): Boolean {
        return fullName.isNotBlank() && fullName.length in 4..50
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.length == 10 && android.util.Patterns.PHONE.matcher(phoneNumber).matches()
    }

    private fun displayAlert(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()
    }

    private fun getGenderPosition(gender: String): Int {
        val genders = arrayOf("Male", "Female", "Other")
        return genders.indexOf(gender) + 1
    }

    private fun getRolePosition(role: String): Int {
        val roles = arrayOf("Employee", "Employer")
        return roles.indexOf(role) + 1
    }
}
