package com.example.myapplication.book.bookItem

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.TAG
import com.example.myapplication.book.data.Book
import com.example.myapplication.book.data.BookRepository
import com.example.myapplication.core.Result
import kotlinx.coroutines.launch

class BookEditViewModel : ViewModel() {
    private val mutableItem = MutableLiveData<Book>().apply { value = Book("", "", pages = 0,sold = false, releaseDate = "",title = "") }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val book: LiveData<Book> = mutableItem
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            Log.v(TAG, "loadItem...");
            mutableFetching.value = true
            mutableException.value = null
            when (val result = BookRepository.load(itemId)) {
                is Result.Success -> {
                    Log.d(TAG, "loadItem succeeded");
                    mutableItem.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "loadItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableFetching.value = false
        }
    }

    fun saveOrUpdateItem(text: String,pages: Number, sold: Boolean, releaseDate: String) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateItem...");
            val item = mutableItem.value ?: return@launch
            item.title = text
            item.pages=pages
            item.sold=sold
            item.releaseDate=releaseDate
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Book>
            if (item._id.isNotEmpty()) {
                result = BookRepository.update(item)
            } else {
                val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
                val randomString = (1..16)
                    .map { kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")
                item._id = randomString
                result = BookRepository.save(item)
            }
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                    mutableItem.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
    fun deleteItem()
    {
        viewModelScope.launch {
            mutableFetching.value = true
            mutableException.value = null
            val item = mutableItem.value ?: return@launch
            val result: Result<Boolean>
            result = BookRepository.delete(item._id)
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "delete succeeded");
//                    mutableItem.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "delete failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}
