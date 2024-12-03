//package org.bohdan.mallproject.domain.usecase.settings
//
//import android.app.Activity
//import android.content.Context
//import org.bohdan.mallproject.domain.repository.SettingsRepository
//import javax.inject.Inject
//
//class ChangeLanguageUseCase @Inject constructor(
//    private val settingsRepository: SettingsRepository
//) {
//    operator fun invoke(context: Context, languageCode: String){
//        return settingsRepository.changeLanguage(context, languageCode)
//    }
//}