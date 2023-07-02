package com.example.sonder_dating_app.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import java.util.concurrent.TimeUnit

class FirebaseAuthProvider(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseMessaging: FirebaseMessaging
) {

   private lateinit var activityContext: Activity

    companion object {
        private lateinit var phone: String
        private lateinit var credentialID: String
        private lateinit var credentialToken: PhoneAuthProvider.ForceResendingToken
    }

    private var listener: OnAuthenticationListener? = null

    init {
        firebaseAuth.setLanguageCode(Locale.getDefault().language)
    }

    fun initActivityContext(activity: Activity) {
        activityContext = activity
    }

    fun getPhoneNumber() = phone

    fun verifyPhoneNumber(phoneNumber: String){
        phone = phoneNumber
        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions
            .newBuilder(firebaseAuth)
            .setActivity(activityContext)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setPhoneNumber(phoneNumber)
            .setCallbacks(getFireBaseAuthCallback())
            .build())
    }

    fun resendVerificationCode(){
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions
            .newBuilder(firebaseAuth)
            .setActivity(activityContext)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setPhoneNumber(phone)
            .setCallbacks(getFireBaseAuthCallback())
            .setForceResendingToken(credentialToken)
            .build())
    }


    fun verifyVerificationCode(code: String){
        val credential = PhoneAuthProvider.getCredential(credentialID, code)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                listener?.onSuccessful()
            }
            else listener?.onVerificationFailed(task.exception?.message.toString())
        }
    }

    fun isAuthenticate(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun getUid(): String? {
        return firebaseAuth.uid
    }

    fun addOnAuthenticationListener(listener: OnAuthenticationListener){
        this.listener = listener
    }

    private fun getFireBaseAuthCallback() =
        object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if(task.isSuccessful) listener?.onSuccessful()
                    else listener?.onVerificationFailed(task.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                listener?.onVerificationFailed(exception.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                credentialID = id
                credentialToken = token
                listener?.onCodeSent()
            }
        }

    fun getToken(callback: (String?) -> Unit) {
        firebaseAuth.currentUser!!.getIdToken(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result.token
                Log.d("aa", "asca token is $token")
                callback.invoke(token)
            } else {
                callback.invoke(null)
            }
        }
    }

    fun getMessagingToken(callback: (String?) -> Unit) {
        firebaseMessaging.token.addOnCompleteListener {task ->
            if (task.isSuccessful) {
                val messagingToken = task.result
                Log.d("aa", "asca messaging token is $messagingToken")
                callback.invoke(messagingToken)
            } else {
                callback.invoke(null)
            }
        }
    }

    interface OnAuthenticationListener {
        fun onSuccessful()
        fun onCodeSent()
        fun onVerificationFailed(exception: String)
    }
}
