package com.sweven.blockcovid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sweven.blockcovid.ui.login.LoginActivity
import java.io.File


class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val context = applicationContext
        val cacheFile = File(context.cacheDir, "token")
        if(cacheFile.exists()) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        } else {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}