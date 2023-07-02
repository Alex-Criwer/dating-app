package com.example.sonder_dating_app.presentation.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sonder_dating_app.presentation.commands.Command
import com.example.sonder_dating_app.presentation.state.DashboardSonderState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal abstract class  BaseViewModel<T : Command> : ViewModel() {

    private var coroutineDispatcherBaseIO: CoroutineDispatcher = Dispatchers.IO
    private var coroutineDispatcherBaseMain: CoroutineDispatcher = Dispatchers.Main

    companion object {
        val state = MutableLiveData(DashboardSonderState())

        private val commandsChannel = Channel<Command>(capacity = Channel.UNLIMITED)
        val commandsFlow = commandsChannel.receiveAsFlow()
    }

    fun dispatchCommand(command: T) = viewModelScope.launch {
        commandsChannel.send(command)
    }

    abstract fun handle(command: T)
}
