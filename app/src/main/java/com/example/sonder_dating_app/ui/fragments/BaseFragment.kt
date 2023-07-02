package com.example.sonder_dating_app.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.sonder_dating_app.presentation.events.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal abstract class BaseFragment : Fragment {
    private var coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main

    constructor(layoutId: Int): super(layoutId)

    constructor(layoutId: Int, dispatcher: CoroutineDispatcher): super(layoutId) {
        coroutineDispatcher = dispatcher
    }

    constructor(): super()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
    }

    // единая очередь сообщений для фрагментов, которые наследуются от BaseFragment.
    // на эту очередь подписывается Activity и получает события с данными от своих фрагментов.
    companion object {
        private val tasksEventChannel = Channel<Event>(capacity = Channel.UNLIMITED)
        val tasksEventFlow = tasksEventChannel.receiveAsFlow()
    }

    protected fun dispatchEvent(event: Event) = lifecycleScope.launch(coroutineDispatcher) {
        tasksEventChannel.send(event)
    }

    protected open fun initFragment() {}
}
