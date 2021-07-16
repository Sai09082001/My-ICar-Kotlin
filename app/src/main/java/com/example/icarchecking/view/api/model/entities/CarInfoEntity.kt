package com.example.icarchecking.view.api.model.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CarInfoEntity : Serializable {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("phone_manager")
    var phoneManager: String? = null
    @SerializedName("password_manager")
    var passwordManager: String? = null
    @SerializedName("car_number")
    var carNumber: String? = null
    @SerializedName("car_brand")
    var carBrand: String? = null
    @SerializedName("active_status")
    var activeStatus: String? = null
    @SerializedName("last_address")
    var lastAddress: String? = null
    @SerializedName("last_speed")
    var lastSpeed: String? = null
    @SerializedName("last_lat")
    var lastLat: String? = null
    @SerializedName("last_lng")
    var lastLng: String? = null
    @SerializedName("last_status")
    var lastStatus: String? = null
}