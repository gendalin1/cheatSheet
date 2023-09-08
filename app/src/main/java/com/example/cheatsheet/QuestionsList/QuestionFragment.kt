package com.example.cheatsheet.QuestionsList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        adapter.refresh(list)

    }
}