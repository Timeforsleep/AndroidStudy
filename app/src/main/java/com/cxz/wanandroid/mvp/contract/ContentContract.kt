package com.cxz.wanandroid.mvp.contract

/**
 * Created by guoyikai on 2023/6/10.
 */
interface ContentContract {

    interface View : CommonContract.View {

    }

    interface Presenter : CommonContract.Presenter<View> {

    }

    interface Model : CommonContract.Model {

    }

}