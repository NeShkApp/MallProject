package org.bohdan.mallproject.presentation.viewmodel.profile

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.R
import org.bohdan.mallproject.domain.model.ProfileOption
import org.bohdan.mallproject.domain.usecase.auth.LogoutUseCase
import org.bohdan.mallproject.domain.usecase.settings.ChangeThemeUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val changeThemeUseCase: ChangeThemeUseCase,
    private val logoutUseCase: LogoutUseCase,
): ViewModel() {
    private val _options = MutableLiveData<List<ProfileOption>>()
    val options: LiveData<List<ProfileOption>> get() = _options

    private val _languageChanged = MutableLiveData<Boolean>()
    val languageChanged: LiveData<Boolean> get() = _languageChanged


    init {
        loadOptions()
    }

    fun loadOptions() {
        _options.value = listOf(
            ProfileOption(ProfileOption.PROFILE, R.string.profile, R.drawable.ic_info),
            ProfileOption(ProfileOption.THEME, R.string.theme, R.drawable.ic_settings),
            ProfileOption(ProfileOption.CHANGE_LANGUAGE, R.string.language, R.drawable.ic_language),
            ProfileOption(ProfileOption.LOGOUT, R.string.logout, R.drawable.ic_logout)
        )
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                logoutUseCase()
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error logout", e)
            }
        }
    }

    fun toggleTheme() {
        Log.d("ProfileViewModel", "Toggle theme called")
        changeThemeUseCase()
    }
}