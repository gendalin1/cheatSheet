package com.example.cheatsheet.QuestionsList

import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cheatsheet.model.QuestionElement
import java.util.Locale


class QuestionViewModel: ViewModel() {

    var allQuestions: List<QuestionElement> = listOf()

    private var _questionList = MutableLiveData<List<QuestionElement>>()
    var questionList:LiveData<List<QuestionElement>> = _questionList

    var search: String = ""

    fun setQuestionList(_list: List<QuestionElement>){
        allQuestions = _list
        _questionList.postValue(updateData(_list))
    }

    fun updateSearch(_search: String ){
        search = _search
        _questionList.postValue(updateData())
    }

    fun updateData(_list: List<QuestionElement> = allQuestions):List<QuestionElement>{

        val filteredList = mutableListOf<QuestionElement>()

        for (question in _list.iterator()){
            if (checkNameContainFilter(question)){
                Log.e("AAA","блэт")
                filteredList.add(question)
            }
        }
        return filteredList
    }

    private fun checkNameContainFilter(questionElement: QuestionElement): Boolean {
        val filteredSearch = filterString(search)

        val filteredNumber = filterString(questionElement.number.toString())
        if (filteredNumber.contains(filteredSearch))
            return true

        val filteredName = filterString(questionElement.question)
        if (filteredName.contains(filteredSearch))
            return true

        questionElement.answers.forEach{
            val filteredAnswer = filterString(it.text)
            if (filteredAnswer.contains(filteredSearch))
                return true
        }

        return false
    }

    fun filterString(string: String): String{
        return string.dropWhile { it == ' ' }.dropLastWhile { it == ' '}.toLowerCase(Locale.getDefault()).replace(Regex("[ё${235.toChar()}]", RegexOption.IGNORE_CASE), "е")
    }
}