package com.nerdygeek.registerlogindemo.helpers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.activities.CartActivity
import com.nerdygeek.grocerylistdemo.activities.MainActivity
import com.nerdygeek.grocerylistdemo.apps.Endpoint
import com.nerdygeek.grocerylistdemo.models.User
import com.nerdygeek.grocerylistdemo.models.User.Companion.KEY_USER
import com.nerdygeek.grocerylistdemo.models.UserResponse
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import org.json.JSONObject


class SessionManager(var mContext: Context) {

    val KEY_NAME = "name"
    val KEY_EMAIL = "email"
    val KEY_PASSWORD = "password"
    val KEY_USER = "user"
    val KEY_IS_LOGGED_IN = "isLogIn"

    var sharedPref: SharedPreferences
    private var editor: SharedPreferences.Editor

    init{
        sharedPref = mContext.getSharedPreferences("GLD_AUTH_file", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }



    fun getUser() : String?{
        return sharedPref.getString(KEY_USER, " ")
    }


    fun login(email: String, password: String){

        var authParams = JSONObject()
        authParams.put("email", email)
        authParams.put("password", password)


        var requestQueue = Volley.newRequestQueue(mContext)
        var request = JsonObjectRequest(
            Request.Method.POST,
            Endpoint.getLogin(),
            authParams,
            Response.Listener {
                editor.putBoolean(KEY_IS_LOGGED_IN, true).apply()
                editor.putString(KEY_USER, it.toString()).apply()

                var intent = Intent(mContext, MainActivity::class.java)



                mContext.startActivity(intent)
            },
            Response.ErrorListener {
                Toast.makeText(mContext, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        )

        requestQueue.add(request)

    }


    fun isLoggedIn(): Boolean{

        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)

    }

    fun logOut(){
        editor.putBoolean(KEY_IS_LOGGED_IN, false)
        editor.apply()
    }




}