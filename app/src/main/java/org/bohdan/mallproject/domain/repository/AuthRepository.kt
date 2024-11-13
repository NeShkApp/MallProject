//package org.bohdan.mallproject.domain.repository
//
//import androidx.lifecycle.LiveData
//import com.google.firebase.auth.FirebaseUser
//
//interface AuthRepository {
//    fun login(
//        email: String,
//        password: String
//    ): LiveData<Result<FirebaseUser?>>
//
//    fun register(
//        email: String,
//        password: String
//    ): LiveData<Result<FirebaseUser?>>
//
//    fun logout()
//}