package org.bohdan.mallproject.presentation.viewmodel.profile

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.bohdan.mallproject.R
import org.bohdan.mallproject.domain.model.ProfileOption
import org.bohdan.mallproject.domain.usecase.settings.LoadThemePreferenceUseCase
import org.bohdan.mallproject.domain.usecase.settings.SaveThemePreferenceUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val loadThemePreferenceUseCase: LoadThemePreferenceUseCase,
    private val saveThemePreferenceUseCase: SaveThemePreferenceUseCase
): ViewModel() {
    private val _options = MutableLiveData<List<ProfileOption>>()
    val options: LiveData<List<ProfileOption>> get() = _options

    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> get() = _isDarkMode

    init {
        loadOptions()
        _isDarkMode.value = loadThemePreference()
    }

    fun changeTheme(isDarkMode: Boolean) {
        if (_isDarkMode.value != isDarkMode) {
            _isDarkMode.value = isDarkMode
            saveThemePreference(isDarkMode)

            if (isDarkMode) {
                Log.d("ProfileViewModel", "Changing to dark mode")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                Log.d("ProfileViewModel", "Changing to light mode")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }


    fun saveThemePreference(isDarkMode: Boolean) {
        saveThemePreferenceUseCase(isDarkMode)
    }

    fun loadThemePreference(): Boolean {
        return loadThemePreferenceUseCase()
    }

    private fun loadOptions() {
        _options.value = listOf(
            ProfileOption(ProfileOption.PROFILE, "Profile", R.drawable.ic_info),
            ProfileOption(ProfileOption.SETTINGS, "Settings", R.drawable.ic_settings),
            ProfileOption(ProfileOption.CHANGE_LANGUAGE, "Language", R.drawable.ic_language),
            ProfileOption(ProfileOption.LOGOUT, "Logout", R.drawable.ic_logout)
        )
    }

    // TODO: create and import LogoutUseCase and implement here
    fun logout() {
        auth.signOut()
    }
}