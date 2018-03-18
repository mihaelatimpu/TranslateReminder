package com.mimi.translatereminder.view.addedit

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import kotlinx.android.synthetic.main.item_sentence.view.*

/**
 * Created by Mimi on 08/02/2018.
 */
class SentenceAdapter(val context: Context, private val delete: (Entity) -> Unit,
                      private val edit: (Entity) -> Unit) : RecyclerView.Adapter<SentenceViewHolder>() {
    private val sentences = ArrayList<Entity>()
    fun refreshSentences(newItems: List<Entity>) {
        sentences.clear()
        sentences.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentenceViewHolder {
        return SentenceViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sentence, parent, false))
    }

    override fun getItemCount() = sentences.size

    override fun onBindViewHolder(holder: SentenceViewHolder, position: Int) {
        holder.bind(sentences[position], delete,edit)
    }

}

class SentenceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(sentence: Entity, delete: (Entity) -> Unit, edit: (Entity) -> Unit) {
        itemView.label.text = sentence.germanWord
        itemView.imageDelete.setOnClickListener {
            delete(sentence)
        }
        itemView.setOnClickListener { edit(sentence) }
    }
}