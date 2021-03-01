package com.nerdygeek.grocerylistdemo.models

import java.io.Serializable

data class ProductResponse(
    val count: Int,
    val data: ArrayList<Product>,
    val error: Boolean
)

data class Product(
    val __v: Int,
    val _id: String,
    val catId: Int,
    val created: String,
    val description: String,
    val image: String,
    val mrp: Double,
    val position: Int,
    val price: Double,
    val productName: String,
    val quantity: Double,
    val status: Boolean,
    val subId: Int,
    val unit: String
) : Serializable{
    companion object{
        const val KEY_PRODUCT = "product"
    }
}