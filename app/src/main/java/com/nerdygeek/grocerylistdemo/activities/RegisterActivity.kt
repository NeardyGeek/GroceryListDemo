package com.nerdygeek.grocerylistdemo.activities

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }

    private fun init() {



        var intent = Intent(this, LoginActivity::class.java)
        button_register.setOnClickListener {
            var firstName =
                if (create_name.text.isNotBlank()) create_name.text.toString() else ""
            var email = if (create_email.text.isNotBlank()) create_email.text.toString() else ""
            var mobile =
                if (create_mobile.text.isNotBlank()) create_mobile.text.toString() else ""
            var password =
                if (create_name.text.isNotBlank()) create_password.text.toString() else ""

            if (firstName.isBlank() || email.isBlank() || mobile.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(
                    email
                ).matches()
            ) {
                Toast.makeText(this, "invalid input", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "invalid password", Toast.LENGTH_SHORT).show()
            } else {
                var authParams = JSONObject()
                authParams.put("firstName", firstName)
                authParams.put("email", email)
                authParams.put("mobile", mobile)
                authParams.put("password", password)

                var requestQueue = Volley.newRequestQueue(this)

                var request = JsonObjectRequest(
                    Request.Method.POST,
                    Endpoint.getRegister(),
                    authParams,
                    Response.Listener {

                        startActivity(intent)


                    },

                    Response.ErrorListener {

                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()

                    }

                )

                requestQueue.add(request)
            }


        }

        text_to_login.setOnClickListener {
            startActivity(intent)
        }


    }
}