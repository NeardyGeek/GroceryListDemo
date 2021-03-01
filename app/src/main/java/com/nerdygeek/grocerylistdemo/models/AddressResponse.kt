package com.nerdygeek.grocerylistdemo.models

import java.io.Serializable

data class AddressResponse(
    val count: Int,
    val data: ArrayList<Address>,
    val error: Boolean
)

data class Address(
    val __v: Int? = null,
    val _id: String? = null,
    val city: String,
    val houseNo: String,
    val location: String,
    val mobile: String,
    val name: String,
    val pincode: Int,
    val streetName: String,
    val type: String,
    val userId: String
) : Serializable{
    companion object{
        const val KEY_ADDRESS = "address"
    }
}