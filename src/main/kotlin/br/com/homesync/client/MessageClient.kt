package br.com.homesync.client

interface MessageClient {
    fun sendMessage(phoneNumber: String, message: String)
}
