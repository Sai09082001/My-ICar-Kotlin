package com.example.icarchecking.view.fragment

interface OnActionCallBack {
    fun callBack(key:String, data : Any?){}
    fun logout(){}
    fun showWarnNoInternet(){}
}
