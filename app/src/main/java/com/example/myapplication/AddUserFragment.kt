package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.fragment.app.Fragment

class AddUserFragment : Fragment() {
    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var btnOk: Button
    private lateinit var spinnerRole: Spinner
    private lateinit var radioGroupGender: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_user, container, false)
        etFullName = view.findViewById(R.id.etFullName)
        etEmail = view.findViewById(R.id.etEmail)
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber)
        btnOk = view.findViewById(R.id.btnOk)
        spinnerRole = view.findViewById(R.id.spinnerRole)
        radioGroupGender = view.findViewById(R.id.radioGroupGender)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnOk.setOnClickListener { addUserAndNavigateHome() }
        setUpInputValidations()
        setupSpinner()
    }

    private fun setUpInputValidations() {
        etFullName.addTextChangedListener {
            validateFields()
        }

        etEmail.addTextChangedListener {
            validateFields()
        }

        etPhoneNumber.addTextChangedListener {
            validateFields()
        }
    }

    private fun validateFields() {
        val fullName = etFullName.text.toString()
        val email = etEmail.text.toString()
        val phoneNumber = etPhoneNumber.text.toString()

        val isFullNameValid = fullName.length in 4..50
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPhoneNumberValid = phoneNumber.length == 10 && phoneNumber.matches("[0-9]+".toRegex())

        etFullName.error = if (!isFullNameValid) "Full Name must be between 4 and 50 characters" else null
        etEmail.error = if (!isEmailValid) "Enter a valid email address" else null
        etPhoneNumber.error = if (!isPhoneNumberValid) "Enter a valid 10-digit phone number" else null

        btnOk.isEnabled = isFullNameValid && isEmailValid && isPhoneNumberValid
    }

    private fun addUserAndNavigateHome() {
        if (btnOk.isEnabled) {
            if (areFieldsFilled()) {
                saveUserToSharedPreferences()
                requireActivity().onBackPressed() // Navigate back to HomeFragment
            } else {
                showEmptyFieldsAlert()
            }
        }
    }

    private fun areFieldsFilled(): Boolean {
        return etFullName.text.isNotBlank() && etEmail.text.isNotBlank() && etPhoneNumber.text.isNotBlank()
    }

    private fun showEmptyFieldsAlert() {
        AlertDialog.Builder(requireContext())
            .setMessage("Please fill all fields.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun saveUserToSharedPreferences() {
        val fullName = etFullName.text.toString()
        val email = etEmail.text.toString()
        val phoneNumber = etPhoneNumber.text.toString()
        val role = spinnerRole.selectedItem.toString()
        val gender = when (radioGroupGender.checkedRadioButtonId) {
            R.id.rbMale -> "Male"
            R.id.rbFemale -> "Female"
            else -> ""
        }

        val sharedPreferences = requireActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val gson = Gson()
        val existingUserListJson = sharedPreferences.getString("userList", "[]")
        val existingUserList: MutableList<User> = gson.fromJson(existingUserListJson, object : TypeToken<MutableList<User>>() {}.type)

        // Generating a unique id for the new user
        val newUserId = (System.currentTimeMillis() / 1000).toInt() // Using current timestamp as id

        val newUser = User(newUserId, fullName, email, phoneNumber, gender, role) // Including id
        existingUserList.add(newUser)

        val editor = sharedPreferences.edit()
        editor.putString("userList", gson.toJson(existingUserList))
        editor.apply()
    }

    private fun setupSpinner() {
        val roles = arrayOf("Select role", "Employee", "Employer") // Include "Select role" as the first item
        val adapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, roles) {
            override fun isEnabled(position: Int): Boolean {
                // Disable "Select role" from being selected
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view as TextView
                if (position == 0) {
                    // Set "Select role" as a hint
                    textView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                    textView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
                } else {
                    // Set other items to black text color and white background color
                    textView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                    textView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
                }
                return view
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view as TextView
                if (position == 0) {
                    // Set "Select role" as a hint
                    textView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                } else {
                    // Set other items to black text color
                    textView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                }
                return view
            }
        }
        spinnerRole.adapter = adapter

        spinnerRole.setSelection(0, false) // Set "Select role" as the initial selection
        spinnerRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle item selection if needed
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No action needed
            }
        }
    }
}
