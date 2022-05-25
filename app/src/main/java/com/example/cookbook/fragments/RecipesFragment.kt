package com.example.cookbook.fragments


import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cookbook.MainActivity
import com.example.cookbook.R
import com.example.cookbook.adapters.RecipesAdapter
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.databinding.FragmentRecipesBinding
import com.example.cookbook.util.NetworkListener
import com.example.cookbook.util.NetworkResult
import com.example.cookbook.util.observeOnce
import com.example.cookbook.viewmodels.MainViewModel
import com.example.cookbook.viewmodels.RecipesViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

    private val args by navArgs <RecipesFragmentArgs>()

    private var _binding : FragmentRecipesBinding? = null
    private val binding get()= _binding!!
    private lateinit var mainViewModel : MainViewModel
    private lateinit var recipesViewModel : RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }

    private lateinit var networkListener: NetworkListener


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater,container,false)

        binding.lifecycleOwner=this
        binding.mainViewModel=mainViewModel

        setHasOptionsMenu(true)

        setupRecyclerView()

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkedNetworkAvailability(requireContext())
                .collect{ status ->
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readLocalDatabase()
                }
        }

        val floatButton: MaterialButton = binding.floatingActionButton as MaterialButton
        floatButton.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_bottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }

        return binding.root

    }


    private fun readLocalDatabase(){
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database->
                if(database.isNotEmpty() && !args.backFromBottomSheet) {
                    mAdapter.setData(database[0].foodRecipe)
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData(){
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesGet.observe(viewLifecycleOwner) { response->
            when(response) {
                is NetworkResult.Success-> {
                    response.data?.let{mAdapter.setData(it)}
                }
                is NetworkResult.Error-> {
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.massage.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {

                }
            }

        }
    }


    private fun loadDataFromCache(){
        lifecycleScope.launch{
            mainViewModel.readRecipes.observe(viewLifecycleOwner){database->
                if(database.isNotEmpty()){
                    mAdapter.setData(database[0].foodRecipe)
                }
            }
        }
    }

    private fun setupRecyclerView(){
        binding.recyclerView.adapter=mAdapter
        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }


    private fun searchApiData(searchQuery: String) {
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val foodRecipe = response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.massage.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}







