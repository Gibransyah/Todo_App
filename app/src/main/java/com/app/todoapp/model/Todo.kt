package com.app.todoapp.model


import com.google.gson.annotations.SerializedName

data class Todo(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("message")
    val message: String
)