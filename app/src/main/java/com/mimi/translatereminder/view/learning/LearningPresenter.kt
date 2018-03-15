package com.mimi.translatereminder.view.learning

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.repository.TranslationRepository
import com.mimi.translatereminder.repository.local.sharedprefs.SharedPreferencesUtil
import com.mimi.translatereminder.utils.LearningFragmentsGenerator
import com.mimi.translatereminder.utils.StateUtil
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_LEARN_NEW_WORDS
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_LISTENING
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_REVIEW_ITEMS
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_REVIEW_WRONG_WORDS
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.runOnUiThread
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class LearningPresenter : LearningContract.Presenter {
    override lateinit var view: LearningContract.Activity
    override var type: Int = TYPE_LEARN_NEW_WORDS
    override var itemIds: List<Int> = listOf()
    private val fragmentGen = LearningFragmentsGenerator()
    private val fragments = ArrayList<Progress>()
    private val stateUtil = StateUtil()

    private var shouldSpellItems = true
    override val repo: TranslationRepository
        get() = view.getRepository()

    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun start() {
        view.init()
        loadItems()
    }

    private fun loadItems() {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val items = retrieveItems()
            shouldSpellItems = SharedPreferencesUtil().getSpellWords(view.getContext())
            fragments.clear()
            if (type == TYPE_LISTENING) {
                fragments.addAll(fragmentGen.generateListeningFragments(items))
            } else {
                fragments.addAll(fragmentGen.start(items))
            }
            onComplete {
                view.hideLoadingDialog()
                view.setFragments(items = fragments)
                view.moveToFragment(0)
                onFragmentVisible(0)
            }
        }
    }

    private fun retrieveItems(): List<Entity> {
        val items = itemIds.map { repo.selectItemById(it)}
        if (items.isNotEmpty()) {
            return items.filterNotNull()
        }
        return when (type) {
            TYPE_LEARN_NEW_WORDS -> repo.retrieveLearningItems(view.getContext())
            TYPE_REVIEW_ITEMS -> repo.retrieveReviewItems(view.getContext())
            TYPE_REVIEW_WRONG_WORDS -> retrieveWrongItems()
            TYPE_LISTENING -> repo.retrieveListeningItems(view.getContext())
            else -> listOf()
        }
    }

    private fun retrieveWrongItems(): List<Entity> {
        val items = repo.retrieveWrongItems(view.getContext())
        items.forEach { it.state = Entity.firstMistakeState }
        return items
    }

    override fun onFragmentVisible(position: Int) {
        val item = fragments[position]
        if (type == TYPE_LISTENING) {
            view.spellText(item.entity?.germanWord ?: "") {
                //spellTranslation(position, item.entity)
                delayToNextFragment(position)
            }
            return
        }
        val spellTypes = listOf(Progress.TYPE_HINT, Progress.TYPE_CHOOSE_TRANSLATION,
                Progress.TYPE_PRESENT)
        if (item.entity != null && spellTypes.any { it == item.type }
                && shouldSpellItems)
            spell(item.entity!!.germanWord)
    }

    private fun spellTranslation(position: Int, item: Entity?) {
        delay(300){
            view.spellText(item?.translation ?: "") {
                delayToNextFragment(position)
            }
        }
    }

    private fun delayToNextFragment(position: Int) {
        delay(1500){
            if (position < fragments.size - 1)
                moveToNextFragment()
        }
    }

    private fun delay(delay: Long, function: () -> Unit) {
        val timer = Timer()
        timer.schedule(delay) { function() }
    }

    override fun spell(text: String, onFinish: () -> Unit) {
        view.spellText(text, onFinish)
    }

    override fun onFragmentResult(addedScore: Int, entityId: Int?, correct: Boolean) {
        view.getContext().runOnUiThread {
            view.showLoadingDialog()
            doAsync(exceptionHandler) {
                if (entityId != null) {
                    val item = repo.selectItemById(entityId)
                    if (item != null) {
                        if (correct) {
                            if (item.nextReview < Calendar.getInstance().timeInMillis)
                                stateUtil.increaseState(item)
                        } else {
                            stateUtil.setWrong(item)
                        }
                        repo.saveEntity(item)
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
        when {
            currentPosition + 1 < fragments.size -> view.moveToFragment(currentPosition + 1)
            type == TYPE_LISTENING -> view.moveToFragment(currentPosition + 1)
            else -> view.finishActivity()
        }
    }


}