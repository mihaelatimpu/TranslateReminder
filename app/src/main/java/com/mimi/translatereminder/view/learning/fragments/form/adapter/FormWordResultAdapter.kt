package com.mimi.translatereminder.view.learning.fragments.form.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mimi.translatereminder.R
import com.mimi.translatereminder.view.learning.fragments.form.OptionWord
import kotlinx.android.synthetic.main.item_form_word_option.view.*

/**
 * Created by Mimi on 24/02/2018.
 */

class FormWordResultAdapter(val context: Context, private val onSelected: (OptionWord) -> Unit)
    : RecyclerView.Adapter<ResultViewHolder>() {
    private val words = arrayListOf<OptionWord>()

    fun removeWord(word: OptionWord) {
        words.remove(word)
        for (i in 0..words.size)
            notifyItemInserted(i)
    }

    fun addWord(word: OptionWord) {
        if (!words.contains(word)) {
            words.add(word)
            notifyItemInserted(words.indexOf(word))
        }
    }

    fun getAllWords() = words
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    = ResultViewHolder(LayoutInflater.from(context).inflate(R.layout.item_form_word_option, parent, false))

    override fun getItemCount() = words.size

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bindView(words[position], onSelected)
    }
}

class ResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(option: OptionWord, onSelected: (OptionWord) -> Unit) {
        itemView.label.text = option.word
        itemView.setOnClickListener { onSelected(option) }
    }
}