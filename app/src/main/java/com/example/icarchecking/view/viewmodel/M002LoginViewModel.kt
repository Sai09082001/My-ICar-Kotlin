package com.example.icarchecking.view.viewmodel

import com.example.icarchecking.view.api.APIRequest
import com.example.icarchecking.view.api.model.entities.AccountEntity


class M002LoginViewModel : BaseViewModel() {
    fun login(phone : String, pass : String){
        val api : APIRequest = getWS().create(APIRequest::class.java)
        api.login(AccountEntity(phone, pass))?.enqueue(initResponse(API_KEY_LOGIN))
    }

    companion object{
        const val API_KEY_LOGIN = "API_KEY_LOGIN"
    }
}
