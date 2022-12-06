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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapters.CategoriesAdapter
import com.example.foodapp.databinding.FragmentCategoriesBinding
import com.example.foodapp.models.FilterQuery
import com.example.foodapp.ui.MainActivity
import com.example.foodapp.ui.viewModels.FoodViewModel
import com.example.foodapp.util.Resource

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    companion object {
        const val TAG = "CategoriesFragment"
    }

    private lateinit var viewModel: FoodViewModel
    private lateinit var catergoriesAdapter: CategoriesAdapter
    private lateinit var binding: FragmentCategoriesBinding

//    // TODO: Rename and change types of parameters
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false)

        viewModel = (activity as MainActivity).viewModel

        viewModel.getCategories()

        setUpRecyclerView()

        catergoriesAdapter.setOnItemClickListener {
            Log.d(TAG, "Clicked")
            val bundle = Bundle().apply {
                putSerializable("Filter", FilterQuery(queryType = "c", query = it.strCategory))
            }
            findNavController().navigate(
                R.id.action_categoriesFragment_to_filterFragment,
                bundle
            )
        }

        viewModel.categories.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        catergoriesAdapter.differ.submitList(it.categories.toList())
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
//        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    private fun setUpRecyclerView() {
        catergoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            adapter = catergoriesAdapter
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
//         * @return A new instance of fragment CategoriesFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            CategoriesFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}