package com.example.foodapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapters.FilterAdapter
import com.example.foodapp.databinding.FragmentSearchBinding
import com.example.foodapp.ui.MainActivity
import com.example.foodapp.ui.viewModels.FoodViewModel
import com.example.foodapp.util.Constants.Companion.SEARCH_DELAY
import com.example.foodapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment(R.layout.fragment_search) {

    companion object {
        const val TAG = "SearchFragment"
    }

    private lateinit var viewModel: FoodViewModel
    private lateinit var filterAdapter: FilterAdapter
    private lateinit var binding: FragmentSearchBinding

    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        viewModel = (activity as MainActivity).viewModel

        setUpRecyclerView()

        filterAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString("RecipeID", it.idMeal)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_recipeFragment,
                bundle
            )
        }

        var job: Job? = null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_DELAY)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        binding.searchImage.visibility = View.INVISIBLE
                        binding.rvSearchNews.visibility = View.VISIBLE
                        viewModel.search(it.toString())
                    }
                }
            }
        }

        viewModel.filters.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        filterAdapter.differ.submitList(it.meals?.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })


        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    private fun setUpRecyclerView() {
        filterAdapter = FilterAdapter()
        binding.rvSearchNews.apply {
            adapter = filterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment SearchFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            SearchFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}