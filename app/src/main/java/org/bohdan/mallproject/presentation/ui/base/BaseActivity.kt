package org.bohdan.mallproject.presentation.ui.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import org.bohdan.mallproject.utils.LanguagePreferences
import org.bohdan.mallproject.utils.LanguagePreferences.updateLocale
import org.bohdan.mallproject.utils.ThemePreferences.setAppTheme

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {

        val languageCode = LanguagePreferences.getLanguage(newBase)
        val updatedContext = updateLocale(newBase, languageCode)

        setAppTheme(updatedContext)

        super.attachBaseContext(updatedContext)
    }
}

