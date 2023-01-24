package com.example.quizapptask.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quizapptask.R
import com.example.quizapptask.api.RetrofitInstance
import com.example.quizapptask.databinding.FragmentQuizBinding
import retrofit2.HttpException
import java.io.IOException


class QuizFragment : Fragment() {
    lateinit var binding: FragmentQuizBinding
    private var mCurrentQuestion : Int = 1
    private var mQuestionList = arrayListOf<String>()
    private var mOptionList = arrayListOf<ArrayList<String>>()
    private var mAnswerList = arrayListOf<String>()
    private var mSelectedOption : Int = 0
    private var mCorrectAnswer : Int = 0
    private var mapCategoryToCode = mapOf("General Knowledge" to 9, "Mathematics" to 19, "Sports" to 21, "History" to 23, "Animals" to 27)
    private val options = ArrayList<TextView>()
    private val myArgs: QuizFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(layoutInflater,container,false)
        options.add(0,binding.tvOption1)
        options.add(1,binding.tvOption2)
        options.add(2,binding.tvOption3)
        options.add(0,binding.tvOption4)
        binding.tvOption1.setOnClickListener {
            if(binding.btDone.text != "Finish" && binding.btDone.text != "Next") {
                defaultOptionView()
                it.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.selected_option
                )
                mSelectedOption = 1
            }
        }
        binding.tvOption2.setOnClickListener {
            if(binding.btDone.text != "Finish" && binding.btDone.text != "Next") {
                defaultOptionView()
                it.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.selected_option
                )
                mSelectedOption = 2
            }
        }
        binding.tvOption3.setOnClickListener {
            if(binding.btDone.text != "Finish" && binding.btDone.text != "Next") {
                defaultOptionView()
                it.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.selected_option
                )
                mSelectedOption = 3
            }
        }
        binding.tvOption4.setOnClickListener {
            if(binding.btDone.text != "Finish" && binding.btDone.text != "Next") {
                defaultOptionView()
                it.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.selected_option
                )
                mSelectedOption = 4
            }
        }
        lifecycleScope.launchWhenCreated {
            val response = try{
                Log.d("url", "/api.php?amount=10&category=" + mapCategoryToCode[myArgs.category].toString() + "&difficulty=" + myArgs.difficulty + "&type=multiple")
                RetrofitInstance.api.getQuestions("/api.php?amount=10&category=" + mapCategoryToCode[myArgs.category].toString() + "&difficulty=" + myArgs.difficulty + "&type=multiple")
            }catch (e: IOException){
                Log.e("QuizFragment", "IOException, you might not have internet connection")
                return@launchWhenCreated
            }catch (e: HttpException){
                Log.e("QuizFragment", "HttpException, unexpected response")
                return@launchWhenCreated
            }
            var temp = 0;
            for(item in response.body()?.results!!){
                mQuestionList.add(item.question)
                mAnswerList.add(item.correct_answer)
                val optionList = arrayListOf<String>(item.correct_answer)
                for(item1 in item.incorrect_answers){
                    optionList.add(item1)
                }
                val store = optionList[temp]
                optionList[temp] = optionList[0]
                optionList[0] = store
                mOptionList.add(optionList)
                temp++
                temp %= 4
            }
            binding.progressBar.visibility = View.INVISIBLE
            binding.tvFetchingQuestions.visibility = View.INVISIBLE
            binding.btDone.visibility = View.VISIBLE
            binding.linearLayoutOptions.visibility = View.VISIBLE
            binding.tvQuestions.visibility = View.VISIBLE
            binding.progressBarQuestions.progress = mCurrentQuestion
            binding.tvProgress.text = "$mCurrentQuestion" + "/10"
            binding.llProgressDetails.visibility = View.VISIBLE
            setQuestion()
            Log.d("Api result", response.body()?.results.toString())
            Log.d("option list", mOptionList.toString())
            Log.d("Question list", mQuestionList.toString())
        }
        binding.btDone.setOnClickListener{
            if(binding.btDone.text == "Finish"){
                val action = QuizFragmentDirections.actionQuizFragmentToResultFragment(myargs = mCorrectAnswer)
                findNavController().navigate(action)
            }
            else if(binding.btDone.text == "Next"){
                setQuestion()
                mSelectedOption = 0
            }
            else{
                if(mSelectedOption == 0){
                    Toast.makeText(context,"Select any option",Toast.LENGTH_LONG).show()
                }
                else if(mCurrentQuestion > 10){
                    Toast.makeText(context,"Finish",Toast.LENGTH_LONG).show()
                }
                else{
                    if(mOptionList[mCurrentQuestion-1][mSelectedOption-1] == mAnswerList[mCurrentQuestion-1]){
                        mCorrectAnswer++;
                        mCurrentQuestion++;
                        answerView(mSelectedOption,R.drawable.correct_option)
                        if(mCurrentQuestion > 10)
                            binding.btDone.text = "Finish"
                        else
                            binding.btDone.text = "Next"
                        mSelectedOption = 0
                    }
                    else{
                        var mCorrectOptionNumber:Int = 0
                        for(item in mOptionList[mCurrentQuestion-1]){
                            mCorrectOptionNumber++;
                            if(item == mAnswerList[mCurrentQuestion-1]){
                                break;
                            }
                        }
                        answerView(mCorrectOptionNumber,R.drawable.correct_option)
                        answerView(mSelectedOption,R.drawable.incorrect_option)
                        mCurrentQuestion++;
                        if(mCurrentQuestion > 10)
                            binding.btDone.text = "Finish"
                        else
                            binding.btDone.text = "Next"
                        mSelectedOption = 0
                    }

                }
            }
        }
        return binding.root
    }
    private fun setQuestion(){
        defaultOptionView()
        if(mCurrentQuestion == mQuestionList.size){
            binding.btDone.text = "Finish"
        }
        else{
            binding.btDone.text = "Submit"
        }
        binding.tvQuestions.text = mQuestionList[mCurrentQuestion-1]
        binding.tvOption1.text = mOptionList[mCurrentQuestion-1][0]
        binding.tvOption2.text = mOptionList[mCurrentQuestion-1][1]
        binding.tvOption3.text = mOptionList[mCurrentQuestion-1][2]
        binding.tvOption4.text = mOptionList[mCurrentQuestion-1][3]
        binding.progressBarQuestions.progress = mCurrentQuestion
        binding.tvProgress.text = "$mCurrentQuestion" + "/10"
    }
    fun defaultOptionView(){
        for(option in options){
            option.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.cardview
            )
        }
    }
    private fun answerView(answer: Int, drawableView:Int){
        when(answer){
            1->{
                binding.tvOption1.background = ContextCompat.getDrawable(
                    requireContext(),drawableView
                )
            }
            2->{
                binding.tvOption2.background = ContextCompat.getDrawable(
                    requireContext(),drawableView
                )
            }
            3->{
                binding.tvOption3.background = ContextCompat.getDrawable(
                    requireContext(),drawableView
                )
            }
            4->{
                binding.tvOption4.background = ContextCompat.getDrawable(
                    requireContext(),drawableView
                )
            }
        }
    }
}