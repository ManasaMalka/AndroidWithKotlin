package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class AddUserFragment : Fragment() {
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.findViewById<Button>(R.id.btnOk).setOnClickListener { addUserAndNavigateHome() }
        setUpInputValidations()
        setupSpinner()
    }

    private fun setUpInputValidations() {
        val fullNameEditText = view?.findViewById<EditText>(R.id.etFullName)
        val emailEditText = view?.findViewById<EditText>(R.id.etEmail)
        val phoneNumberEditText = view?.findViewById<EditText>(R.id.etPhoneNumber)

        fullNameEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateFields()
            }
        })

        emailEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateFields()
            }
        })

        phoneNumberEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateFields()
            }
        })
    }

    private fun validateFields(): Boolean {
        val fullNameEditText = view?.findViewById<EditText>(R.id.etFullName)
        val emailEditText = view?.findViewById<EditText>(R.id.etEmail)
        val phoneNumberEditText = view?.findViewById<EditText>(R.id.etPhoneNumber)

        val fullName = fullNameEditText?.text.toString()
        val email = emailEditText?.text.toString()
        val phoneNumber = phoneNumberEditText?.text.toString()

        val isFullNameValid = fullName.length in 4..50
        val isEmailValid = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPhoneNumberValid = phoneNumber.length == 10 && phoneNumber.matches("[0-9]+".toRegex())

        fullNameEditText?.error = if (!isFullNameValid) "Full Name must be between 4 and 50 characters" else null
        emailEditText?.error = if (!isEmailValid) "Enter a valid email address" else null
        phoneNumberEditText?.error = if (!isPhoneNumberValid) "Enter a valid 10-digit phone number" else null

        return isFullNameValid && isEmailValid && isPhoneNumberValid
    }

    private fun addUserAndNavigateHome() {
        val isFieldsValid = validateFields()
        val isGenderSelected = view?.findViewById<RadioButton>(R.id.rbMale)?.isChecked ?: false ||
                view?.findViewById<RadioButton>(R.id.rbFemale)?.isChecked ?: false
        val selectedRole = view?.findViewById<Spinner>(R.id.spinnerRole)?.selectedItem.toString()
        val isRoleSelected = selectedRole != "Select role"

        if (isFieldsValid && isGenderSelected && isRoleSelected) {
            val fullNameEditText = view?.findViewById<EditText>(R.id.etFullName)
            val emailEditText = view?.findViewById<EditText>(R.id.etEmail)
            val phoneNumberEditText = view?.findViewById<EditText>(R.id.etPhoneNumber)

            val fullName = fullNameEditText?.text.toString()
            val email = emailEditText?.text.toString()
            val phoneNumber = phoneNumberEditText?.text.toString()
            val role = selectedRole
            val gender = when (view?.findViewById<RadioButton>(R.id.rbMale)?.isChecked) {
                true -> "Male"
                false -> "Female"
                else -> ""
            }

            val newUser = User(0, fullName, email, phoneNumber, gender, role)
            viewModel.insertUser(newUser)

            requireActivity().onBackPressed() // Navigate back to HomeFragment
        } else {
            showIncompleteFieldsAlert()
        }
    }

    private fun showIncompleteFieldsAlert() {
        AlertDialog.Builder(requireContext())
            .setMessage("Please fill all fields, select gender, and choose a role.")
            .setPositiveButton("OK", null)
            .show()
    }


    private fun setupSpinner() {
        val spinner = view?.findViewById<Spinner>(R.id.spinnerRole)
        val roles = arrayOf("Select role", "Employee", "Employer")
        val adapter = CustomSpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)
        spinner?.adapter = adapter

        spinner?.setSelection(0, false)
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

}
