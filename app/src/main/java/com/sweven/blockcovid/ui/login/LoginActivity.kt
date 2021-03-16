package com.sweven.blockcovid.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.sweven.blockcovid.MainActivity
import com.sweven.blockcovid.R
import java.io.File

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login_button)
        val loading = findViewById<ProgressBar>(R.id.loading)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
                username.text.clear()
                password.text.clear()
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                saveToken(loginResult.success)
                saveUsername(loginResult.success)
                setResult(Activity.RESULT_OK)
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }


    private fun saveToken(model: LoggedInUserView) {
        val context = applicationContext
        val token = model.token
        val expiryDate = model.expiryDate
        File.createTempFile("token", null, context.cacheDir)
        File.createTempFile("expiryDate", null, context.cacheDir)
        val cacheToken = File(context.cacheDir, "token")
        val cacheExpiry = File(context.cacheDir, "expiryDate")
        if (token != null) {
            cacheToken.writeText(token)
        }
        if (expiryDate != null) {
            cacheExpiry.writeText(expiryDate.toString())
        }
    }

    private fun saveUsername(model: LoggedInUserView) {
        val context = applicationContext
        val username = model.displayName
        File.createTempFile("username", null, context.cacheDir)
        val cacheFile = File(context.cacheDir, "username")
        if (username != null) {
            cacheFile.writeText(username)
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(applicationContext, getString(R.string.error).plus(" ").plus(errorString), Toast.LENGTH_SHORT).show()
    }
}

/**
 * Funzione di estensione per semplificare l'impostazione di un'azione afterTextChanged sui componenti EditText.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
