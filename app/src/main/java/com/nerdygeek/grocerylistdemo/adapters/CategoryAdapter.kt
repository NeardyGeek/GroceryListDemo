package com.nerdygeek.grocerylistdemo.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.activities.SubCategoryActivity
import com.nerdygeek.grocerylistdemo.apps.Config
import com.nerdygeek.grocerylistdemo.models.Category
import com.nerdygeek.grocerylistdemo.models.CategoryResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_item.view.*

class CategoryAdapter(var context: Context) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    var list = ArrayList<Category>()

    fun setData(l: ArrayList<Category>){
        list = l
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var category = list[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return list.size
    }



    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(category: Category){
            itemView.cate_name.text = category.catName
            Picasso.get().load("${Config.IMAGE_URL}${category.catImage}").into(itemView.card_img_view)
            itemView.setOnClickListener {
                var intent = Intent(context, SubCategoryActivity::class.java)
                intent.putExtra(Category.KEY_CATE, category)
                context.startActivity(intent)

            }



        }

    }
}