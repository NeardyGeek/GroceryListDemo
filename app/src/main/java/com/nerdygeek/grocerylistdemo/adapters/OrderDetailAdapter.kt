package com.nerdygeek.grocerylistdemo.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.apps.Config
import com.nerdygeek.grocerylistdemo.helper.MathHelper
import com.nerdygeek.grocerylistdemo.models.OrderProduct
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.order_detail_row.view.*
import kotlinx.android.synthetic.main.sub_row_item.view.*

class OrderDetailAdapter(var context: Context, var mList: ArrayList<OrderProduct>) : RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(orderProduct: OrderProduct){
            var price = MathHelper.round(orderProduct.price)
            var mrp = MathHelper.round(orderProduct.mrp)
            var img = orderProduct.image
            var qty = orderProduct.quantity
            var discount = mrp - price


            itemView.order_mrp.text = "$$mrp"
            itemView.order_mrp.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.order_price.text = "you paid: $$price"
            itemView.order_qty.text = "quantity: $qty"
            itemView.order_discount.text = "you saved: $discount"

            Picasso.get().load("${Config.IMAGE_URL}${img}").into(itemView.order_row_img_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.order_detail_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}