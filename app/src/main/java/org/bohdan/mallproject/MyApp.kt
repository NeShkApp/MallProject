package org.bohdan.mallproject

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import dagger.hilt.android.HiltAndroidApp
import org.bohdan.mallproject.utils.AirplaneModeReceiver

@HiltAndroidApp
class MyApp : Application()