package com.mimi.translatereminder.view.learning.fragments.form.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mimi.translatereminder.R
import com.mimi.translatereminder.view.learning.fragments.form.OptionWord
import kotlinx.android.synthetic.main.item_form_word_option.view.*
import org.jetbrains.anko.textColor

/**
 * Created by Mimi on 24/02/2018.
 *
 */
class FormWordOptionAdapter(val context: Context, private val onSelected: (OptionWord) -> Unit) : RecyclerView.Adapter<ViewHolder>() {
    private val words = arrayListOf<OptionWord>()

    fun refreshWords(newWords: List<OptionWord>) {
        words.clear()
        words.addAll(newWords)
        for (i in 0..words.size)
            notifyItemInserted(i)
    }

    fun removeWord(word: OptionWord) {
        word.selected = true
        notifyItemChanged(words.indexOf(word))
    }

    fun addWord(word: OptionWord) {
        word.selected = false
        notifyItemChanged(words.indexOf(word))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_form_word_option, parent, false))

    override fun getItemCount() = words.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(words[position], onSelected)
    }
}


class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(option: OptionWord, onSelected: (OptionWord) -> Unit) {
        itemView.label.text = option.word
        val color = if (option.selected) R.color.grey else R.color.colorPrimary
        itemView.label.textColor = ContextCompat.getColor(itemView.context, color)
        itemView.setOnClickListener {
            onSelected(option)
        }
    }
}