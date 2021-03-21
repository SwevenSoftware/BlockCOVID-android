package com.sweven.blockcovid

import android.content.Intent
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
        setContentView(R.layout.activity_start)

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

        if(cacheToken.exists()) {
            val cacheExpiry = File(context.cacheDir, "expiryDate")
            val expiryDate = cacheExpiry.readText().toLong()
            val currentTime = LocalDateTime.now(UTC).toEpochSecond(UTC)
            if (expiryDate < currentTime) {
                val cacheUser = File(context.cacheDir, "username")
                cacheToken.delete()
                cacheExpiry.delete()
                cacheUser.delete()
                val i = Intent(this, StartActivity::class.java)
                startActivity(i)
                finish()
            } else {
                val i = Intent(this, UserActivity::class.java)
                startActivity(i)
                finish()
            }
        } else {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}
