package com.example.myapplication
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class YourViewFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_your_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val userId = arguments?.getInt("userId") ?: 0

        // Observe the user data based on userId
        userViewModel.getUserById(userId).observe(viewLifecycleOwner, Observer { user ->
            // Bind user data to views
            user?.let {
                view.findViewById<TextView>(R.id.fullNameTextView).text = it.fullName
                view.findViewById<TextView>(R.id.emailTextView).text = it.email
                view.findViewById<TextView>(R.id.phoneNumberTextView).text = it.phoneNumber
                view.findViewById<TextView>(R.id.genderTextView).text = it.gender
                view.findViewById<TextView>(R.id.roleTextView).text = it.role
            }
        })
    }
}
