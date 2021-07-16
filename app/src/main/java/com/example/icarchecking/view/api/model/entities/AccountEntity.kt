package com.example.icarchecking.view.api.model.entities

import com.example.icarchecking.App
import com.example.icarchecking.CommonUtils
import com.google.gson.annotations.SerializedName
import lombok.ToString
import java.io.Serializable

@ToString
class AccountEntity : Serializable {
    @SerializedName("phone")
    var mPhone: String? = null

    @SerializedName("password")
    var mPass: String? = null

    @SerializedName("device_token")
    var mDeviceToken: String? = null

    constructor(mPhone : String, mPass : String) : super(){
        this.mPhone = mPhone
        this.mPass = mPass
        mDeviceToken = CommonUtils.getInstance().getPref(App.DEVICE_TOKEN)
    }
}


