package com.nerdygeek.grocerylistdemo.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.adapters.SubCategoryAdapter
import com.nerdygeek.grocerylistdemo.apps.Endpoint
import com.nerdygeek.grocerylistdemo.models.CategoryResponse
import com.nerdygeek.grocerylistdemo.models.Product
import com.nerdygeek.grocerylistdemo.models.ProductResponse
import com.nerdygeek.grocerylistdemo.models.SubCategory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sub_category.view.*


class SubCategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var subId = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subId = it.getInt(SubCategory.KEY_SUBCATE)

        }
    }

    private fun generateData(subId: Int, view: View, scd: SubCategoryAdapter) {


        var requestQueue = Volley.newRequestQueue(view.context)

        var request = StringRequest(
            Request.Method.GET,
            Endpoint.getProductBySubId(subId),
            Response.Listener {
                view.sub_frag_progress_bar.visibility = View.GONE
                var gson = Gson()
                var productResponse = gson.fromJson(it, ProductResponse::class.java)
                scd.setData(productResponse.data)

            },
            Response.ErrorListener {
                Log.d("gld", it.toString())

            }
        )

        requestQueue.add(request)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_sub_category, container, false)

        var subCategoryAdapter = SubCategoryAdapter(view.context)
        generateData(subId, view, subCategoryAdapter)
        view.frag_sub_recycler_view.adapter = subCategoryAdapter
        view.frag_sub_recycler_view.layoutManager = LinearLayoutManager(view.context)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(subId: Int) =
            SubCategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(SubCategory.KEY_SUBCATE, subId)
                }
            }
    }
}