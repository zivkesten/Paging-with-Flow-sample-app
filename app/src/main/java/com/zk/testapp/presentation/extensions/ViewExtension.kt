package com.zk.testapp.presentation.extensions

import android.view.View

fun View.setDebounceClickListener(action: () -> Unit) {
    val actionDebouncer = ActionDebouncer(action)

    setOnClickListener {
        actionDebouncer.notifyAction()
    }
}

private class ActionDebouncer(private val action: () -> Unit) {

    companion object {
        const val DEBOUNCE_TIME_INTERVAL = 600L
    }

    private var lastTimeAction = 0L
    fun notifyAction() {

        val now = System.currentTimeMillis()
        val milliSecondsPassed = now - lastTimeAction
        val actionAllowed = milliSecondsPassed > DEBOUNCE_TIME_INTERVAL
        lastTimeAction = now

        if (actionAllowed) {
            action.invoke()
        }
    }
}