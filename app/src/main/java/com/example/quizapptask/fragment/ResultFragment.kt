package com.example.quizapptask.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quizapptask.R
import com.example.quizapptask.databinding.FragmentResultBinding


class ResultFragment : Fragment() {

    lateinit var binding: FragmentResultBinding
    private val args: ResultFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(layoutInflater,container,false)
        val correctAnswers = args.myargs
        binding.tvCorrectAnswers.text = "Your score is: $correctAnswers"
        binding.btAgain.setOnClickListener{
            findNavController().navigate(R.id.action_resultFragment_to_categoryFragment)
        }
        return binding.root
    }

}