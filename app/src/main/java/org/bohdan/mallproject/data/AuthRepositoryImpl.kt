//package org.bohdan.mallproject.data
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.firestore.FirebaseFirestore
//import org.bohdan.mallproject.domain.repository.AuthRepository
//
//object AuthRepositoryImpl: AuthRepository {
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//
//    override fun login(email: String, password: String): LiveData<Result<FirebaseUser?>> {
//        val resultLiveData = MutableLiveData<Result<FirebaseUser?>>()
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    resultLiveData.value = Result.success(auth.currentUser)
//                } else {
//                    resultLiveData.value = Result.failure(task.exception ?: Exception("Unknown error"))
//                }
//            }
//        return resultLiveData
//    }
//
//    override fun register(email: String, password: String): LiveData<Result<FirebaseUser?>> {
//        val resultLiveData = MutableLiveData<Result<FirebaseUser?>>()
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    resultLiveData.value = Result.success(auth.currentUser)
//                } else {
//                    resultLiveData.value = Result.failure(task.exception ?: Exception("Unknown error"))
//                }
//            }
//        return resultLiveData
//    }
//
//    override fun logout() {
//        auth.signOut()
//    }
//}