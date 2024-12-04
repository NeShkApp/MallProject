package org.bohdan.mallproject

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val preferredLanguage = LanguagePreferences.getLanguage(newBase)
        val localeToSwitchTo = Locale(preferredLanguage)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}
