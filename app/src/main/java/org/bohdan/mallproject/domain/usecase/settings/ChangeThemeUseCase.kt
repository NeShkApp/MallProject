package org.bohdan.mallproject.domain.usecase.settings

import org.bohdan.mallproject.domain.repository.SettingsRepository
import javax.inject.Inject

class ChangeThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(){
        settingsRepository.changeTheme()
    }
}