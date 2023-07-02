package com.example.sonder_dating_app.presentation.view_models.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.sonder_dating_app.firebase.AuthenticationStatus
import com.example.sonder_dating_app.firebase.interactor.FirebaseAuthInteractor
import com.example.sonder_dating_app.location_manager.LocationManager
import com.example.sonder_dating_app.presentation.commands.SignUpCommand
import com.example.sonder_dating_app.presentation.commands.SignUpCommand.CheckUser
import com.example.sonder_dating_app.presentation.utils.MOSCOW
import com.example.sonder_dating_app.presentation.utils.extensions.set
import com.example.sonder_domain.mapper.toUser
import com.example.sonder_domain.models.User
import com.example.sonder_domain.repository.SonderMessageTokenSharedPref
import com.example.sonder_domain.repository.SonderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import misc.Misc
import javax.inject.Inject

@HiltViewModel
internal class SignUpViewModel @Inject constructor(
    private var interactor: FirebaseAuthInteractor,
    private var sonderRepository: SonderRepository,
    private var locationManager: LocationManager,
    private val sonderMessageTokenSharedPref: SonderMessageTokenSharedPref // временно, лучше через getUser
) : BaseLoginViewModel(interactor) {

    fun verifyPhoneNumber(phoneNumber: String) {
        interactor.verifyPhoneNumber(phoneNumber)
    }

    fun resendVerificationCode() {
        interactor.resendVerificationCode()
    }

    fun verifyVerificationCode(code: String) {
        interactor.verifyVerificationCode(code)
    }

    fun getPhone() = interactor.getPhone()

    fun getUid(): String? = interactor.getUid()

    fun getToken(callback: (String?) -> Unit) = interactor.getToken(callback)

    fun getMesssagingToken(callback: (String?) -> Unit) = interactor.getMessagingToken(callback)

    fun onBack() {
        status.set(AuthenticationStatus.Default)
    }

    override fun handle(command: SignUpCommand) {
        when(command) {
            is CheckUser -> handleCheckUser(command.uid)
        }
    }

    private fun handleCheckUser(uid: String?) = viewModelScope.launch(Dispatchers.IO) {
        if (uid == null) return@launch
        state.value?.let {
            it.user.uid = uid
            it.user.geo = MOSCOW //locationManager.getLocation() ?: MOSCOW
            getToken { token ->
                if (token != null) {
                    it.token = token
                    sonderRepository.setToken(token)
                } else {
                    Log.d("a", " asca Не удалось получить токен")
                }
            }
        }
        _uid.postValue(uid)
        getMesssagingToken { messagingToken ->
            if (messagingToken != null) {
                viewModelScope.launch(Dispatchers.IO) {
                    sonderRepository.setMessageToken(uid, messagingToken)
                }
            } else {
                Log.d("a", "Не удалось получить messaging токен")
            }
        }

        val grpcUser = sonderRepository.getUser(uid)
        Log.d("a", "asca CheckUser ${grpcUser.user.toUser()} ${grpcUser.error.message} ${grpcUser.status.name}")
        if (grpcUser.status != Misc.EReplyStatus.ERS_OK) {
            sonderRepository.registerUser(state.value?.user ?: User(uid = uid))
        }
    }

    fun isUserFirstTimeRegisted(uid: String?): Boolean {
        if (uid == null) return true
//        val task = viewModelScope.async (Dispatchers.IO) {
//            val grpcUser = sonderRepository.getUser(uid)
//            grpcUser.status != Misc.EReplyStatus.ERS_OK
//        }
//        return task.await()
        return sonderMessageTokenSharedPref.isUserRegistedFirstTime()
    }
}
