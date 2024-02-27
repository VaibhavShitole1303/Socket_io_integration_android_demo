package com.example.socket_io_integration_android_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MainActivity : AppCompatActivity() {

    private lateinit var socketListener: SocketListener
    private lateinit var viewModel: MainViewModel
    private val okHttpClient=OkHttpClient()
    private var webSocket:WebSocket?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnConnect=findViewById<Button>(R.id.btn_connect)
        val btnDisconnect=findViewById<Button>(R.id.btn_disconnect)
        val btnSend=findViewById<Button>(R.id.btn_send)
        val edtMessage=findViewById<AppCompatEditText>(R.id.edt_message)
        val tvMessage=findViewById<AppCompatTextView>(R.id.tv_message)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        socketListener=SocketListener(viewModel)

        viewModel.socketStatus.observe(this,{
            tvMessage.text= if(it) "Connnected" else "Disconnected"
        })
        var text =""
        viewModel.message.observe(this,{
            text += "${if (it.first) "You:" else "Other: "} ${it.second}\n"
            tvMessage.text=text
        })
        btnConnect.setOnClickListener { 
            webSocket=okHttpClient.newWebSocket(createRequest(),socketListener)
        }
        btnDisconnect.setOnClickListener {
            webSocket?.close(1000,"Cancelled Manually")
        }
        btnSend.setOnClickListener {
            if(edtMessage.text.toString().isNotEmpty()){
                webSocket?.send(edtMessage.text.toString())
                viewModel.setMessage(Pair(true,edtMessage.text.toString()))
            }
        }
    }
    private fun createRequest(): Request {
        val webSocketUrl = "wss://s11977.blr1.piesocket.com/v3/1?api_key=MOOU8OGxg8cxrihXOrnqfhZmlXMCVCct4eRUCQAQ&notify_self=1"
        return Request.Builder()
            .url(webSocketUrl)
            .build()
    }
}