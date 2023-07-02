package com.example.sonder_dating_app.presentation.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sonder_dating_app.presentation.commands.MatchesCommand
import com.example.sonder_dating_app.presentation.commands.MatchesCommand.GetReactionsForUser
import com.example.sonder_domain.mapper.toReaction
import com.example.sonder_domain.mapper.toUser
import com.example.sonder_domain.models.MatchUser
import com.example.sonder_domain.repository.SonderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import misc.Misc
import javax.inject.Inject

@HiltViewModel
internal class MatchesViewModel @Inject constructor(
    private var sonderRepository: SonderRepository
) : BaseViewModel<MatchesCommand>() {

    private val _matchesUsers = MutableLiveData<MutableList<MatchUser>>(ArrayList())
    val matchesUsers: LiveData<MutableList<MatchUser>> = _matchesUsers

    override fun handle(command: MatchesCommand) {
        when(command) {
            is GetReactionsForUser -> getReactionsForUser()
        }
    }

    private fun getReactionsForUser() {
        viewModelScope.launch (Dispatchers.IO) {
            state.value?.user?.uid?.let { uid ->
                val getReactionsResult = sonderRepository.getReactions(uid)
                Log.d("a", "asca get reactions result ${getReactionsResult.reactionsList}")
                val matchesUsers = if (getReactionsResult.status == Misc.EReplyStatus.ERS_OK) {
                    Log.d("a", "asca get reactions ${getReactionsResult.reactionsList}")
                    getReactionsResult.reactionsList.mapNotNull {
                        val toUser = sonderRepository.getUser(it.fromUID)
                        if (toUser.status == Misc.EReplyStatus.ERS_OK) {
                            MatchUser(
                                user = toUser.user.toUser(),
                                reaction =  it.reactionType.toReaction(),
                                isMatch = it.isMatch
                            )
                        } else null
                    }
                } else ArrayList()
                _matchesUsers.postValue(matchesUsers.toMutableList())
            }
        }
    }
}
