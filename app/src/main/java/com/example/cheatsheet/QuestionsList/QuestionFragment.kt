package com.example.cheatsheet.QuestionsList

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.cheatsheet.databinding.FragmentFirstBinding
import com.example.cheatsheet.util.getDataFromExcel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class QuestionFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding

    private val viewModel: QuestionViewModel by viewModels()

    val adapter: QuestionAdapter = QuestionAdapter()

    var textWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFirstBinding.inflate(inflater, container, false)
        binding.recycler.adapter = adapter


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = getDataFromExcel(requireActivity())
        viewModel.setQuestionList(list)

        viewModel.questionList.observe(viewLifecycleOwner){
            adapter.refresh(it)
        }

        binding.scrollUp.setOnClickListener {
            binding.recycler.scrollToPosition(0)
        }

        textWatcher = binding.search.doAfterTextChanged {text ->
            viewModel.updateSearch(text.toString())
            binding.cleaButton.isVisible = !text.isNullOrEmpty()
        }

        binding.cleaButton.setOnClickListener {
            binding.search.setText("")
        }
    }

    override fun onDestroy() {
        textWatcher = null
        super.onDestroy()
    }
}