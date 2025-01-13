package org.bohdan.mallproject.presentation.ui.base

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.bohdan.mallproject.R
import org.bohdan.mallproject.data.receivers.AirplaneModeReceiver
import org.bohdan.mallproject.data.sharedpreferences.LanguagePreferences.getLanguage
import org.bohdan.mallproject.data.sharedpreferences.LanguagePreferences.updateLocale
import org.bohdan.mallproject.data.sharedpreferences.ThemePreferences.setAppTheme

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val languageCode = getLanguage(newBase)
        val updatedContext = updateLocale(newBase, languageCode)
        setAppTheme(updatedContext)

        super.attachBaseContext(updatedContext)
    }

    private val airplaneModeReceiver = AirplaneModeReceiver { isAirplaneModeOn ->
        if (isAirplaneModeOn) {
            showCheckInternetDialog()
        } else {
            dismissDialog()
        }
    }

    private var checkInternetDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(airplaneModeReceiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))

        //first time show
        val isAirplaneModeOn = Settings.Global.getInt(
            contentResolver,
            Settings.Global.AIRPLANE_MODE_ON
        ) != 0
        if (isAirplaneModeOn) {
            showCheckInternetDialog()
        }
    }

    private fun showCheckInternetDialog() {
        if (checkInternetDialog == null || !checkInternetDialog!!.isShowing) {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.check_internet_dialog, null)

            builder.setView(dialogView)

            builder.setCancelable(false)
            checkInternetDialog = builder.show()
        }
    }

    private fun dismissDialog() {
        checkInternetDialog?.dismiss()
        checkInternetDialog = null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airplaneModeReceiver)
    }

}