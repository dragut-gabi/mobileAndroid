package com.example.myapplication.auth.data

import com.example.myapplication.auth.data.remote.RemoteAuthDataSource
import com.example.myapplication.core.Api
import com.example.myapplication.core.Constants
import com.example.myapplication.core.Result

object AuthRepository {
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        Constants.instance()?.deleteValueString("token")
        Api.tokenInterceptor.token = null
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        val result = RemoteAuthDataSource.login(user)
        if (result is Result.Success<TokenHolder>) {
            setLoggedInUser(user, result.data)
            Constants.instance()?.storeValueString("token",result.data.token);
        }
        return result
    }

    private fun setLoggedInUser(user: User, tokenHolder: TokenHolder) {
        AuthRepository.user = user
        Api.tokenInterceptor.token = tokenHolder.token
    }
}
