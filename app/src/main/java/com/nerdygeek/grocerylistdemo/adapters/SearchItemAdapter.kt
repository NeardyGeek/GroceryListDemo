package com.nerdygeek.grocerylistdemo.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.activities.ProductActivity
import com.nerdygeek.grocerylistdemo.apps.Config
import com.nerdygeek.grocerylistdemo.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.sub_row_item.view.*
import java.util.*
import java.util.logging.Filter
import java.util.logging.LogRecord
import kotlin.collections.ArrayList

class SearchItemAdapter(var context: Context): RecyclerView.Adapter<SearchItemAdapter.ViewHolder>(), Filterable {

    private var mList = ArrayList<Product>()
    private var filteredList = ArrayList<Product>()




    fun setData(productList: ArrayList<Product>){
        mList = productList
        filteredList = mList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(product: Product){
            var discount = product.mrp - product.price
            itemView.text_sub_name.text = product.productName
            itemView.text_sub_price.text = "$${product.price.toInt()}"
            itemView.text_sub_discount.text = "-$${discount.toInt()}"
            itemView.text_sub_mrp.text = "$${product.mrp.toInt()}"
            itemView.text_sub_mrp.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            Picasso.get().load("${Config.IMAGE_URL}${product.image}").into(itemView.sub_row_img_view)

            itemView.setOnClickListener {
                var intent = Intent(context, ProductActivity::class.java)
                intent.putExtra(Product.KEY_PRODUCT, product)
                intent.putExtra("discount", discount)
                context.startActivity(intent)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.sub_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(input: CharSequence?): FilterResults {
                val query = input.toString()
                filteredList = if(query.isBlank()){
                    mList
                }else{
                    val resultList = ArrayList<Product>()
                    for(row in mList){
                        val name = row.productName.toLowerCase(Locale.ROOT)
                        if(name.contains(query.toLowerCase(Locale.ROOT))){
                            resultList.add(row)
                        }
                    }

                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults

            }

            override fun publishResults(input: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<Product>
                notifyDataSetChanged()
            }

        }
    }


}