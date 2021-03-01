package com.nerdygeek.grocerylistdemo.models

import java.io.Serializable


data class OrdersResponse(
    val count: Int,
    val data: ArrayList<OrderResponse>,
    val error: Boolean
)

data class OrderResponse(

    val date: String,
    val orderStatus: String,
    val orderSummary: OrderSummary,
    val payment: Payment,
    val products: ArrayList<OrderProduct>,
    val shippingAddress: ShippingAddress,
    val user: OrderUser,
    val userId: String
): Serializable{
    companion object{
        const val KEY_ORDER = "order"
    }
}

data class OrderSummary(
    val _id: String? = null,
    val deliveryCharges: Int,
    val discount: Int,
    val orderAmount: Int,
    val ourPrice: Int,
    val totalAmount: Int
): Serializable{
    companion object{
        const val KEY_ORDER = "order"
    }
}

data class Payment(
    val _id: String? = null,
    val paymentMode: String,
    val paymentStatus: String
): Serializable{
    companion object{
        const val KEY_ORDER = "order"
    }
}

data class OrderProduct(
    val _id: String? = null,
    val image: String,
    val mrp: Double,
    val price: Double,
    val quantity: Int
): Serializable{
    companion object{
        const val KEY_ORDER = "order"
    }
}

data class ShippingAddress(
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val streetName: String
): Serializable{
    companion object{
        const val KEY_ORDER = "order"
    }
}

data class OrderUser(
    val _id: String? = null,
    val email: String,
    val mobile: String,
    val name: String
): Serializable{
    companion object{
        const val KEY_ORDER = "order"
    }
}