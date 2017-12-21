package com.mimi.translatereminder.view.learning.fragments.group

import android.os.Handler
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.dto.Progress
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
    private val map: HashMap<String, String> = HashMap()
    private val maxPairs = 4
    private val animationDelay = 300L
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
                view.refreshText(map.keys.toList(), map.values.toList())
            }
        }
    }

    private fun reloadData() {
        val items = ArrayList<Entity>()
        val repo = masterPresenter.getRepository()
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
        map.clear()
        items.forEach { map.put(it.germanWord, it.translation) }

    }

    override fun onSelected(left: String, right: String) {
            masterPresenter.spell(left)
        if (map[left] == right) {
            map.remove(left)
            showRightAnimation(left, right)
        } else {
            showWrongAnimation(left, right)
        }
    }

    override fun onVisibleToUser() {
    }

    private fun showRightAnimation(left: String, right: String) {
        view.makeButtonsInactive(left, right)
        view.changeBackground(left, right, R.color.green)
        Handler().postDelayed({
            view.changeBackground(left, right, R.color.grey)
            checkIfFinished()
        }, animationDelay)
    }

    private fun checkIfFinished() {
        if (map.isEmpty())
            masterPresenter.onFragmentResult(10, correct = true)
    }

    private fun showWrongAnimation(left: String, right: String) {
        view.changeBackground(left, right, R.color.red)
        Handler().postDelayed({
            view.changeBackground(left, right, R.color.colorPrimaryDark)
        }, animationDelay)
    }

}
