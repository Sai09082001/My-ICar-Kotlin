package com.example.icarchecking

import android.util.Log
import com.example.icarchecking.view.MapManager
import com.example.icarchecking.view.api.model.entities.MsgEntity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import kotlinx.coroutines.launch
import java.net.URI


class WebSocketUtil private constructor() {
    private var mWebSocketClient: WebSocketClient? = null
    fun connectWebSocket() {
        if (mWebSocketClient != null && !mWebSocketClient!!.isClosed) return
        val uri: URI = try {
            val token = CommonUtils.getInstance().getPref(TOKEN)
            if (token == null || token.isEmpty()) {
                return
            }
            val mToken = String.format(SERVER_ADDRESS, token)
            Log.i(TAG, "token: $mToken")
            URI.create(mToken)
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        mWebSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(serverHandshake: ServerHandshake?) {
                Log.i("WebSocket", "Opened")
                sendMessage()
            }

            @Synchronized
            override fun onMessage(sms: String?) {
                Log.i(TAG, sms!!)
                if (sms.contains(TYPE_PING) || sms.contains(TYPE_WELCOME)) {
                    return
                }
                //xu ly sms
                val carInfo = Gson().fromJson(sms, MsgEntity::class.java)

                if (carInfo.carInfo == null) return
                //Coroutine
                GlobalScope.launch(Dispatchers.Main) {
                    MapManager.getInstance().updateStatusCar(carInfo.carInfo)
                    MapManager.getInstance().updateTrackingCar(carInfo.carInfo)
                }
                Log.i(TAG, "WebSocket: sms: ${carInfo.carInfo}")
            }

            override fun onClose(i: Int, s: String, b: Boolean) {
                Log.i("WebSocket", "Closed $s")
            }

            override fun onError(e: Exception) {
                Log.i("WebSocket", "Error " + e.message)
            }
        }
        mWebSocketClient?.connect()
    }

    private fun sendMessage() {
        if (mWebSocketClient == null) return
        mWebSocketClient?.send("{\"command\":\"subscribe\",\"identifier\":\"{\\\"channel\\\":\\\"UsersChannel\\\"}\"}")
    }

    fun disconnect() {
        if (mWebSocketClient != null) {
            mWebSocketClient?.close()
            mWebSocketClient = null
            instance = null
        }
    }

    companion object {
        const val TOKEN = "TOKEN"
        private const val SERVER_ADDRESS =
            "wss://icar-api.techja.edu.vn/socket-io?Authorization=Bearer__%s"
        private val TAG = WebSocketUtil::class.java.name
        private const val TYPE_PING = "\"type\":\"ping\""
        private const val TYPE_WELCOME = "\"type\":\"welcome\""
        private var instance: WebSocketUtil? = null
        fun getInstance(): WebSocketUtil? {
            if (instance == null) {
                instance = WebSocketUtil()
            }
            return instance
        }
    }
}