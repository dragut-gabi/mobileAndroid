package com.example.myapplication.book.data

import com.example.myapplication.core.Result
import com.example.myapplication.book.data.remote.BookApi

object BookRepository {
    private var cachedBooks: MutableList<Book>? = null;

    suspend fun loadAll(): Result<List<Book>> {
        if (cachedBooks != null) {
            return Result.Success(cachedBooks as List<Book>);
        }
        try {
            val items = BookApi.service.find()
            cachedBooks = mutableListOf()
            cachedBooks?.addAll(items)
            return Result.Success(cachedBooks as List<Book>)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun load(itemId: String): Result<Book> {
        val item = cachedBooks?.find { it._id == itemId }
        if (item != null) {
            return Result.Success(item)
        }
        try {
            return Result.Success(BookApi.service.read(itemId))
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun save(book: Book): Result<Book> {
        try {
            val createdItem = BookApi.service.create(book)
            cachedBooks?.add(createdItem)
            return Result.Success(createdItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(book: Book): Result<Book> {
        try {
            val updatedItem = BookApi.service.update(book._id, book)
            val index = cachedBooks?.indexOfFirst { it._id == book._id }
            if (index != null) {
                cachedBooks?.set(index, updatedItem)
            }
            return Result.Success(updatedItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
    suspend fun delete(itemId: String): Result<Boolean>
    {
        try {
            BookApi.service.delete(itemId)
            cachedBooks?.remove(cachedBooks?.find { it._id == itemId })
            return Result.Success(true)
        }
        catch (e: Exception) {
            return Result.Error(e)
        }
    }
}