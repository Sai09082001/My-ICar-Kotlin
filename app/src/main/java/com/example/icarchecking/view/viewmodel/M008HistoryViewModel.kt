package com.example.icarchecking.view.viewmodel

import com.example.icarchecking.CommonUtils
import com.example.icarchecking.view.api.APIRequest

class M008HistoryViewModel : BaseViewModel() {

    fun getGroupDay(carId: Int) {
        val token = CommonUtils.getInstance().getPref(TOKEN)
        val api: APIRequest = getWS().create(APIRequest::class.java)
        api.getHistoryGroupDay(token, carId)?.enqueue(initResponse(API_KEY_GET_GROUP_DAY))
    }

    fun getHistory(carId: Int, startTime: String, endTime: String) {
        val token = CommonUtils.getInstance().getPref(TOKEN)
        val api: APIRequest = getWS().create(APIRequest::class.java)
        api.getHistoryByCar(token, startTime, endTime, carId)
            ?.enqueue(initResponse(API_KEY_GET_HISTORY))
    }

    companion object {
        const val API_KEY_GET_HISTORY = "API_KEY_GET_HISTORY"
        const val API_KEY_GET_GROUP_DAY = "API_KEY_GET_GROUP_DAY"
    }
}