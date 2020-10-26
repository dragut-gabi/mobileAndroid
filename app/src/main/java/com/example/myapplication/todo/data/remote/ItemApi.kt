package com.example.myapplication.todo.data.remote

import com.example.myapplication.core.Api
import com.example.myapplication.todo.data.Item
import retrofit2.Response
import retrofit2.http.*

object ItemApi {
    interface Service {
        @GET("/api/item")
        suspend fun find(): List<Item>

        @GET("/api/item/{id}")
        suspend fun read(@Path("id") itemId: String): Item;

        @Headers("Content-Type: application/json")
        @POST("/api/item")
        suspend fun create(@Body item: Item): Item

        @Headers("Content-Type: application/json")
        @PUT("/api/item/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: Item): Item

        @DELETE("/api/item/{id}")
        suspend fun delete(@Path("id") itemId: String): Response<Unit>
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}