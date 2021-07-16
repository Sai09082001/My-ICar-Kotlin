package com.example.icarchecking.view.api.model.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class HistoryInfoEntity : Serializable {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("car_id")
    var carId: Int? = null
    @SerializedName("address")
    var address: String? = null
    @SerializedName("speed")
    var speed: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("user_id")
    var userId: Int? = null
    @SerializedName("created_at")
    var createAt: String? = null
    @SerializedName("updated_at")
    var updateAt: String? = null
    @SerializedName("active_status")
    var activeStatus: String? = null
}