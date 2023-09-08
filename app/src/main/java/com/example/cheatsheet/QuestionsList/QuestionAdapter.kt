package com.example.cheatsheet.QuestionsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cheatsheet.databinding.ItemQuestionBinding
import com.example.cheatsheet.model.QuestionElement

class QuestionAdapter(): RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    private var items: List<QuestionElement> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.number.text = "Вопрос № ${item.number}"
        holder.binding.question.text = item.question

        val adapter = AnswerAdapter()
        holder.binding.recycler.adapter = adapter
        adapter.refresh(item.answers)

    }

    class ViewHolder(val binding: ItemQuestionBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemQuestionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }

    override fun getItemCount() = items.size

    fun refresh(_items: List<QuestionElement>) {
        items = _items
        notifyDataSetChanged()
    }
}