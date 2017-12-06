package com.mimi.translatereminder.view.main.favourites

import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.view.main.MainContract

/**
 * Created by Mimi on 06/12/2017.
 *
 */

interface FavouritesContract {
    interface View : BaseView<Presenter>
    interface Presenter : MainContract.FragmentPresenter<View>
}