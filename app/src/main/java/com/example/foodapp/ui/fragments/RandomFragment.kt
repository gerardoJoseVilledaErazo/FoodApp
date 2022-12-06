package com.example.foodapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentRandomBinding
import com.example.foodapp.models.Meal
import com.example.foodapp.ui.MainActivity
import com.example.foodapp.ui.viewModels.FoodViewModel
import com.example.foodapp.util.Resource
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class RandomFragment : Fragment(R.layout.fragment_random) {

    companion object {
        const val TAG = "RecipeFragment"
    }

    private lateinit var viewModel: FoodViewModel
    private lateinit var binding: FragmentRandomBinding
    private lateinit var youTubePlayerView: YouTubePlayerView

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_random, container, false)

        viewModel = (activity as MainActivity).viewModel

        viewModel.getRandomRecipe()

        viewModel.recipes.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        val meal = it.meals?.get(0)
                        setRecipe(meal)
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
//        return inflater.inflate(R.layout.fragment_random, container, false)
    }

    private fun setRecipe(meal: Meal?) {
        Glide.with(binding.root)
            .load(meal?.strMealThumb)
            .into(binding.recipeImage)

        binding.recipeText.text = meal?.strMeal

        binding.recipeIngredient.text = meal?.let { viewModel.getIngredients(it) }

        binding.recipeInstruction.text = meal?.strInstructions

        youTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = meal?.let { viewModel.getVideoId(it) }
                youTubePlayer.pause()
                if (videoId != null) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            }
        })

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
//         * @return A new instance of fragment RandomFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            RandomFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}