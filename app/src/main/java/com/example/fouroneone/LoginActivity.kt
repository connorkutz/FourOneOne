package com.example.fouroneone

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class LoginActivity : AppCompatActivity() {
    private val PREF_FILENAME = "project-2-fouroneone"

    private val PREF_SAVED_EMAIL = "SAVED_EMAIL"

    private lateinit var welcomeMessage: TextView
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAuth = FirebaseAuth.getInstance()

        val preferences = getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)

        welcomeMessage = findViewById(R.id.welcome_message)
        emailText = findViewById(R.id.email_text)
        passwordText = findViewById(R.id.password_text)
        loginButton = findViewById(R.id.login_button)
        signupButton = findViewById(R.id.signup_button)

        emailText.addTextChangedListener(textWatcher)
        passwordText.addTextChangedListener(textWatcher)

        val savedEmail: String = preferences.getString(PREF_SAVED_EMAIL, "")
        emailText.setText(savedEmail)

        loginButton.setOnClickListener {
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    firebaseAnalytics.logEvent("login_success", null)

                    val user = firebaseAuth.currentUser

                    if(user != null){
                        Toast.makeText(this, "Logged in as ${user.email}!", Toast.LENGTH_SHORT).show()
                    }

                    // TODO remember password switch
                }
                else{
                    val exception = task.exception
                    val errorType = if(exception is FirebaseAuthInvalidCredentialsException)
                        "invalid credentials" else "network connection"

                    val bundle = Bundle()
                    bundle.putString("error_type", errorType)

                    firebaseAnalytics.logEvent("login_failed", bundle)

                    Toast.makeText(this, "Login failed: $exception", Toast.LENGTH_SHORT).show()
                }
            }
        }


        signupButton.setOnClickListener{
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    if(user != null){
                        Toast.makeText(this, "Account created for ${user.email}!", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, "Account creation failed.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // Only enable the Login button if both the username & password have been inputted
            val emailString: String = emailText.text.toString()
            val passwordString: String = passwordText.text.toString()
            loginButton.isEnabled = emailString.isNotEmpty() && passwordString.isNotEmpty()
            signupButton.isEnabled = emailString.isNotEmpty() && passwordString.isNotEmpty()
        }
    }
}
