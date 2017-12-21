package com.mimi.translatereminder.view.learning

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.repository.local.sharedprefs.SharedPreferencesUtil
import com.mimi.translatereminder.utils.LearningFragmentsGenerator
import com.mimi.translatereminder.utils.StateUtil
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_LEARN_NEW_WORDS
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_REVIEW_ITEMS
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_REVIEW_WRONG_WORDS
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.runOnUiThread

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class LearningPresenter : LearningContract.Presenter {
    override lateinit var view: LearningContract.Activity
    override var type: Int = TYPE_LEARN_NEW_WORDS
    override var itemId: Int? = null
    private val fragmentGen = LearningFragmentsGenerator()
    private val fragments = ArrayList<Progress>()
    private val stateUtil = StateUtil()
    private var shouldSpellItems = true
    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun start() {
        view.init()
        reloadItems()
    }

    override fun getRepository() = view.getRepository()

    private fun reloadItems() {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val items = retrieveItems()
            shouldSpellItems = SharedPreferencesUtil().getSpellWords(view.getContext())
            fragments.clear()
            fragments.addAll(fragmentGen.start(items))
            onComplete {
                view.hideLoadingDialog()
                view.setFragments(items = fragments)
                view.moveToFragment(0)
            }
        }
    }

    private fun retrieveItems(): List<Entity> {
        val repo = view.getRepository()
        val itemId = itemId
        if (itemId != null && itemId != 0) {
            val item = repo.selectItemById(itemId)
            if (item != null)
                return listOf(item)
        }
        return when (type) {
            TYPE_LEARN_NEW_WORDS -> repo.retrieveLearningItems(view.getContext())
            TYPE_REVIEW_ITEMS -> repo.retrieveReviewItems(view.getContext())
            TYPE_REVIEW_WRONG_WORDS -> repo.retrieveWrongItems(view.getContext())
            else -> listOf()
        }
    }

    override fun onFragmentVisible(position: Int) {
        val item = fragments[position]
        val spellTypes = listOf(Progress.TYPE_HINT, Progress.TYPE_CHOOSE_TRANSLATION, Progress.TYPE_PRESENT)
        if (item.entity != null && spellTypes.any { it == item.type }
                && shouldSpellItems)
            view.spellText(item.entity!!.germanWord)
    }

    override fun onFragmentResult(addedScore: Int, entityId: Int?, correct: Boolean) {
        view.getContext().runOnUiThread {
            view.showLoadingDialog()
            doAsync(exceptionHandler) {
                if (entityId != null) {
                    val item = view.getRepository().selectItemById(entityId)
                    if (item != null) {
                        if (correct)
                            stateUtil.increaseState(item)
                        else
                            stateUtil.setWrong(item)
                        view.getRepository().saveEntity(item)
                    }
                }
                onComplete {
                    view.hideLoadingDialog()
                    moveToNextFragment()
                }
            }
        }
    }

    private fun moveToNextFragment() {
        val currentPosition = view.getCurrentFragmentPosition()
        if (currentPosition + 1 < fragments.size)
            view.moveToFragment(currentPosition + 1)
        else
            view.finishActivity()
    }


}