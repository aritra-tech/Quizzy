package com.example.quizapptask.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.quizapptask.R
import com.example.quizapptask.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private val options = ArrayList<TextView>()
    private var mSelectedOption: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentCategoryBinding.inflate(layoutInflater,container,false)
        options.add(0,binding.tvOptionAnimals)
        options.add(1,binding.tvOptionHistory)
        options.add(2,binding.tvOptionSports)
        options.add(3,binding.tvOptionMathematics)
        options.add(4,binding.tvOptionGk)

        binding.btDone.setOnClickListener{
            if(mSelectedOption == ""){
                Toast.makeText(context,"Please select any Category",Toast.LENGTH_SHORT).show()
            }
            else {
                val action = CategoryFragmentDirections.actionCategoryFragmentToDifficultyFragment(mSelectedOption)
                findNavController().navigate(action)
            }
        }
        binding.tvOptionAnimals.setOnClickListener{
            defaultOptionView()
            mSelectedOption = "Animals"
            it.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.selected_option
            )
        }
        binding.tvOptionHistory.setOnClickListener {
            defaultOptionView()
            mSelectedOption = "History"
            it.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.selected_option
            )
        }
        binding.tvOptionSports.setOnClickListener {
            defaultOptionView()
            mSelectedOption = "Sports"
            it.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.selected_option
            )
        }
        binding.tvOptionMathematics.setOnClickListener {
            defaultOptionView()
            mSelectedOption = "Mathematics"
            it.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.selected_option
            )
        }
        binding.tvOptionGk.setOnClickListener {
            defaultOptionView()
            mSelectedOption = "General Knowledge"
            it.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.selected_option
            )
        }
        return binding.root
    }
    fun defaultOptionView(){
        mSelectedOption = ""
        for(option in options){
            option.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.cardview
            )
        }
    }
}