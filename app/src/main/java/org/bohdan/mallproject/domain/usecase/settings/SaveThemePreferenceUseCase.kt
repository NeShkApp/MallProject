package org.bohdan.mallproject.domain.usecase.settings

import org.bohdan.mallproject.domain.repository.SettingsRepository
import javax.inject.Inject

class SaveThemePreferenceUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(isDarkMode: Boolean){
        settingsRepository.saveThemePreference(isDarkMode)
    }
}