package com.example.cookbook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.example.cookbook.R
import com.example.cookbook.bindingadapters.RecipeRowBinding
import com.example.cookbook.databinding.FragmentOverviewBinding
import com.example.cookbook.models.Result
import com.example.cookbook.util.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_overview.view.*
import kotlinx.android.synthetic.main.fragment_overview.view.time_textView
import kotlinx.android.synthetic.main.recipes_row.view.*
import org.jsoup.Jsoup


class Overview : Fragment() {

    private var _binding : FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments

        val myBundle : Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        binding.overviewImageView.load(myBundle?.image)
        binding.titleTextView.text = myBundle?.title
        binding.likesTextView.text = myBundle?.aggregateLikes.toString()
        binding.timeTextView.text = myBundle?.readyInMinutes.toString()
        RecipeRowBinding.parseHtml(binding.overviewDescriptionTextView,myBundle?.description)

        myBundle?.vegetarian?.let { updateColors(it, binding.vegeterianTextView, binding.vegeterianImageView) }
        myBundle?.vegan?.let {updateColors(it, binding.veganTextView, binding.veganImageView) }
        myBundle?.cheap?.let { updateColors(it, binding.cheapTextView, binding.cheapImageView) }
        myBundle?.dairyFree?.let {updateColors(it, binding.dairyFreeTextView, binding.dairyFreeImageView) }
        myBundle?.glutenFree?.let {updateColors(it, binding.glutenFreeTextView, binding.glutenFreeImageView) }
        myBundle?.veryHealthy?.let { updateColors(it, binding.healthyTextView, binding.healthyImageView) }

        return binding.root
    }

   private fun updateColors(stateIsOn: Boolean, textView: TextView, imageView:ImageView){
       if (stateIsOn){
           imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
           textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
       }
   }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}