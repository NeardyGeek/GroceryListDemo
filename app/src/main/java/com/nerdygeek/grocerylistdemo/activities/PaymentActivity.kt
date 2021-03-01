package com.nerdygeek.grocerylistdemo.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.apps.Config
import com.nerdygeek.grocerylistdemo.apps.Endpoint
import com.nerdygeek.grocerylistdemo.helper.DBhelper
import com.nerdygeek.grocerylistdemo.helper.MathHelper
import com.nerdygeek.grocerylistdemo.models.*
import com.nerdygeek.registerlogindemo.helpers.SessionManager
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_payment.view.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import kotlinx.android.synthetic.main.tool_bar.*
import org.json.JSONObject
import java.util.*

class PaymentActivity : AppCompatActivity() {

    var paymentMode = "unknown"
    lateinit var dbHelper: DBhelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        init()


    }

    private fun setUpToolBar() {
        var toolBar = tool_bar
        toolBar.title = "My Account"
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

        var address = intent.getSerializableExtra(Address.KEY_ADDRESS) as Address

        onRadioButtonClicked(radio_group)
        var subTotal = MathHelper.round(dbHelper.getSubtotal())
        var discounts = MathHelper.round(dbHelper.getDiscounts())
        var shipTo = "${address.houseNo} ${address.streetName}"

        sub_total.text = "you paying: $$subTotal"
        shipping_address.text = "shipping to: $shipTo"
        order_discounts.text = "you saved: $$discounts"

       

        button_place_order.setOnClickListener {
            submitOrder(dbHelper, sessionManager)
        }

        button_back_home.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


    }






    private fun submitOrder(dbHelper: DBhelper, sessionManager: SessionManager) {

        var requestQueue = Volley.newRequestQueue(this)
        var request = JsonObjectRequest(
            Request.Method.POST,
            Endpoint.postOrder(),
            getOrderParams(dbHelper, sessionManager),
            Response.Listener {
                order_summary.visibility = View.GONE
                button_place_order.visibility = View.GONE
                text_thank.visibility = View.VISIBLE
                button_back_home.visibility = View.VISIBLE

                Toast.makeText(applicationContext, "order completed", Toast.LENGTH_SHORT).show()

                dbHelper.clearTable()


            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, "failed to pay", Toast.LENGTH_SHORT).show()
            }

        )

        requestQueue.add(request)
    }

    private fun getOrderParams(dbHelper: DBhelper, sessionManager: SessionManager) : JSONObject {
        var address = intent.getSerializableExtra(Address.KEY_ADDRESS) as Address
        var shippingAddress = ShippingAddress(address.city, address.streetName, address.pincode, address.houseNo)
        var products = dbHelper.getOrderProducts()
        var discounts = dbHelper.getDiscounts().toInt()
        var ourPrice = dbHelper.getSubtotal().toInt()
        var orderAmount = discounts + ourPrice
        var orderSummary = OrderSummary(null,0, discounts, orderAmount, ourPrice, ourPrice)
        var payment = Payment(null, paymentMode, "complete")
        var user = Gson().fromJson(sessionManager.getUser(), UserResponse::class.java).user
        var orderUser = OrderUser(null, user.email, user.mobile, user.firstName)
        var date = Calendar.getInstance().time.toString()
        var orderResponse = OrderResponse(date, "confirmed", orderSummary, payment, products, shippingAddress, orderUser, user._id)
        return JSONObject(Gson().toJson(orderResponse, OrderResponse::class.java))


    }

    private fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.id) {
                R.id.check_cash ->
                    if (checked) {
                        paymentMode = "cash"
                        //button_place_order.visibility = View.VISIBLE
                    }
                R.id.check_online ->
                    if (checked) {
                        paymentMode = "online"
                        //button_place_order.visibility = View.VISIBLE


                    }
            }
        }
    }
}