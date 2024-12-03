//package org.bohdan.mallproject.domain.usecase.settings
//
//import org.bohdan.mallproject.domain.repository.SettingsRepository
//import javax.inject.Inject
//
//class LoadLanguagePreferenceUseCase @Inject constructor(
//    private val settingsRepository: SettingsRepository
//) {
//    operator fun invoke(): String{
//        return settingsRepository.loadLanguagePreference()
//    }
//}