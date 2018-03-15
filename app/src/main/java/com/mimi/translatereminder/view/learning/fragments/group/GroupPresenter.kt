package com.mimi.translatereminder.view.learning.fragments.group

import android.os.Handler
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.learning.LearningContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by Mimi on 18/12/2017.
 *
 */
class GroupPresenter(override var view: GroupContract.View,
                     private val masterPresenter: LearningContract.Presenter,
                     override var type: Int, override var ids: List<Int>) : GroupContract.Presenter {
    private val solutionsMap: HashMap<String, String> = HashMap()
    private val maxPairs = 4
    private val animationDelay = 300L
    private var selectedLeft: GroupItem? = null
    private var selectedRight: GroupItem? = null

    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun start() {
        view.init()
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            reloadData()
            onComplete {
                view.hideLoadingDialog()
                view.refreshText(solutionsMap.keys.toList(), solutionsMap.values.toList())
            }
        }
    }

    override fun onLeftSelected(left: GroupItem) {
        val oldLeft = selectedLeft
        if (oldLeft != null) {
            oldLeft.state = State.Default
            view.refreshLeftItem(oldLeft)
        }
        selectedLeft = left
        left.state = State.Chosen
        view.refreshLeftItem(left)
        if (selectedRight != null) {
            onBothItemsSelected()
        }
    }

    override fun onRightSelected(right: GroupItem) {
        val oldRight = selectedRight
        if (oldRight != null) {
            oldRight.state = State.Default
            view.refreshRightItem(oldRight)
        }
        selectedRight = right
        right.state = State.Chosen
        view.refreshRightItem(right)
        if (selectedLeft != null) {
            onBothItemsSelected()
        }
    }

    private fun onBothItemsSelected() {
        val left = selectedLeft ?: return
        val right = selectedRight ?: return
        val correct = solutionsMap[left.text] == right.text
        if (correct) {
            left.state = State.Correct
            right.state = State.Correct
        } else {
            left.state = State.Wrong
            right.state = State.Wrong
        }
        view.refreshLeftItem(left)
        view.refreshRightItem(right)

        Handler().postDelayed({
            val newState = if (correct) State.NotSelectable else State.Default
            left.state = newState
            right.state = newState
            view.refreshLeftItem(left)
            view.refreshRightItem(right)
            solutionsMap.remove(left.text)
            selectedLeft = null
            selectedRight = null

            checkIfFinished()
        }, animationDelay)
    }

    private fun reloadData() {
        val items = ArrayList<Entity>()
        val repo = masterPresenter.repo
        ids.forEach {
            if (items.size < maxPairs) {
                val item = repo.selectItemById(it)
                if (item != null)
                    items.add(item)
            }
        }
        val all = ArrayList<Entity>()
        all.addAll(repo.getReviewItems(maxPairs * 2))
        if (all.size < maxPairs * 2)
            all.addAll(repo.getOverflowItems(maxPairs * 2 - all.size))
        all.forEach {
            if (!items.contains(it) && items.size < maxPairs)
                items.add(it)
        }
        solutionsMap.clear()
        items.forEach { solutionsMap.put(it.germanWord, it.translation) }

    }


    override fun onVisibleToUser() {
    }

    private fun checkIfFinished() {
        if (solutionsMap.isEmpty())
            masterPresenter.onFragmentResult(10, correct = true)
    }

}
