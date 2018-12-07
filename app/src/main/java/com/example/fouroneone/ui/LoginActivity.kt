package com.example.fouroneone.ui

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.widget.*
import com.example.fouroneone.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class LoginActivity : AppCompatActivity() {
    private val PREF_FILENAME = "project-2-fouroneone"
    private val PREF_SAVED_EMAIL = "SAVED_EMAIL"
    private val PREF_REMEMBER_SWITCH = "remember_switch"
    private val PREF_SAVED_PASSWORD = "SAVED_PASSWORD"

    private lateinit var welcomeMessage: TextView
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var rememberSwitch: Switch
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_login)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAuth = FirebaseAuth.getInstance()

        welcomeMessage = findViewById(R.id.welcome_message)
        emailText = findViewById(R.id.email_text)
        passwordText = findViewById(R.id.password_text)
        rememberSwitch = findViewById(R.id.remember_switch)
        loginButton = findViewById(R.id.login_button)
        signupButton = findViewById(R.id.signup_button)
        emailText.addTextChangedListener(textWatcher)
        passwordText.addTextChangedListener(textWatcher)

        getPrefs()


        loginButton.setOnClickListener {
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    firebaseAnalytics.logEvent("login_success", null)

                    val user = firebaseAuth.currentUser

                    if(user != null){
                        Toast.makeText(this, "Logged in as ${user.email}!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MenuActivity::class.java)
                        startActivity(intent)
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
                        val intent = Intent(this, MenuActivity::class.java)
                        startActivity(intent)
                    }
                }
                else{
                    Toast.makeText(this, "Account creation failed.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        setPrefs()
    }

    override fun onStop() {
        super.onStop()
        setPrefs()
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

    private fun getPrefs(){
        val preferences = getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)
        Log.d("connor", "prefs remembered")
        if(preferences.getBoolean(PREF_REMEMBER_SWITCH, false)) {
            rememberSwitch.isChecked = true
            emailText.setText(preferences.getString(PREF_SAVED_EMAIL, ""))
            passwordText.setText(preferences.getString(PREF_SAVED_PASSWORD, ""))
        }
    }

    private fun setPrefs() {
        val preferences = getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        Log.d("connor", "storing prefs")
        if(rememberSwitch.isChecked){
            editor.putBoolean(PREF_REMEMBER_SWITCH, true)
            editor.putString(PREF_SAVED_EMAIL, emailText.text.toString())
            editor.putString(PREF_SAVED_PASSWORD, passwordText.text.toString())
            editor.apply()
        }
        else{
            editor.putBoolean(PREF_REMEMBER_SWITCH, false)
            editor.apply()
        }
    }
}
