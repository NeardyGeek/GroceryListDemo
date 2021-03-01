package com.nerdygeek.grocerylistdemo.adapters

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.activities.PaymentActivity
import com.nerdygeek.grocerylistdemo.apps.Endpoint
import com.nerdygeek.grocerylistdemo.models.Address
import kotlinx.android.synthetic.main.row_address.view.*

class AddressRowAdapter(var mContext: Context) : RecyclerView.Adapter<AddressRowAdapter.ViewHolder>() {
    private var mList = ArrayList<Address>()


    fun setData(addressList: ArrayList<Address>){
        mList = addressList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_address, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mList[position])

    }

    override fun getItemCount(): Int {
        return mList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(address: Address){
            itemView.host_name.text = address.name
            itemView.house_no.text = "${address.houseNo} ${address.streetName}"
            itemView.text_city.text = "${address.city}${address.location} ${address.pincode}"
            itemView.host_mobile.text = address.mobile


            itemView.setOnClickListener{
                var intent = Intent(mContext, PaymentActivity::class.java)
                intent.putExtra(Address.KEY_ADDRESS, address)
                mContext.startActivity(intent)

            }

            itemView.button_remove_address.setOnClickListener {
                mList.removeAt(adapterPosition)
                notifyDataSetChanged()

                var requestQueue = Volley.newRequestQueue(mContext)
                var request = StringRequest(
                    Request.Method.DELETE,
                    Endpoint.deleteAddress(address._id),
                    Response.Listener {
                        Toast.makeText(mContext, "delete successfully", Toast.LENGTH_SHORT).show()
                    },
                    Response.ErrorListener {
                        Toast.makeText(mContext, "delete failed", Toast.LENGTH_SHORT).show()
                    }
                )

                requestQueue.add(request)
            }

            itemView.button_edit_address.setOnClickListener {
                Toast.makeText(mContext, "feature working in progress", Toast.LENGTH_SHORT).show()
            }

        }

    }
}