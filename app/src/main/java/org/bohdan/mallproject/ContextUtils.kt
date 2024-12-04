package org.bohdan.mallproject

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import com.google.common.io.Resources
import java.util.Locale

class ContextUtils(base: Context) : ContextWrapper(base) {

    companion object {
        fun updateLocale(c: Context, localeToSwitchTo: Locale): ContextWrapper {
            var context = c
            val resources: android.content.res.Resources? = context.resources
            val configuration: Configuration = resources!!.configuration

            // Для API >= 24, використовуємо LocaleList
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(localeToSwitchTo)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
            } else {
                // Для API < 24, без LocaleList
                configuration.locale = localeToSwitchTo
            }

            // Застосовуємо конфігурацію до контексту
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                context = context.createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }

            return ContextUtils(context)
        }
    }
}
