package com.nerdygeek.grocerylistdemo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.apps.Endpoint
import com.nerdygeek.grocerylistdemo.helper.DBhelper
import com.nerdygeek.grocerylistdemo.models.Address
import com.nerdygeek.grocerylistdemo.models.User
import com.nerdygeek.grocerylistdemo.models.UserResponse
import com.nerdygeek.registerlogindemo.helpers.SessionManager
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import kotlinx.android.synthetic.main.tool_bar.*
import org.json.JSONObject

class AddAddressActivity : AppCompatActivity() {

    lateinit var dbHelper: DBhelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        init()
    }

    private fun setUpToolBar() {
        var toolBar = tool_bar
        toolBar.title = "save new address"
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        var item = menu.findItem(R.id.menu_cart)
        MenuItemCompat.setActionView(item, R.layout.menu_cart_layout)
        var view = MenuItemCompat.getActionView(item)
        var textCartCount = view.text_cart_count
        updateCartCount(textCartCount)
        view.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))

        }

        return true
    }

    private fun updateCartCount(textCartCount: TextView) {

        var count = dbHelper.getCartItemCount()
        Log.d("gld", count.toString())
        if (count >= 1) {
            textCartCount.visibility = View.VISIBLE
            textCartCount.text = "$count"
        } else {
            textCartCount?.visibility = View.GONE
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_profile -> Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show()
            R.id.menu_setting -> Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()

        }

        return true
    }

    private fun init() {
        dbHelper = DBhelper(this)
        var sessionManager = SessionManager(this)
        setUpToolBar()
        var user = Gson().fromJson(sessionManager.getUser(), UserResponse::class.java).user

        input_name.setText(user.firstName)
        input_mobile.setText(user.mobile)

        button_save.setOnClickListener {
            var user = Gson().fromJson(SessionManager(this).getUser(), UserResponse::class.java).user
            var houseNo = input_house_no.text.toString()
            var streetName = input_street_name.text.toString()
            var state = input_state.text.toString()
            var city = input__city.text.toString()
            var pincode = input_zip.text.toString().toInt()
            var type = input_type.text.toString()

            var address = Address(null, null, city,houseNo,state,user.mobile, user.firstName, pincode, streetName,type,user._id)
            var addressParams = JSONObject(Gson().toJson(address, Address::class.java))



            var requestQueue = Volley.newRequestQueue(this)
            var request = JsonObjectRequest(
                Request.Method.POST,
                Endpoint.postAddress(),
                addressParams,
                Response.Listener {
                    Toast.makeText(applicationContext, "new address has been filed", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AddressActivity::class.java))


                },

                Response.ErrorListener {
                    Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT).show()


                }
            )
            requestQueue.add(request)


        }
    }

}