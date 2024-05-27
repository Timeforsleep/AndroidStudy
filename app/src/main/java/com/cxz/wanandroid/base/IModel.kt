package com.cxz.wanandroid.base

import io.reactivex.disposables.Disposable

/**
 * Created by guoyikai on 2023/8/18.
 */
interface IModel {

    fun addDisposable(disposable: Disposable?)

    fun onDetach()

}