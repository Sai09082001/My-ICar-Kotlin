package com.example.icarchecking.view.api.model.entities

import com.google.gson.annotations.SerializedName
import lombok.ToString
import java.io.Serializable

class MsgEntity : Serializable {
    @SerializedName("message")
    val carInfo: CarInfoEntity? = null
}