package com.nerdygeek.grocerylistdemo.activities

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.apps.Config
import com.nerdygeek.grocerylistdemo.helper.DBhelper
import com.nerdygeek.grocerylistdemo.helper.MathHelper
import com.nerdygeek.grocerylistdemo.models.CartItem
import com.nerdygeek.grocerylistdemo.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import kotlinx.android.synthetic.main.tool_bar.*

class ProductActivity : AppCompatActivity() {

    lateinit var dbHelper: DBhelper
    private var textCartCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        init()
    }

    private fun setUpToolBar(product: Product) {
        var toolBar = tool_bar
        toolBar.title = product.productName
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)

        var item = menu.findItem(R.id.menu_cart)
        MenuItemCompat.setActionView(item, R.layout.menu_cart_layout)
        var view = MenuItemCompat.getActionView(item)
        textCartCount = view.text_cart_count
        updateCartCount()
        view.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))

        }
        return true
    }

    private fun updateCartCount() {
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


        var product = intent.getSerializableExtra(Product.KEY_PRODUCT) as Product
        setUpToolBar(product)

        var price = product.price
        var mrp = product.mrp
        var discount = mrp - price
        var description = product.description

        Picasso.get().load("${Config.IMAGE_URL}${product.image}").into(product_img_view)
        text_description.text = description
        text_description.movementMethod = ScrollingMovementMethod()
        text_price.text = "$ ${MathHelper.round(price)}"
        product_discount.text = "-$${MathHelper.round(discount)}"
        product_mrp.text = "$ ${MathHelper.round(mrp)}"
        product_mrp.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG





        dbHelper = DBhelper(this)

        updateCartCount()


        button_cart.setOnClickListener {

            var quantity = text_quantity.text.toString().toInt()
            var cartItem = CartItem(
                product._id,
                product.productName,
                product.image,
                product.mrp,
                product.price,
                quantity
            )
            if (!dbHelper.isExist(cartItem)) {

                dbHelper.addCartItem(cartItem)

            } else {
                var oldQuantity = dbHelper.getCartItemQuantity(cartItem)

                dbHelper.updateCartItem(cartItem._id, oldQuantity + quantity)
            }

            updateCartCount()

        }

        button_plus.setOnClickListener {
            var old = text_quantity.text.toString().toInt()
            var new = old + 1
            text_quantity.text = new.toString()
        }

        button_minus.setOnClickListener {
            var old = text_quantity.text.toString().toInt()
            var new = old - 1
            if (new >= 1) {
                text_quantity.text = new.toString()
            }


        }

        button_view_cart.setOnClickListener {
            text_quantity.text = "1"
            startActivity(Intent(this, CartActivity::class.java))
            finish()
        }

    }


}