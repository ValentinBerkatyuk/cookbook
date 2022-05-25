package com.example.cookbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cookbook.R
import com.example.cookbook.models.ExtendedIngredient
import com.example.cookbook.util.Constants.Companion.BASE_IMAGE_URL
import com.example.cookbook.util.FoodDiffUtil
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*
import java.util.*

class IngredientAdapter:RecyclerView.Adapter<IngredientAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent : ViewGroup,viewType: Int) : MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredients_row_layout,parent,false))
    }

    override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
        holder.itemView.apply {
            ingredient_imageView.load(BASE_IMAGE_URL + ingredientsList[position].image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
            ingredient_name.text = ingredientsList[position].name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
            ingredient_amount.text = ingredientsList[position].amount.toString()
            ingredient_consistency.text = ingredientsList[position].consistency
            ingredient_original.text = ingredientsList[position].original
            ingredient_unit.text = ingredientsList[position].unit
        }
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredient : List<ExtendedIngredient>) {
        val ingredientDiffUtil = FoodDiffUtil(ingredientsList,newIngredient)
        val diffUtil = DiffUtil.calculateDiff(ingredientDiffUtil)
        ingredientsList = newIngredient
        diffUtil.dispatchUpdatesTo(this)

    }
}