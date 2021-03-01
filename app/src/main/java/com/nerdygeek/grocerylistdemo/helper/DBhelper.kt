package com.nerdygeek.grocerylistdemo.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getIntOrNull
import com.nerdygeek.grocerylistdemo.apps.Config
import com.nerdygeek.grocerylistdemo.models.CartItem
import com.nerdygeek.grocerylistdemo.models.OrderProduct

class DBhelper(var mContext: Context) : SQLiteOpenHelper(mContext, Config.DATABASE_NAME, null, Config.DATABASE_VERSION ) {

    companion object{


        const val TABLE_NAME = "cart"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_PRICE = "price"
        const val COLUMN_IMG = "image"
        const val COLUMN_MRP = "mrp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var createTable = "create table $TABLE_NAME ($COLUMN_ID char(100) primary key, $COLUMN_NAME char(100), $COLUMN_QUANTITY integer, $COLUMN_PRICE double, $COLUMN_IMG char(50), $COLUMN_MRP double)"
        db?.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table $TABLE_NAME")
        onCreate(db)

    }

    fun isExist(cartItem: CartItem): Boolean{
        var db = readableDatabase
        var query = "select * from $TABLE_NAME where id = ?"
        var cursor = db.rawQuery(query, arrayOf(cartItem._id))

        return cursor.count != 0

    }

    fun addCartItem(cartItem: CartItem) {
        var db = writableDatabase
        var contentValues = ContentValues()
        if(!isExist(cartItem)){
            contentValues.put(COLUMN_ID, cartItem._id)
            contentValues.put(COLUMN_NAME, cartItem.name)
            contentValues.put(COLUMN_QUANTITY, cartItem.quantity)
            contentValues.put(COLUMN_PRICE, cartItem.price)
            contentValues.put(COLUMN_IMG, cartItem.image)
            contentValues.put(COLUMN_MRP, cartItem.mrp)
            db.insert(TABLE_NAME, null, contentValues)

        }


    }

    fun getCartItemCount(): Int{
        var count = 0
        var db = readableDatabase
        var query = "select sum ($COLUMN_QUANTITY) from $TABLE_NAME"

        var cursor = db.rawQuery(query, null)
        if(cursor != null && cursor.moveToFirst()){
            count = cursor.getInt(0)
        }

        return count

    }

    fun getCartItemQuantity(cartItem: CartItem): Int{
        var quantity = 0
        var db = readableDatabase
        var query = "select * from $TABLE_NAME where $COLUMN_ID = ?"
        var cursor = db.rawQuery(query, arrayOf(cartItem._id))

        if(cursor != null && cursor.moveToFirst()){
            quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
        }

        return quantity
    }

    fun updateCartItem(id: String, quantity: Int){
        var db = writableDatabase
        var contentValues = ContentValues()
        contentValues.put(COLUMN_QUANTITY, quantity)


        db.update(TABLE_NAME, contentValues, "$COLUMN_ID = ?", arrayOf(id))
    }


    fun deleteCartItem(cartItem: CartItem){
        writableDatabase.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf("${cartItem._id}"))
    }

    fun getSubtotal(): Double{
        var db = readableDatabase
        var subTotal = 0.0
        var query = "select sum($COLUMN_QUANTITY * $COLUMN_PRICE) from $TABLE_NAME"
        var cursor = db.rawQuery(query, null)

        if(cursor != null && cursor.moveToFirst()){
            subTotal = cursor.getDouble(0)
        }

        return subTotal


    }

    fun getDiscounts(): Double{
        var db = readableDatabase
        var discounts = 0.0
        var query = "select sum($COLUMN_QUANTITY * ($COLUMN_MRP - $COLUMN_PRICE)) from $TABLE_NAME"
        var cursor = db.rawQuery(query, null)

        if(cursor != null && cursor.moveToFirst()){
            discounts = cursor.getDouble(0)
        }

        return discounts


    }

    fun getCartItems(): ArrayList<CartItem>{
        var db = readableDatabase
        var cartItemList = ArrayList<CartItem>()

        var cursor =db.query(TABLE_NAME, arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_IMG, COLUMN_QUANTITY, COLUMN_PRICE, COLUMN_MRP), null, null,
        null, null, null, null)

        if(cursor != null && cursor.moveToFirst()){
            do{
                var id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                var name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                var img = cursor.getString(cursor.getColumnIndex(COLUMN_IMG))
                var quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
                var price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                var mrp = cursor.getDouble(cursor.getColumnIndex(COLUMN_MRP))

                cartItemList.add(CartItem(id, name, img, mrp, price, quantity))

            }while(cursor.moveToNext())

        }

        return cartItemList

    }

    fun getOrderProducts(): ArrayList<OrderProduct>{
        var db = readableDatabase
        var orderProductList = ArrayList<OrderProduct>()

        var cursor =db.query(TABLE_NAME, arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_IMG, COLUMN_QUANTITY, COLUMN_PRICE, COLUMN_MRP), null, null,
            null, null, null, null)

        if(cursor != null && cursor.moveToFirst()){
            do{
                var id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                var name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                var img = cursor.getString(cursor.getColumnIndex(COLUMN_IMG))
                var quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
                var price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                var mrp = cursor.getDouble(cursor.getColumnIndex(COLUMN_MRP))

                orderProductList.add(OrderProduct(null,img,mrp,price,quantity))

            }while(cursor.moveToNext())

        }

        return orderProductList

    }

    fun clearTable(){
        var db = writableDatabase
        db.delete(TABLE_NAME, null, null)
    }







}