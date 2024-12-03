package org.bohdan.mallproject

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.stripe.android.Stripe
import dagger.hilt.android.HiltAndroidApp
import org.bohdan.mallproject.domain.repository.SettingsRepository
import org.bohdan.mallproject.domain.usecase.settings.LoadThemePreferenceUseCase
import org.bohdan.mallproject.domain.usecase.settings.SaveThemePreferenceUseCase
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application() {

    @Inject
    lateinit var loadThemePreferenceUseCase: LoadThemePreferenceUseCase

    @Inject
    lateinit var saveThemePreferenceUseCase: SaveThemePreferenceUseCase

//    @Inject
//    lateinit var changeLanguageUseCase: ChangeLanguageUseCase
//
//    @Inject
//    lateinit var loadLanguagePreferenceUseCase: LoadLanguagePreferenceUseCase

//    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(updateBaseContextLocale(base))
//    }
//
//    private fun updateBaseContextLocale(context: Context?): Context? {
//        val languageCode = getPreferredLanguage(context)
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//        val config = Configuration()
//        config.setLocale(locale)
//        config.setLayoutDirection(locale)
//        return context?.createConfigurationContext(config)
//    }
//
//    private fun getPreferredLanguage(context: Context?): String {
//        val prefs = context?.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        return prefs?.getString("languageCode", "en") ?: "en"
//    }
//
//    private fun getLanguageFromPreferences(): String {
//        // Отримуємо мову з SharedPreferences (за замовчуванням "en")
//        return sharedPreferences.getString("languageCode", "en") ?: "en"
//    }


    override fun onCreate() {
        super.onCreate()
//        val languageCode = getLanguageFromPreferences()
//        setLocale(languageCode)

        //theme
        val isDarkMode = loadThemePreferenceUseCase()
        saveThemePreferenceUseCase(isDarkMode)

        //language
//        val languageCode = loadLanguagePreferenceUseCase()
//        setLocale(languageCode)


//        changeLanguageUseCase(applicationContext, languageCode)

//        val languageCode = loadLanguagePreferenceUseCase()
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//
//        val config = resources.configuration
//        config.setLocale(locale)
//        config.setLayoutDirection(locale)
//        resources.updateConfiguration(config, resources.displayMetrics)
    }
}