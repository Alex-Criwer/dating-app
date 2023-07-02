package com.example.sonder_dating_app.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sonder_dating_app.presentation.commands.SuggestedProfilesCommand
import com.example.sonder_dating_app.presentation.commands.SuggestedProfilesCommand.*
import com.example.sonder_domain.mapper.toUser
import com.example.sonder_domain.models.Reaction
import com.example.sonder_domain.models.User
import com.example.sonder_domain.repository.SonderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import misc.Misc
import javax.inject.Inject

@HiltViewModel
internal class SuggestedProfilesViewModel @Inject constructor(
    private var sonderRepository: SonderRepository
) : BaseViewModel<SuggestedProfilesCommand>() {

    private val _suggestedUsers = MutableLiveData<ArrayList<User>>(ArrayList())
    val suggestedUsers: LiveData<ArrayList<User>> = _suggestedUsers

    override fun handle(command: SuggestedProfilesCommand) {
        when (command) {
            is SearchSuggestedUsers -> getSuggestedUsers()
            is RemoveFirstUser -> removeFirstUser()
            is SetDislikeToUser -> setReaction(command.fromUid, command.toUid, Reaction.DISLIKE)
            is SetLikeToUser -> setReaction(command.fromUid, command.toUid, Reaction.LIKE)
        }
    }

    fun getUsersToDisplay() : List<User> {
        return suggestedUsers.value?.toList() ?: emptyList()
    }

    fun clearUsers() {
        _suggestedUsers.value = ArrayList()
    }

    private fun getSuggestedUsers() {
        viewModelScope.launch (Dispatchers.IO) {
            state.value?.user?.let {
                val grpcUsers = sonderRepository.searchUsers(it)
                val users = if (grpcUsers.status == Misc.EReplyStatus.ERS_OK) {
                    grpcUsers.usersList.map { grpcUser -> grpcUser.toUser() }
                } else ArrayList()
                _suggestedUsers.postValue(ArrayList(users))
            }
        }
    }

    private fun removeFirstUser() {
        if (_suggestedUsers.value!!.isNotEmpty()) {
            val oldList = _suggestedUsers.value.orEmpty()
            _suggestedUsers.value = ArrayList(oldList.drop(1))
        }
    }

    private fun setReaction(fromUid: String, toUid: String, reaction: Reaction) {
        viewModelScope.launch (Dispatchers.IO) {
            val grpcResponse = sonderRepository.setReaction(fromUid, toUid, reaction)
            if (grpcResponse.match) {

            }
        }
    }
}
