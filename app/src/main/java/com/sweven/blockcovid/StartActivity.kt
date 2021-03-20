package com.sweven.blockcovid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        if(cacheToken.exists()) {
            val cacheExpiry = File(context.cacheDir, "expiryDate")
            val expiryDate = cacheExpiry.readText().toLong()
            val currentTime = LocalDateTime.now(UTC).toEpochSecond(UTC)
            if (expiryDate < currentTime) {
                val cacheUser = File(context.cacheDir, "username")
                cacheToken.delete()
                cacheExpiry.delete()
                cacheUser.delete()
                val i = Intent(this, CleanerActivity::class.java)
                startActivity(i)
                finish()
            } else {
                val i = Intent(this, CleanerActivity::class.java)
                startActivity(i)
                finish()
            }
        } else {
            val i = Intent(this, CleanerActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}
