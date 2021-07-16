package com.example.icarchecking.view.api

import com.example.icarchecking.view.api.model.CarInfoModelRes
import com.example.icarchecking.view.api.model.GroupDayModelRes
import com.example.icarchecking.view.api.model.HistoryInfoModelRes
import com.example.icarchecking.view.api.model.UserInfoModelRes
import com.example.icarchecking.view.api.model.entities.AccountEntity
import com.example.icarchecking.view.api.model.entities.UserCreateNewPassEntity
import com.example.icarchecking.view.api.model.entities.UserRegisterEntity
import retrofit2.Call
import retrofit2.http.*

interface APIRequest {
    @POST("auth/login")
    @Headers("Content-Type:application/json")
    fun login(@Body body: AccountEntity?): Call<UserInfoModelRes?>?

    @GET("auth/check")
    @Headers("Content-Type:application/json")
    fun checkPhone(@Query("phone") phone: String): Call<UserInfoModelRes?>?

    @POST("auth/register")
    @Headers("Content-Type:application/json")
    fun register(@Body body: UserRegisterEntity?): Call<UserInfoModelRes?>?

    @POST("auth/forgot_password")
    @Headers("Content-Type:application/json")
    fun createNewPass(@Body userCreateNewPassEntity: UserCreateNewPassEntity): Call<UserInfoModelRes?>?

    @GET("cars")
    @Headers("Content-Type:application/json")
    fun getListCar(
        @Header("Authorization") token: String?,
        @Query("page") page: Int
    ): Call<CarInfoModelRes?>?

    @GET("locations/group_day")
    @Headers("Content-Type:application/json")
    fun getHistoryGroupDay(
        @Header("Authorization") token: String?,
        @Query("car_id") carId: Int
    ): Call<GroupDayModelRes?>?

    @GET("locations")
    @Headers("Content-Type:application/json")
    fun getHistoryByCar(
        @Header("Authorization") token: String?,
        @Query("filter[created_at_gteq]") startTime: String,
        @Query("filter[created_at_lteq]") endTime: String,
        @Query("filter[car_id_eq]") carId: Int
    ): Call<HistoryInfoModelRes?>?
}