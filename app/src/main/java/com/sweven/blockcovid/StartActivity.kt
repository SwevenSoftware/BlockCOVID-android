package com.sweven.blockcovid

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.sweven.blockcovid.ui.login.LoginActivity
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        val context = applicationContext
        val cacheToken = File(context.cacheDir, "token")
        val cacheTheme = File(context.cacheDir, "theme")
        if (!cacheTheme.exists()) {
            File.createTempFile("theme", null, context.cacheDir)
            cacheTheme.writeText("0")
        } else {
            val theme = cacheTheme.readText()
            if (theme == "1") {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        if (cacheToken.exists()) {
            val cacheExpiry = File(context.cacheDir, "expiryDate")
            val cacheAuth = File(context.cacheDir, "authority")
            val expiryDate = cacheExpiry.readText().toLong()
            val currentTime = LocalDateTime.now(UTC).toEpochSecond(UTC)
            if (expiryDate < currentTime) {
                val cacheUser = File(context.cacheDir, "username")
                cacheToken.delete()
                cacheExpiry.delete()
                cacheUser.delete()
                cacheAuth.delete()
                val i = Intent(this, StartActivity::class.java)
                startActivity(i)
                finish()
            } else {
                when (cacheAuth.readText()) {
                    "USER", "ADMIN" -> {
                        val i = Intent(this, UserActivity::class.java)
                        startActivity(i)
                        finish()
                    }
                    "CLEANER" -> {
                        val i = Intent(this, CleanerActivity::class.java)
                        startActivity(i)
                        finish()
                    }
                }
            }
        } else {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notifyEndChannel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
