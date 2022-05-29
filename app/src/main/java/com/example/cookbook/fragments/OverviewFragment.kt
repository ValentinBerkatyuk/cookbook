package com.example.cookbook.fragments

import android.os.Bundle
import android.util.Log
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


class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private var _binding : FragmentOverviewBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val myBundle : Result = args!!.getParcelable<Result>(RECIPE_RESULT_KEY) as Result

        binding.overviewImageView.load(myBundle.image)
        binding.titleTextView.text = myBundle.title
        binding.likesTextView.text = myBundle.aggregateLikes.toString()
        binding.timeTextView.text = myBundle.readyInMinutes.toString()
        RecipeRowBinding.parseHtml(binding.overviewDescriptionTextView,myBundle.description)

        updateColors(myBundle.vegetarian, binding.vegeterianTextView, binding.vegeterianImageView)
        updateColors(myBundle.vegan, binding.veganTextView, binding.veganImageView)
        updateColors(myBundle.cheap, binding.cheapTextView, binding.cheapImageView)
        updateColors(myBundle.dairyFree, binding.dairyFreeTextView, binding.dairyFreeImageView)
        updateColors(myBundle.glutenFree, binding.glutenFreeTextView, binding.glutenFreeImageView)
        updateColors(myBundle.veryHealthy, binding.healthyTextView, binding.healthyImageView)

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