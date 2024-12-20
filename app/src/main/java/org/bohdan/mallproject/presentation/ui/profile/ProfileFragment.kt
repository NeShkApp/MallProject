package org.bohdan.mallproject.presentation.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

    }

    private fun setupRecyclerView() {
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
        when (option.id) {
            ProfileOption.PROFILE -> {
                // Реалізуйте обробку перегляду профілю
            }
            ProfileOption.THEME -> {
                viewModel.toggleTheme(requireContext())
                requireActivity().recreate()
            }
            ProfileOption.CHANGE_LANGUAGE -> {
                showLanguageSelectionDialog()
            }
            ProfileOption.ORDERS -> {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToOrdersFragment()
                )
            }
            ProfileOption.LOGOUT -> {
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


    private fun showLanguageSelectionDialog() {
//        val languages = arrayOf(
//            "English",
//            "Polski",
//            "Українська"
//        )

        val languages = arrayOf(
            HtmlCompat.fromHtml("&#127468;&#127463;", HtmlCompat.FROM_HTML_MODE_LEGACY).toString() + " English",
            HtmlCompat.fromHtml("&#127477;&#127473;", HtmlCompat.FROM_HTML_MODE_LEGACY).toString() + " Polski",
            HtmlCompat.fromHtml("&#127482;&#127462;", HtmlCompat.FROM_HTML_MODE_LEGACY).toString() + " Українська"
        )

        val languageCodes = arrayOf(
            "en",
            "pl",
            "uk"
        )

        AlertDialog.Builder(requireContext())
            .setTitle("Select Language")
            .setItems(languages) { dialog, which ->
                val selectedLanguageCode = languageCodes[which]

                viewModel.changeLanguage(requireContext(), selectedLanguageCode)
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
        _binding = null
    }

}
