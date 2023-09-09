package com.example.cheatsheet.QuestionsList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cheatsheet.databinding.FragmentFirstBinding
import com.example.cheatsheet.util.getDataFromExcel
import com.example.cheatsheet.util.loadExcelFile


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class QuestionFragment : Fragment() {

    private val PICK_EXCEL_REQUEST = 100
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

        binding.scrollUp.setOnClickListener {
            binding.recycler.scrollToPosition(0)
            binding.scrollUp.hide()
        }

        viewModel.questionList.observe(viewLifecycleOwner){
            adapter.refresh(it)
        }

        binding.download.setOnClickListener {
            openExcelFile()
        }

        textWatcher = binding.search.doAfterTextChanged {text ->
            viewModel.updateSearch(text.toString())
            binding.cleaButton.isVisible = !text.isNullOrEmpty()
        }

        binding.cleaButton.setOnClickListener {
            binding.search.setText("")
        }


        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Вызывается при каждом скролле

                // Проверьте направление скролла
                if (dy < 0) {
                    // Скролл вверх
                    // Проверьте, видны ли первые 3 элемента
                    val layoutManager = recyclerView.layoutManager
                    if (layoutManager is LinearLayoutManager) {
                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                        if (firstVisibleItemPosition > 3) {
                            // Первые 3 элемента не видны, покажите кнопку
                            binding.scrollUp.show()
                        } else {
                            // Первые 3 элемента видны, скройте кнопку
                            binding.scrollUp.hide()
                        }
                    }
                }

                if (dy > 10){
                    binding.scrollUp.hide()
                }
            }
        })
    }


    private fun openExcelFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") // MIME-тип для файлов Excel (.xlsx)
        startActivityForResult(intent, PICK_EXCEL_REQUEST)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_EXCEL_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val fileUri = data.data
            loadExcelFile(fileUri, requireActivity())
            val list = getDataFromExcel(requireActivity())
            viewModel.setQuestionList(list)
        }
    }


    override fun onDestroy() {
        textWatcher = null
        super.onDestroy()
    }
}