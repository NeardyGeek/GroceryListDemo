package com.nerdygeek.grocerylistdemo.apps

import kotlin.coroutines.cancellation.CancellationException

class Endpoint {


  companion object{
    private const val CATEGORY = "category"
    private const val SUB_CATEGORY = "subcategory/"
    private const val PRODUCT = "products"
    private const val REGISTER = "auth/register"
    private const val LOGIN = "auth/login"
    private const val ADDRESS = "address"
    private const val ORDER = "orders"




    fun getCategory(): String{
      return "${Config.BASE_URL + CATEGORY}"
    }

    fun getSubCategoryByCatId(catId: Int): String{
      return "${Config.BASE_URL + SUB_CATEGORY + catId}"
    }

    fun getProductBySubId(subId: Int): String{
      return "${Config.BASE_URL}$PRODUCT/sub/$subId"
    }

    fun getRegister(): String{
      return "${Config.BASE_URL + REGISTER}"
    }

    fun getLogin(): String{
      return "${Config.BASE_URL + LOGIN}"
    }

    fun getAddress(userId: String): String{
      return "${Config.BASE_URL}$ADDRESS/$userId"
    }

    fun deleteAddress(id: String?): String{
      return "${Config.BASE_URL}$ADDRESS/$id"
    }

    fun postAddress() : String{
      return "${Config.BASE_URL}$ADDRESS"
    }

    fun postOrder(): String{
      return "${Config.BASE_URL}$ORDER"
    }

    fun getOrders(id: String): String{
      return "${Config.BASE_URL}$ORDER/$id"
    }

    fun getAllProducts(): String{
      return "${Config.BASE_URL}$PRODUCT"
    }



  }

  //using method
}