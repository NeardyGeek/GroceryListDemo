package com.nerdygeek.grocerylistdemo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.adapters.CartItemAdapter
import com.nerdygeek.grocerylistdemo.helper.DBhelper
import com.nerdygeek.grocerylistdemo.helper.MathHelper
import com.nerdygeek.grocerylistdemo.models.CartItem
import com.nerdygeek.registerlogindemo.helpers.SessionManager
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import kotlinx.android.synthetic.main.tool_bar.*

class CartActivity : AppCompatActivity() {

    private lateinit var sessionManager : SessionManager
    private lateinit var dbHelper : DBhelper
    private var textCartCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        init()
    }

    private fun setUpToolBar(){
        var toolBar = tool_bar
        toolBar.title = "Cart"
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        var item = menu.findItem(R.id.menu_cart)
        MenuItemCompat.setActionView(item, R.layout.menu_cart_layout)
        var view = MenuItemCompat.getActionView(item)
        textCartCount = view.text_cart_count
        updateCartCount(dbHelper)
        view.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))


        }


        return true
    }


    fun updateCartCount(dbHelper : DBhelper) {
        var count = dbHelper.getCartItemCount()
        Log.d("gld", count.toString())
        if(count >= 1){
            textCartCount?.visibility = View.VISIBLE
            textCartCount?.text = "$count"
        }else{
            textCartCount?.visibility = View.GONE
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.menu_profile -> Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show()
            R.id.menu_setting -> Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()

        }

        return true
    }

    private fun init() {


        dbHelper = DBhelper(this)
        setUpToolBar()
        sessionManager = SessionManager(this)


        cart_recycler_view.adapter = CartItemAdapter(this, dbHelper.getCartItems())
        cart_recycler_view.layoutManager = LinearLayoutManager(this)
        updateSubTotal(dbHelper)

        button_check_out.setOnClickListener{
            startActivity(Intent(this, AddressActivity::class.java))
        }



//        for(cartItem in cartItemList){
//            total += (cartItem.price * cartItem.quantity)
//        }
//
//        updateSubTotal(0.0)


    }

    fun updateSubTotal(dbHelper: DBhelper){
        var subTotal = MathHelper.round(dbHelper.getSubtotal())
        var discounts = MathHelper.round(dbHelper.getDiscounts())
        text_sub_total.text = "subtotal: $$subTotal"
        text_discounts.text = "discounts: -$$discounts"
    }



//    fun updateSubTotal(newPrice: Double) {
//        total += newPrice
//        text_sub_total.text = "subtotal: $total"
//
//    }


}