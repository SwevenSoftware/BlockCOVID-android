package com.sweven.blockcovid.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sweven.blockcovid.CleanerActivity
import com.sweven.blockcovid.R
import com.sweven.blockcovid.UserActivity
import com.sweven.blockcovid.data.model.LoggedInUser
import java.io.File

open class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login = findViewById<Button>(R.id.login_button)
        val editUsername = findViewById<TextInputEditText>(R.id.username)
        val editPassword = findViewById<TextInputEditText>(R.id.password)
        val loading = findViewById<CircularProgressIndicator>(R.id.loading)

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(
            this@LoginActivity,
            {
                checkLoginFormState(it, login)
            }
        )

        loginViewModel.loginResult.observe(
            this@LoginActivity,
            {
                checkLoginResult(it, loading, editUsername, editPassword)
            }
        )

        editUsername.afterTextChanged {
            loginViewModel.loginDataChanged(
                editUsername.text.toString(),
                editPassword.text.toString()
            )
        }

        editPassword.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    editUsername.text.toString(),
                    editPassword.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            editUsername.text.toString(),
                            editPassword.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.show()
                loginViewModel.login(editUsername.text.toString(), editPassword.text.toString())
            }
        }
    }

    fun checkLoginFormState(formState: LoginFormState, login: Button) {
        login.isEnabled = formState.isDataValid
        val username = findViewById<TextInputLayout>(R.id.username_layout)
        val password = findViewById<TextInputLayout>(R.id.password_layout)

        if (formState.usernameError != null) {
            username.error = getString(formState.usernameError)
        } else {
            username.error = null
        }
        if (formState.passwordError != null) {
            password.error = getString(formState.passwordError)
        } else {
            password.error = null
        }
    }

    fun checkLoginResult(
        formResult: LoginResult,
        loading: CircularProgressIndicator,
        editUsername: TextInputEditText,
        editPassword: TextInputEditText
    ) {
        loading.hide()
        if (formResult.success != null) {
            updateUiWithUser(formResult.success)
            saveToken(formResult.success)
            setResult(Activity.RESULT_OK)

            when (getCacheAuth()) {
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
        } else if (formResult.error != null) {
            showLoginFailed(formResult.error)
            editUsername.text?.clear()
            editPassword.text?.clear()
        }
    }

    fun getCacheAuth(): String {
        return File(cacheDir, "authority").readText()
    }

    fun saveToken(model: LoggedInUser) {
        val context = applicationContext
        val token = model.token
        val expiryDate = model.expiryDate
        val username = model.displayName
        val authority = model.authority
        File.createTempFile("token", null, context.cacheDir)
        File.createTempFile("expiryDate", null, context.cacheDir)
        File.createTempFile("username", null, context.cacheDir)
        File.createTempFile("authority", null, context.cacheDir)
        val cacheToken = File(context.cacheDir, "token")
        val cacheExpiry = File(context.cacheDir, "expiryDate")
        val cacheUser = File(context.cacheDir, "username")
        val cacheAuth = File(context.cacheDir, "authority")
        cacheToken.writeText(token)
        cacheExpiry.writeText(expiryDate.toString())
        cacheUser.writeText(username)
        cacheAuth.writeText(authority)
    }

    fun updateUiWithUser(model: LoggedInUser) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    fun showLoginFailed(errorString: String?) {
        Toast.makeText(applicationContext, getString(R.string.error).plus(" ").plus(errorString), Toast.LENGTH_SHORT).show()
    }

    /**
     * Funzione di estensione per semplificare l'impostazione di un'azione afterTextChanged sui componenti EditText.
     */
    fun TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}
