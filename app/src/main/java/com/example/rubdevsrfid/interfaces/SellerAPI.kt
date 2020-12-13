package com.example.rubdevsrfid.interfaces

import com.example.rubdevsrfid.models.Seller
import com.example.rubdevsrfid.models.SellerRoom
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


public interface SellerAPI {
    @GET("/api/seller/{id}")
    fun find(@Path ("id") id: String): Call<Seller>

    @POST("/api/seller")
    fun save(@Body sellerRoom: SellerRoom?) : Call<SellerRoom>
}