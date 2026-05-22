package com.nivar.app.data.repository

import com.nivar.app.data.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun signUpWithEmail(email: String, password: String, user: User): Result<User>
    suspend fun signInAnonymously(): Result<User>
    suspend fun signOut()
    suspend fun updateProfile(user: User): Result<Unit>
}
