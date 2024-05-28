package com.cxz.wanandroid.ui.setting

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import androidx.appcompat.app.AlertDialog
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.cxz.wanandroid.R
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.RefreshHomeEvent
import com.cxz.wanandroid.ext.showSnackMsg
import com.cxz.wanandroid.rx.SchedulerUtils
import com.cxz.wanandroid.ui.activity.CommonActivity
import com.cxz.wanandroid.ui.activity.ContentActivity
import com.cxz.wanandroid.utils.CacheDataUtil
import com.cxz.wanandroid.widget.IconPreference
import com.tencent.bugly.beta.Beta
import io.reactivex.Observable
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

/**
 * Created by guoyikai on 2023/6/13.
 */
class SettingFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var context: SettingActivity? = null
    private lateinit var colorPreview: IconPreference

    companion object {
        fun getInstance(): SettingFragment {
            return SettingFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_setting)
        setHasOptionsMenu(true)
        context = activity as SettingActivity

        colorPreview = findPreference("color") as IconPreference

        setDefaultText()

        findPreference("switch_show_top").setOnPreferenceChangeListener { preference, newValue ->
            // 通知首页刷新数据
            // 延迟发送通知：为了保证刷新数据时 SettingUtil.getIsShowTopArticle() 得到最新的值
            Observable.timer(100, TimeUnit.MILLISECONDS)
                .compose(SchedulerUtils.ioToMain())
                .subscribe({
                    EventBus.getDefault().post(RefreshHomeEvent(true))
                }, {})
            true
        }

        findPreference("auto_nightMode").setOnPreferenceClickListener {
            context?.startWithFragment(AutoNightModeFragment::class.java.name, null, null, 0, null)
            true
        }

        findPreference("color").setOnPreferenceClickListener {
            ColorChooserDialog.Builder(context!!, R.string.choose_theme_color)
                .backButton(R.string.back)
                .cancelButton(R.string.cancel)
                .doneButton(R.string.done)
                .customButton(R.string.custom)
                .presetsButton(R.string.back)
                .allowUserColorInputAlpha(false)
                .show()
            false
        }

        findPreference("clearCache").onPreferenceClickListener = Preference.OnPreferenceClickListener {
            CacheDataUtil.clearAllCache(context!!)
            context?.showSnackMsg(getString(R.string.clear_cache_successfully))
            setDefaultText()
            false
        }

        findPreference("official_website").setOnPreferenceClickListener {
            // context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.official_website_url))))
            ContentActivity.start(activity, getString(R.string.official_website_url))
            false
        }
    }

    private fun setDefaultText() {
        try {
            findPreference("clearCache").summary = CacheDataUtil.getTotalCacheSize(context!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key ?: return
        if (key == "color") {
            colorPreview.setView()
        }
    }
}