package com.example.cheatsheet.QuestionsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
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
        val tourDiffUtilCallback = QuestionDiffCallback(this.items, _items)
        val tourDiffResult = DiffUtil.calculateDiff(tourDiffUtilCallback)
        this.items = _items
        tourDiffResult.dispatchUpdatesTo(this)
    }
}

class QuestionDiffCallback(
    var oldList: List<QuestionElement> = listOf(),
    var newList: List<QuestionElement> = listOf(),
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                oldList[oldItemPosition].number == newList[newItemPosition].number
                )
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                oldList[oldItemPosition].question == newList[newItemPosition].question &&
                        oldList[oldItemPosition].answers == newList[newItemPosition].answers
                )
    }
}