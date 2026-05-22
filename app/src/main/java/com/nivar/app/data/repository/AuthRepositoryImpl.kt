package com.nivar.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nivar.app.data.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                trySend(null)
            } else {
                // Fetch from Firestore
                firestore.collection("users").document(firebaseUser.uid)
                    .get()
                    .addOnSuccessListener { doc ->
                        trySend(doc.toObject(User::class.java) ?: User(uid = firebaseUser.uid))
                    }
                    .addOnFailureListener {
                        trySend(User(uid = firebaseUser.uid, isGuest = firebaseUser.isAnonymous))
                    }
            }
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> = runCatching {
        val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        val firebaseUser = result.user ?: throw Exception("Google Sign In Failed")
        val user = User(uid = firebaseUser.uid, email = firebaseUser.email ?: "")
        saveUserToFirestore(user)
        user
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<User> = runCatching {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw Exception("Sign In Failed")
        User(uid = firebaseUser.uid, email = firebaseUser.email ?: "")
    }

    override suspend fun signUpWithEmail(email: String, password: String, user: User): Result<User> = runCatching {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw Exception("Sign Up Failed")
        val newUser = user.copy(uid = firebaseUser.uid)
        saveUserToFirestore(newUser)
        newUser
    }

    override suspend fun signInAnonymously(): Result<User> = runCatching {
        val result = auth.signInAnonymously().await()
        val firebaseUser = result.user ?: throw Exception("Guest Sign In Failed")
        User(uid = firebaseUser.uid, isGuest = true)
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun updateProfile(user: User): Result<Unit> = runCatching {
        saveUserToFirestore(user)
    }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.uid).set(user).await()
    }
}
