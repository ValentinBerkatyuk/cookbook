package com.example.cookbook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.R
import com.example.cookbook.adapters.IngredientAdapter
import com.example.cookbook.models.Result
import com.example.cookbook.util.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_ingredient.view.*


class IngredientFragment : Fragment(R.layout.fragment_ingredient) {

    private val mAdapter: IngredientAdapter by lazy { IngredientAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        setupRecyclerView(view)
        myBundle?.extendedIngredients?.let {
            mAdapter.setData(it)
        }
    }

    private fun setupRecyclerView(view: View) {
        view.ingredient_recyclerView.adapter = mAdapter
        view.ingredient_recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}