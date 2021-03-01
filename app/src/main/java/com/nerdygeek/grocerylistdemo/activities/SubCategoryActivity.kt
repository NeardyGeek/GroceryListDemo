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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.adapters.SubCategoryFragmentAdapter
import com.nerdygeek.grocerylistdemo.apps.Endpoint
import com.nerdygeek.grocerylistdemo.helper.DBhelper
import com.nerdygeek.grocerylistdemo.models.Category
import com.nerdygeek.grocerylistdemo.models.CategoryResponse
import com.nerdygeek.grocerylistdemo.models.SubCategoryResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import kotlinx.android.synthetic.main.tool_bar.*

class SubCategoryActivity : AppCompatActivity() {


    private lateinit var dbHelper : DBhelper
    private var textCartCount: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)


        init()

    }

    override fun onRestart() {
        super.onRestart()
        init()
    }



    private fun setUpToolBar(category: Category){
        tool_bar.title = category.catName
        setSupportActionBar(tool_bar)
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
            R.id.menu_cart -> startActivity(Intent(this, CartActivity::class.java))
            R.id.menu_profile -> Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show()
            R.id.menu_setting -> Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()

        }

        return true
    }

    private fun init() {
        dbHelper = DBhelper(this)
        var category = intent.getSerializableExtra(Category.KEY_CATE) as Category
        setUpToolBar(category)
        var subCategoryFragmentAdapter = SubCategoryFragmentAdapter(supportFragmentManager)


        var requestQueue = Volley.newRequestQueue(this)

        var request = StringRequest(
            Request.Method.GET,
            Endpoint.getSubCategoryByCatId(category.catId),
            Response.Listener {
                sub_activity_progress_bar.visibility = View.GONE
                var gson = Gson()
                var subCategoryResponse = gson.fromJson(it, SubCategoryResponse::class.java)
                for(sc in subCategoryResponse.data){
                    subCategoryFragmentAdapter.addFragment(sc)
                }

                view_pager.adapter = subCategoryFragmentAdapter
                sub_tab_layout.setupWithViewPager(view_pager)


            },
            Response.ErrorListener {
                Log.d("gld", it.toString())


            }
        )

        requestQueue.add(request)


    }
}