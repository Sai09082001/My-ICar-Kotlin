package com.example.icarchecking.view.fragment

interface OnHomeCallBack {
    fun showFrg(backTag : String, tag : String, isBacked : Boolean)
    fun showFrg(backTag : String,data  : Any?, tag : String, isBacked : Boolean)
    fun closeApp()
}
