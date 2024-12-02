package org.bohdan.mallproject.presentation.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.FragmentProfileBinding
import org.bohdan.mallproject.domain.model.ProfileOption
import org.bohdan.mallproject.presentation.adapters.ProfileOptionsAdapter
import org.bohdan.mallproject.presentation.viewmodel.profile.ProfileViewModel

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding == null")

    private lateinit var optionsAdapter: ProfileOptionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ProfileFragment", "onCreateView called")
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ProfileFragment", "onViewCreated called")
    }

    private fun setupRecyclerView() {
        Log.d("ProfileFragment", "setupRecyclerView called")
        optionsAdapter = ProfileOptionsAdapter { option ->
            handleOptionClick(option)
        }

        binding.recyclerViewOptions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = optionsAdapter
        }
    }

    private fun observeViewModel() {
        Log.d("ProfileFragment", "observeViewModel called")
        viewModel.options.observe(viewLifecycleOwner) { options ->
            Log.d("ProfileFragment", "options updated: $options")
            optionsAdapter.submitList(options)
        }

//        viewModel.isDarkMode.observe(viewLifecycleOwner) { isDarkMode ->
//            Log.d("ProfileFragment", "isDarkMode updated: $isDarkMode")
//            if (isDarkMode) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//        }
    }

    private fun handleOptionClick(option: ProfileOption) {
        Log.d("ProfileFragment", "Option clicked: ${option.id}")
        when (option.id) {
            ProfileOption.PROFILE -> {
                // Реалізуйте обробку перегляду профілю
            }
            ProfileOption.SETTINGS -> {
                val currentTheme = viewModel.isDarkMode.value ?: false
                viewModel.changeTheme(!currentTheme)
            }
            ProfileOption.CHANGE_LANGUAGE -> {
                // Реалізуйте зміну мови
            }
            ProfileOption.LOGOUT -> {
                Log.d("ProfileFragment", "Logout option selected")
                val builder = AlertDialog.Builder(binding.root.context)
                builder.setTitle("Confirm Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
                        Log.d("ProfileFragment", "Logout confirmed")
                        findNavController().navigate(
                            ProfileFragmentDirections.actionProfileFragmentToAuthActivity()
                        )
                        logout()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

    private fun logout() {
        Log.d("ProfileFragment", "Logout initiated")
        viewModel.logout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ProfileFragment", "onDestroyView called")
        _binding = null
    }
}
