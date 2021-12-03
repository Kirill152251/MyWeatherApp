package com.example.myweatherapp.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState (private val status: Status, private val errorMsg: String) {
    companion object {
        val LOADING = NetworkState(Status.RUNNING, "Loading...")
        val LOADED = NetworkState(Status.SUCCESS, "Success")
        val ERROR = NetworkState(Status.FAILED, "An error has occurred :(")
    }
}
