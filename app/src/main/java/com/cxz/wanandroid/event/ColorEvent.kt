package com.cxz.wanandroid.event

import com.cxz.wanandroid.utils.SettingUtil

/**
 * Created by guoyikai on 2023/6/18.
 */
class ColorEvent(var isRefresh: Boolean, var color: Int = SettingUtil.getColor())