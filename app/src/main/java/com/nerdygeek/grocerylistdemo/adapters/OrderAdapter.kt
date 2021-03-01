package com.nerdygeek.grocerylistdemo.adapters

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.activities.OrderDetailsActivity
import com.nerdygeek.grocerylistdemo.apps.Config
import com.nerdygeek.grocerylistdemo.models.OrderResponse
import com.nerdygeek.grocerylistdemo.models.OrderSummary
import kotlinx.android.synthetic.main.row_order.view.*

class OrderAdapter(var mContext: Context) : RecyclerView.Adapter<OrderAdapter.ViewHolder>(){

    private var mList = ArrayList<OrderResponse>()
    fun setData(list : ArrayList<OrderResponse>){
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_order, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(orderResponse: OrderResponse){
            itemView.order_date.text = orderResponse.date
            itemView.order_shipment.text = "shipment: $${orderResponse.orderSummary.deliveryCharges}"
            itemView.order_sub_total.text = "subtotal: $${orderResponse.orderSummary.orderAmount}"
            itemView.order_amount.text = "you paid: $${orderResponse.orderSummary.totalAmount}"
            itemView.order_discounts.text = "you saved: $${orderResponse.orderSummary.discount}"
            itemView.order_method.text = "payment method: ${orderResponse.payment.paymentMode}"

            itemView.setOnClickListener{
                var intent = Intent(mContext, OrderDetailsActivity::class.java)

                intent.putExtra(OrderResponse.KEY_ORDER, orderResponse)
                mContext.startActivity(intent)
            }

        }

    }
}