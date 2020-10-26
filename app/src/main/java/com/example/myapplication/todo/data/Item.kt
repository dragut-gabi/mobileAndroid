package com.example.myapplication.todo.data

data class Item(
    var _id: String,
    val userId: String,
    var title: String,
    var pages: Number,
    var sold: Boolean,
    var releaseDate: String

) {
    override fun toString(): String = title
}
