package com.example.cookbook.fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cookbook.R
import com.example.cookbook.adapters.FavoriteRecipesAdapter
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.databinding.FragmentFavoriteRecipesBinding
import com.example.cookbook.viewmodels.MainViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private val mAdapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(requireActivity(), mainViewModel) }
    private val mainViewModel: MainViewModel by viewModels()
    private var _binding : FragmentFavoriteRecipesBinding? = null
    private val binding get()= _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter
        setHasOptionsMenu(true)
        setupRecyclerView(binding.favoriteRecipesRecyclerView)

        val button : MaterialButton = binding.favoriteBottomSheet as MaterialButton

        button.setOnClickListener {
            findNavController().navigate(R.id.action_favoriteFragment_to_bottomSheet)
        }

        return binding.root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.deleteAll_favorite_recipes_menu){
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar(){
        Snackbar.make(
            binding.root,
            "All recipes removed.",
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}