package org.bohdan.mallproject

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.drawable.DrawableCompat.applyTheme
import com.stripe.android.Stripe
import dagger.hilt.android.HiltAndroidApp
import org.bohdan.mallproject.domain.repository.SettingsRepository
import org.bohdan.mallproject.domain.usecase.settings.LoadThemePreferenceUseCase
import org.bohdan.mallproject.domain.usecase.settings.SaveThemePreferenceUseCase
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application()
