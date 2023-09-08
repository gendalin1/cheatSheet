package com.example.cheatsheet.QuestionsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cheatsheet.R
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

        if (item.isRight){
            holder.binding.answer.setBackgroundColor(holder.itemView.resources.getColor(R.color.masulman_green))
        }
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
        val tourDiffUtilCallback = AnswerDiffCallback(this.items, _items)
        val tourDiffResult = DiffUtil.calculateDiff(tourDiffUtilCallback)
        this.items = _items
        tourDiffResult.dispatchUpdatesTo(this)
    }
}

class AnswerDiffCallback(
    var oldList: List<AnswerItem> = listOf(),
    var newList: List<AnswerItem> = listOf(),
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                oldList[oldItemPosition].text == newList[newItemPosition].text
                )
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                oldList[oldItemPosition].text == newList[newItemPosition].text &&
                        oldList[oldItemPosition].isRight == newList[newItemPosition].isRight
                )
    }
}