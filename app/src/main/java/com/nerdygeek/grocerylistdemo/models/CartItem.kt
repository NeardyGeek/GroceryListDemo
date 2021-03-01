package com.nerdygeek.grocerylistdemo.models

import java.io.Serializable

data class CartItem(
    val _id: String,
    val name: String,
    val image: String,
    val mrp: Double,
    val price: Double,
    val quantity: Int = 0
) : Serializable{
    companion object{
        const val KEY_CART_ITEM = "cart"
    }
}