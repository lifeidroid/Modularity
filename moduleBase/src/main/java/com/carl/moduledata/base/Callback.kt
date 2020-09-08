package com.carl.moduledata.base

interface Callback<S, F> {
    fun onSuccess(t: S)

    fun onFail(s: F)
}