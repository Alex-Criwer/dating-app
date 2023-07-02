package com.example.sonder_dating_app.presentation.event_handler

internal abstract class EventHandler<Event: Any> {
    abstract fun handle(event: Event)
}
