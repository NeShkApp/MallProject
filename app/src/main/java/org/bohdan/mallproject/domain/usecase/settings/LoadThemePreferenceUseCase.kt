package org.bohdan.mallproject.domain.usecase.settings

import org.bohdan.mallproject.domain.repository.SettingsRepository
import javax.inject.Inject

class LoadThemePreferenceUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Boolean{
        return settingsRepository.loadThemePreference()
    }
}