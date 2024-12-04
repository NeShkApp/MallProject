package org.bohdan.mallproject.presentation.ui.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import com.google.android.play.integrity.internal.c
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.LanguagePreferences
import org.bohdan.mallproject.R
import org.bohdan.mallproject.ThemePreferences
import org.bohdan.mallproject.databinding.FragmentProfileBinding
import org.bohdan.mallproject.domain.model.ProfileOption
import org.bohdan.mallproject.presentation.adapters.ProfileOptionsAdapter
import org.bohdan.mallproject.presentation.ui.home.MainActivity
import org.bohdan.mallproject.presentation.viewmodel.profile.ProfileViewModel
import java.util.Locale

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
//        Log.d("ProfileFragment", "onCreateView called")
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.d("ProfileFragment", "onViewCreated called")

        setupRecyclerView()
        observeViewModel()

    }

    private fun setupRecyclerView() {
//        Log.d("ProfileFragment", "setupRecyclerView called")
        optionsAdapter = ProfileOptionsAdapter { option ->
            handleOptionClick(option)
        }

        binding.recyclerViewOptions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = optionsAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.options.observe(viewLifecycleOwner) { options ->
            val localizedOptions = options.map {
                val title = getString(it.titleResId)
                it.copy(title = title)
            }
            optionsAdapter.submitList(localizedOptions)
        }

    }

    private fun handleOptionClick(option: ProfileOption) {
//        Log.d("ProfileFragment", "Option clicked: ${option.id}")
        when (option.id) {
            ProfileOption.PROFILE -> {
                // Реалізуйте обробку перегляду профілю
            }
            ProfileOption.THEME -> {
//                viewModel.toggleTheme()
                val isCurrentlyDarkMode = ThemePreferences.isDarkMode(requireContext())
                val newTheme = !isCurrentlyDarkMode // Перемикаємо тему
                ThemePreferences.saveThemePreference(requireContext(), newTheme)

                recreateActivityWithConfiguration()
            }
            ProfileOption.CHANGE_LANGUAGE -> {
                showLanguageSelectionDialog()

            }
            ProfileOption.LOGOUT -> {
//                Log.d("ProfileFragment", "Logout option selected")
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

    private fun recreateActivityWithConfiguration() {
        // Оновлюємо мову та тему
        applyConfiguration()

        // Перезавантажуємо активність для застосування змін
        requireActivity().recreate()
    }



    private fun applyConfiguration() {
        // Тема
        val isDarkMode = ThemePreferences.isDarkMode(requireContext())
        val mode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)

        // Мова
        val languageCode = LanguagePreferences.getLanguage(requireContext())
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = requireContext().resources.configuration
        config.setLocale(locale)

        requireActivity().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("English", "Polski")
        val languageCodes = arrayOf("en", "pl")

        AlertDialog.Builder(requireContext())
            .setTitle("Select Language")
            .setItems(languages) { dialog, which ->
                val selectedLanguageCode = languageCodes[which]
                LanguagePreferences.saveLanguage(requireContext(), selectedLanguageCode)  // Збереження вибраної мови

                requireActivity().recreate()
                dialog.dismiss()
            }
            .create()
            .show()
    }



    private fun logout() {
        Log.d("ProfileFragment", "Logout initiated")
        viewModel.logout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        Log.d("ProfileFragment", "onDestroyView called")
        _binding = null
    }

}
