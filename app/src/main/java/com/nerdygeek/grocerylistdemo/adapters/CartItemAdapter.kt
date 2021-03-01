package com.nerdygeek.grocerylistdemo.adapters

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.activities.CartActivity
import com.nerdygeek.grocerylistdemo.apps.Config
import com.nerdygeek.grocerylistdemo.helper.DBhelper
import com.nerdygeek.grocerylistdemo.helper.MathHelper
import com.nerdygeek.grocerylistdemo.models.CartItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.row_cart_item.view.*
import kotlinx.android.synthetic.main.row_item.view.*

class CartItemAdapter(var mContext: Context,  var mList : ArrayList<CartItem>) : RecyclerView.Adapter<CartItemAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])



    }

    override fun getItemCount(): Int {
        return mList.size
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var dbHelper = DBhelper(mContext)

        fun bind(cartItem: CartItem){
            var name = cartItem.name
            val price = MathHelper.round(cartItem.price)
            var quantity = cartItem.quantity
            var mrp = MathHelper.round(cartItem.mrp)
            var discount = mrp - price

            var singleTotal = price * quantity
            var singleDiscounts = MathHelper.round(discount * quantity)

            itemView.item_name.text = name
            itemView.text_quantity.text = "$quantity"
            itemView.text_single_total.text = "$$singleTotal"
            itemView.text_price.text = "$$price"
            itemView.text_mrp.text = "$$mrp"
            itemView.text_discount.text = "-$$singleDiscounts"

            Picasso.get().load("${Config.IMAGE_URL}${cartItem.image}").into(itemView.cart_row_img_view)

            itemView.button_delete.setOnClickListener{

                mList.removeAt(adapterPosition)
                notifyDataSetChanged()

                dbHelper.deleteCartItem(cartItem)

                mList = dbHelper.getCartItems()
                if(mContext is CartActivity){
                    (mContext as CartActivity).updateSubTotal(dbHelper)
                    (mContext as CartActivity).updateCartCount(dbHelper)
                }

//                if(mContext is CartActivity){
//                    (mContext as CartActivity).updateSubTotal( singleTotal* -1)
//                }


            }

            itemView.plus.setOnClickListener{
                var old = itemView.text_quantity.text.toString().toInt()
                var new = old + 1
                dbHelper.updateCartItem(cartItem._id, new)
                singleTotal = MathHelper.round(price * new)
                singleDiscounts = MathHelper.round(discount * new)


                itemView.text_quantity.text = new.toString()
                itemView.text_single_total.text = "$$singleTotal"
                itemView.text_discount.text = "-$$singleDiscounts"
                if(mContext is CartActivity){
                    (mContext as CartActivity).updateSubTotal(dbHelper)
                    (mContext as CartActivity).updateCartCount(dbHelper)
                }

//                if(mContext is CartActivity){
//                    (mContext as CartActivity).updateSubTotal(price)
//                }



            }

            itemView.minus.setOnClickListener{
                var old = itemView.text_quantity.text.toString().toInt()
                var new = old - 1

                if(new >= 1){
                    dbHelper.updateCartItem(cartItem._id, new)
                    singleTotal = MathHelper.round(price * new)
                    singleDiscounts = MathHelper.round(discount * new)

                    itemView.text_quantity.text = new.toString()
                    itemView.text_single_total.text = "$$singleTotal"
                    itemView.text_discount.text = "-$$singleDiscounts"

                    if(mContext is CartActivity){
                        (mContext as CartActivity).updateSubTotal(dbHelper)
                        (mContext as CartActivity).updateCartCount(dbHelper)

                    }

//                    if(mContext is CartActivity){
//                        (mContext as CartActivity).updateSubTotal(price * -1)
//                    }




                }

            }

        }

    }
}