package com.example.foodapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapters.ListAdapter
import com.example.foodapp.databinding.FragmentAreaBinding
import com.example.foodapp.databinding.FragmentIngredientBinding
import com.example.foodapp.models.FilterQuery
import com.example.foodapp.ui.MainActivity
import com.example.foodapp.ui.viewModels.FoodViewModel
import com.example.foodapp.util.Resource

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class AreaFragment : Fragment(R.layout.fragment_area) {

    companion object {
        const val TAG = "AreaFragment"
    }

    private lateinit var viewModel: FoodViewModel
    private lateinit var listAdapter: ListAdapter
    private lateinit var binding: FragmentAreaBinding

//    // TODO: Rename and change types of parameters
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_area, container, false)

        viewModel = (activity as MainActivity).viewModel

        viewModel.getAreaList()

        setUpRecyclerView()

        listAdapter.setOnItemClickListener {
            Log.d(CategoriesFragment.TAG, "Clicked")
            val bundle = Bundle().apply {
                putSerializable("Filter",
                    it.strArea?.let { it1 -> FilterQuery(queryType = "a", query = it1) })
            }
            findNavController().navigate(
                R.id.action_areaFragment_to_filterFragment,
                bundle
            )
        }

        viewModel.filters.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        listAdapter.differ.submitList(it.meals?.toList())
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
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_area, container, false)
    }

    private fun setUpRecyclerView() {
        listAdapter = ListAdapter()
        binding.rvCategories.apply {
            adapter = listAdapter
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
//         * @return A new instance of fragment AreaFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            AreaFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}