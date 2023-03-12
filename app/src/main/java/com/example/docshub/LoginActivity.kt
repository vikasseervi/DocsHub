package com.example.docshub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class LoginActivity : AppCompatActivity() {

    lateinit var etUsername: EditText
    lateinit var  etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonClick = findViewById<Button>(R.id.login_button)
        buttonClick.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }

    fun validateInput(): Boolean {
        if (R.id.username_editText.equals("onyx333") && R.id.password_editText.equals("onyx333") ) {
            return true
        }
        return false
    }
}

//
//
//package com.handyopinion
//
//import android.content.Intent
//import android.util.Patterns
//import android.view.View
//import android.widget.EditText
//import android.widget.Toast
//
//
//class LoginActivity : AppCompatActivity() {
//    lateinit var etUsername: EditText
//    lateinit var  etPassword: EditText
//    val MIN_PASSWORD_LENGTH = 6
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//        viewInitializations()
//    }
//
//    fun viewInitializations() {
//        etUsername = findViewById(R.id.username_editText)
//        etPassword = findViewById(R.id.password_editText)
//
//        // To show back button in actionbar
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//    }
//
//    // Checking if the input in form is valid
//    fun validateInput(): Boolean {
//        if (etUsername.text.toString() == "") {
//            etUsername.error = "Please Enter Email"
//            return false
//        }
//        if (etPassword.text.toString() == "") {
//            etPassword.error = "Please Enter Password"
//            return false
//        }
//
//        // checking the proper email format
//        if (!isEmailValid(etUsername.text.toString())) {
//            etUsername.error = "Please Enter Valid Email"
//            return false
//        }
//
//        // checking minimum password Length
//        if (etPassword.text.length < MIN_PASSWORD_LENGTH) {
//            etPassword.error = "Password Length must be more than " + MIN_PASSWORD_LENGTH + "characters"
//            return false
//        }
//        return true
//    }
//
//    fun isEmailValid(email: String?): Boolean {
//        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
//    }
//
//    // Hook Click Event
//    fun performSignUp(v: View) {
//        if (validateInput()) {
//
//            // Input is valid, here send data to your server
//            val email = etUsername!!.text.toString()
//            val password = etPassword!!.text.toString()
//            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
//            // Here you can call you API
//            // Check this tutorial to call server api through Google Volley Library https://handyopinion.com
//        }
//    }
//
//    fun goToSignup(v: View) {
//        // Open your SignUp Activity if the user wants to signup
//        // Visit this article to get SignupActivity code https://handyopinion.com/signup-activity-in-android-studio-kotlin-java/
//        val intent = Intent(this, SignupActivity::class.java)
//        startActivity(intent)
//    }
//}