package com.nerdygeek.grocerylistdemo.helper

class MathHelper {
    companion object{
        fun round(number: Double): Double{
            val number3digits:Double = String.format("%.3f", number).toDouble()
            return String.format("%.2f", number3digits).toDouble()
        }
    }
}