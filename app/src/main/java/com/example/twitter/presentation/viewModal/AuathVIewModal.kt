package com.example.twitter.presentation.viewModal

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitter.domain.model.AuthenticatedUser
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class AuthViewModal @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore,
    val datastore: DataStore<Preferences>
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()
    var verificationIdStore = ""

    companion object {
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val ABOUT_KEY = stringPreferencesKey("about")
        private val LINK_KEY = stringPreferencesKey("link")
        private val PROFILE_IMAGE_KEY = stringPreferencesKey("profile_image")
    }

    fun saveUserPreferences(name: String, about: String, link: String, profileImage: String) {
        viewModelScope.launch {
            datastore.edit { preferences ->
                preferences[USER_NAME_KEY] = name
                preferences[ABOUT_KEY] = about
                preferences[LINK_KEY] = link
                preferences[PROFILE_IMAGE_KEY] = profileImage
            }
        }
    }

    suspend fun getUserPreferences(): UserPreferences? {
        val preferences = datastore.data.first()
        val name = preferences[USER_NAME_KEY]
        val about = preferences[ABOUT_KEY]
        val link = preferences[LINK_KEY]
        val profileImage = preferences[PROFILE_IMAGE_KEY]

        return if (name != null && about != null && link != null && profileImage != null) {
            UserPreferences(name, about, link, profileImage)
        } else {
            null
        }
    }


    fun sendOtp(phoneNumber: String, activity: Activity) {
        _authState.value = AuthState.Idle

        val phone = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                Log.d("TAG", "onCodeSent: $verificationId")
                verificationIdStore = verificationId
                _authState.value = AuthState.OnOtpSend(verificationId = verificationId)
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("TAG1", "onVerificationCompleted:$credential")
                signInWithCredential(credential, activity)
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Log.d("TAG3", "onVerificationFailed: ${exception.message}")
                if (exception is FirebaseAuthInvalidCredentialsException) {
                    Log.d("TAG5", "onVerificationFailed: ${exception.message}")
                } else if (exception is FirebaseTooManyRequestsException) {
                    Log.d("TAG7", "onVerificationFailed: ${exception.message}")
                } else if (exception is FirebaseAuthMissingActivityForRecaptchaException) {
                    Log.d("TAG9", "onVerificationFailed: ${exception.message}")
                }
                _authState.value = AuthState.Error(exception.message.toString())
            }

        }
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(phone)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun signInWithCredential(credential: PhoneAuthCredential, context: Context) {
        _authState.value = AuthState.Loading
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val newUser = AuthenticatedUser(
                        uid = user?.uid ?: "",
                        phoneNumber = user?.phoneNumber ?: ""
                    )
                    markUserIsAuthenticated(context)
                    _authState.value = AuthState.Success(newUser)
                    fetchUserProfile(user?.uid ?: "")
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Sign in failed")
                }
            }
    }

    private fun markUserIsAuthenticated(context: Context) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit() { putBoolean("isSigned", true) }
    }

    fun fetchUserProfile(userId: String) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(AuthenticatedUser::class.java)
                    if (user != null) {
                        _authState.value = AuthState.Success(user)
                    }
                } else {
                    _authState.value = AuthState.Error("User profile not found")
                }
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error(exception.message.toString())
            }
    }

    fun verifyOtp(otp: String, context: Context) {
        val currentAuthState = _authState.value
        _authState.value = AuthState.Loading
        if (currentAuthState !is AuthState.OnOtpSend || currentAuthState.verificationId.isEmpty()) {
            Log.d("TAG", "verifyOtp: Verification ID is empty")
            _authState.value = AuthState.Error("Verification ID is empty")
            return
        }
        val credential = PhoneAuthProvider.getCredential(verificationIdStore, otp)
        signInWithCredential(credential, context = context)
    }

    fun saveUserProfile(
        userId: String,
        name: String,
        about: String,
        link: String,
        phoneNumber: String,
        profilePictureUrl: Bitmap?
    ) {
        val encodedImage = profilePictureUrl?.let { convertBitmapToBase64(it) }
        val user = AuthenticatedUser(
            uid = userId,
            name = name,
            about = about,
            link = link,
            phoneNumber = phoneNumber,
            profilePictureUrl = encodedImage
        )
        firestore.collection("users").document(userId).set(user)
            .addOnSuccessListener {
                _authState.value = AuthState.Success(user)
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error(it.message.toString())
            }
    }

    fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
    }

    fun base64ToBitmap(base64Image: String): coil3.Bitmap? {
        return try {
            val decodedByte = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
        } catch (e: Exception) {
            null
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    fun signOut(context: Context) {
        firebaseAuth.signOut()
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit { putBoolean("isSigned", false) }
    }
}


sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class OnOtpSend(val verificationId: String) : AuthState()
    data class Success(val user: AuthenticatedUser) : AuthState()
    data class Error(val message: String) : AuthState()

}

data class UserPreferences(
    val name: String,
    val about: String,
    val link: String,
    val profileImage: String
)

