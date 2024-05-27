package com.cxz.wanandroid.http.exception

/**
 * Created by guoyikai on 2023/4/21.
 */
class ApiException : RuntimeException {

    private var code: Int? = null

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))
}