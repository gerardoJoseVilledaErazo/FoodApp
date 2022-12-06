package com.example.foodapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapters.FilterAdapter
import com.example.foodapp.databinding.FragmentFilterBinding
import com.example.foodapp.ui.MainActivity
import com.example.foodapp.ui.viewModels.FoodViewModel
import com.example.foodapp.util.Resource

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class FilterFragment : Fragment(R.layout.fragment_filter) {

    companion object {
        const val TAG = "FilterFragment"
    }

    private lateinit var viewModel: FoodViewModel
    private lateinit var filterAdapter: FilterAdapter
    private lateinit var binding: FragmentFilterBinding
    private val args: FilterFragmentArgs by navArgs()

    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false)

        viewModel = (activity as MainActivity).viewModel

        setUpRecyclerView()

        val filter = args.filter

        when (filter.queryType) {
            "a" -> {
                viewModel.getFilterByArea(filter.query)
            }
            "c" -> {
                viewModel.getFilterByCategory(filter.query)
            }
            "i" -> {
                viewModel.getFilterByIngredient(filter.query)
            }
        }

        Log.d(TAG, "Inside filter fragment")

        filterAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString("RecipeID", it.idMeal)
            }
            findNavController().navigate(
                R.id.action_filterFragment_to_recipeFragment,
                bundle
            )
        }

        viewModel.filters.observe(viewLifecycleOwner, Observer { response ->
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
//        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    private fun setUpRecyclerView() {
        filterAdapter = FilterAdapter()
        binding.rvCategories.apply {
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
//         * @return A new instance of fragment FilterFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            FilterFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}