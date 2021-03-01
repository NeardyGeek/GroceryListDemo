package com.nerdygeek.grocerylistdemo.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.apps.Endpoint
import com.nerdygeek.registerlogindemo.helpers.SessionManager
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {

        var sessionManager = SessionManager(this)
        if (sessionManager.isLoggedIn()) {

            startActivity(Intent(this, MainActivity::class.java))

        }else{

            var email = ""
            var password = ""

            button_login.setOnClickListener {

                email = if (input_email.text.isNotBlank()) input_email.text.toString() else ""
                password = if (input_password.text.isNotBlank()) input_password.text.toString() else ""

                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(this, "input is empty", Toast.LENGTH_LONG).show()
                } else {
                    sessionManager.login(email, password)
                }

            }

            text_to_register.setOnClickListener {
                var intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

        }



    }
}