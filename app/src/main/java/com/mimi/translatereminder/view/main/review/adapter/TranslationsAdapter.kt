package com.mimi.translatereminder.view.main.review.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import kotlinx.android.synthetic.main.item_translation.view.*

/**
 * Created by Mimi on 06/12/2017.
 *
 */
class TranslationsAdapter(val context: Context) : RecyclerView.Adapter<TranslationsAdapter.TranslationHolder>() {
    private val translations = arrayListOf<Entity>()
    fun refreshTranslations(newTranslations: List<Entity>) {
        translations.clear()
        translations.addAll(newTranslations)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TranslationHolder, position: Int) {
        holder.bind(translations[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = TranslationHolder(LayoutInflater.from(context).
            inflate(R.layout.item_translation, parent, false))

    override fun getItemCount() = translations.size

    class TranslationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(entity: Entity) {
            itemView.title.text = entity.germanWord
            itemView.subtitle.text = entity.translation
        }
    }
}