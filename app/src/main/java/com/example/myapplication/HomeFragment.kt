package com.example.myapplication

import android.os.Bundle
import androidx.core.os.bundleOf
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {
    private lateinit var viewModel: UserViewModel
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        val toolbar = rootView.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setupViews()
        observeUserData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_select_all -> {
                item.isChecked = !item.isChecked
                userListAdapter.toggleSelectAll(item.isChecked)
                return true
            }
            R.id.action_view -> {
                val selectedUsers = userListAdapter.getSelectedUsers()
                if (selectedUsers.size == 1) {
                    navigateToViewScreen(selectedUsers.first())
                } else {
                    showSelectionAlert()
                }
                return true
            }
            R.id.action_update -> {
                val selectedUsers = userListAdapter.getSelectedUsers()
                if (selectedUsers.size == 1) {
                    navigateToUpdateScreen(selectedUsers.first())
                } else {
                    showSelectionAlert()
                }
                return true
            }
            R.id.action_delete -> {
                val selectedUsers = userListAdapter.getSelectedUsers()
                if (selectedUsers.isNotEmpty()) {
                    showDeleteConfirmationDialog(selectedUsers)
                }
                return true
            }
            R.id.action_logout -> {
                navigateToLoginFragment()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViews() {
        val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        userListAdapter = UserListAdapter()
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.userListRecyclerView)
        recyclerView.adapter = userListAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val fabAddUser = requireView().findViewById<FloatingActionButton>(R.id.fabAddUser)
        fabAddUser.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addUserFragment)
        }
    }

    private fun observeUserData() {
        viewModel.allUsers.observe(viewLifecycleOwner, Observer { userList ->
            userListAdapter.submitList(userList)
        })
    }

    private fun navigateToViewScreen(userId: Int) {
        findNavController().navigate(R.id.yourViewFragment, bundleOf("userId" to userId))
    }

    private fun navigateToUpdateScreen(userId: Int) {
        findNavController().navigate(R.id.yourUpdateFragment, bundleOf("userId" to userId))
    }

    private fun navigateToLoginFragment() {
        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    private fun showSelectionAlert() {
        AlertDialog.Builder(requireContext())
            .setMessage("Please select exactly one user.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(selectedUsers: Set<Int>) {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete selected users?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteUsers(selectedUsers)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
