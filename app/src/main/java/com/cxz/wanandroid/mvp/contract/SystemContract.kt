package com.cxz.wanandroid.mvp.contract

import com.cxz.wanandroid.base.IModel
import com.cxz.wanandroid.base.IPresenter
import com.cxz.wanandroid.base.IView

/**
 * @author guoyikai
 * @date 2023/11/17
 * @desc
 */
interface SystemContract {

    interface View : IView {
        fun scrollToTop()
    }

    interface Presenter : IPresenter<View> {

    }

    interface Model : IModel {

    }

}