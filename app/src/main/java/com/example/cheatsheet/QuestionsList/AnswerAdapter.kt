package com.example.cheatsheet.QuestionsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cheatsheet.databinding.ItemAnswerBinding
import com.example.cheatsheet.model.AnswerItem
import com.example.cheatsheet.model.QuestionElement

class AnswerAdapter(): RecyclerView.Adapter<AnswerAdapter.ViewHolder>() {
    private var items: List<AnswerItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.answer.text = "${position+1}. ${item.text}"
    }

    class ViewHolder(val binding: ItemAnswerBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAnswerBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }

    override fun getItemCount() = items.size

    fun refresh(_items: List<AnswerItem>) {
        items = _items
        notifyDataSetChanged()
    }
}