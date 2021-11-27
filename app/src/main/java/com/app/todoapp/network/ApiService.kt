package com.app.todoapp.network

import com.app.todoapp.model.Item
import com.app.todoapp.model.Todo
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("todos")
    fun getAllTodo(): Call<Todo>

    @FormUrlEncoded
    @POST("Todos")
    fun postTodo(
        @Field("name") name: String
    ): Call<Todo>

    @DELETE("todos/{id}")
    fun deleteTodo(
        @Path("id") id: Int
    ): Call<Todo>

    @FormUrlEncoded
    @PUT("todos/{id}")
    fun updateTodo(
        @Path("id") id: Int,
        @Field("name") name: String
    ): Call<Todo>

    @FormUrlEncoded
    @POST("items")
    fun postTodoItem(
        @Field("name") name: String,
        @Field("id") id: Int
    ): Call<Item>

    @FormUrlEncoded
    @PUT("item/{id}")
    fun updateTodoItem(
        @Path("id") id: Int,
        @Path("name") name: String
    ): Call<Item>

    @DELETE("item/{id}")
    fun deleteTodoItem(
        @Path("id") id: Int
    ): Call<Item>

}