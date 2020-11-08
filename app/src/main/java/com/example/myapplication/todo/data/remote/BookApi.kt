package com.example.myapplication.todo.data.remote

import com.example.myapplication.core.Api
import com.example.myapplication.todo.data.Book
import retrofit2.Response
import retrofit2.http.*

object BookApi {
    interface Service {
        @GET("/api/book")
        suspend fun find(): List<Book>

        @GET("/api/book/{id}")
        suspend fun read(@Path("id") itemId: String): Book;

        @Headers("Content-Type: application/json")
        @POST("/api/book")
        suspend fun create(@Body book: Book): Book

        @Headers("Content-Type: application/json")
        @PUT("/api/book/{id}")
        suspend fun update(@Path("id") itemId: String, @Body book: Book): Book

        @DELETE("/api/book/{id}")
        suspend fun delete(@Path("id") itemId: String): Response<Unit>
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}