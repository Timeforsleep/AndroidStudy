package com.cxz.wanandroid.mvp.presenter

import ArticleDetailBean
import DBHelper
import android.util.Log
import com.cxz.wanandroid.app.App
import com.cxz.wanandroid.ext.ss
import com.cxz.wanandroid.mvp.contract.HomeContract
import com.cxz.wanandroid.mvp.model.HomeModel
import com.cxz.wanandroid.mvp.model.bean.Article
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.utils.SettingUtil
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

/**
 * Created by guoyikai on 2023/4/21.
 */
class HomePresenter : CommonPresenter<HomeContract.Model, HomeContract.View>(), HomeContract.Presenter {

    override fun createModel(): HomeContract.Model? = HomeModel()

    override fun requestBanner() {
        mModel?.requestBanner()?.ss(mModel, mView, false) {
            mView?.setBanner(it.data)
        }
    }

    override fun requestArticles(num: Int) {
        mModel?.requestArticles(num)?.ss(mModel, mView, num == 0) {
            mView?.setArticles(it.data)
        }
    }

    override fun requestHomeData() {

        requestBanner()

        val observable = if (SettingUtil.getIsShowTopArticle()) {
            mModel?.requestArticles(0)
        } else {
            Observable.zip(mModel?.requestTopArticles(), mModel?.requestArticles(0),
                    BiFunction<HttpResult<MutableList<Article>>, HttpResult<ArticleResponseBody>,
                            HttpResult<ArticleResponseBody>> { t1, t2 ->
                        t1.data.forEach {
                            // 置顶数据中没有标识，手动添加一个标识
                            it.top = "1"
                        }
                        t2.data.datas.addAll(0, t1.data)
                        t2
                    })
        }
        observable?.ss(mModel, mView, false) {
            mView?.setArticles(it.data)
            Log.w("gyk", "requestHomeData: ", )
            val dbHelper = DBHelper.getInstance(App.context)
            it.data.datas.forEach {
                dbHelper.insertArticle(ArticleDetailBean(
                    author = it.author,
                    fresh = it.fresh,
                    articleId = it.id,
                    link = it.link,
                    niceDate = it.niceDate,
                    shareUser = it.shareUser,
                    title = it.title,
                    superChapterId = it.superChapterId,
                    superChapterName = it.superChapterName,
                    collect = it.collect
                ))
            }

        }
    }

}