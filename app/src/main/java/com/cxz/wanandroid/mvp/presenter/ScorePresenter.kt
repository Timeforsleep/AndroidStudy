package com.cxz.wanandroid.mvp.presenter

import com.cxz.wanandroid.base.BasePresenter
import com.cxz.wanandroid.ext.ss
import com.cxz.wanandroid.mvp.contract.ScoreContract
import com.cxz.wanandroid.mvp.model.ScoreModel

/**
 * @author guoyikai
 * @date 2023/9/5
 * @desc
 */
class ScorePresenter : BasePresenter<ScoreContract.Model, ScoreContract.View>(), ScoreContract.Presenter {

    override fun createModel(): ScoreContract.Model? = ScoreModel()

    override fun getUserScoreList(page: Int) {
        mModel?.getUserScoreList(page)?.ss(mModel, mView) {
            mView?.showUserScoreList(it.data)
        }
    }
}