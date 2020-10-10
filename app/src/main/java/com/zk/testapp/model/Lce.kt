package com.zk.testapp.model

sealed class Lce<T> {
    class Loading<T> : Lce<T>() {
        override fun equals(other: Any?) = (other is Loading<*>)
        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }
    data class Content<T>(val packet: T) : Lce<T>()
    data class Error<T>(val packet: T) : Lce<T>()
}