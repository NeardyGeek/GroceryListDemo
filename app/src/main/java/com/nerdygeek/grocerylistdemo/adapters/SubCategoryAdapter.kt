package com.nerdygeek.grocerylistdemo.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.activities.ProductActivity
import com.nerdygeek.grocerylistdemo.activities.SubCategoryActivity
import com.nerdygeek.grocerylistdemo.apps.Config
import com.nerdygeek.grocerylistdemo.helper.MathHelper
import com.nerdygeek.grocerylistdemo.models.Product
import com.nerdygeek.grocerylistdemo.models.SubCategory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.sub_row_item.view.*

class SubCategoryAdapter(var context: Context): RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder>() {

    var mList = ArrayList<Product>()

    fun setData(list: ArrayList<Product>){
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.sub_row_item, parent, false)
        return SubCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        var product = mList[position]
        holder.bind(product)


    }

    override fun getItemCount(): Int {
        return mList.size
    }


    inner class SubCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(product: Product){
            var price = product.price
            var mrp = product.mrp
            var discount = mrp - price
            itemView.text_sub_name.text = product.productName
            itemView.text_sub_price.text = "$${MathHelper.round(price)}"
            itemView.text_sub_discount.text = "-$${MathHelper.round(discount)}"
            itemView.text_sub_mrp.text = "$${MathHelper.round(mrp)}"
            itemView.text_sub_mrp.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            Picasso.get().load("${Config.IMAGE_URL}${product.image}").into(itemView.sub_row_img_view)

            itemView.setOnClickListener {
                var intent = Intent(context, ProductActivity::class.java)
                intent.putExtra(Product.KEY_PRODUCT, product)

                context.startActivity(intent)

            }


        }

    }
}