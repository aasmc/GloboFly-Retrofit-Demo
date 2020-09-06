package com.smartherd.globofly.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.smartherd.globofly.R
import com.smartherd.globofly.services.MessageService
import com.smartherd.globofly.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.coroutines.*
import java.lang.Exception

class WelcomeActivity : AppCompatActivity() {

	private val job = Job()
	private val welcomeScope = CoroutineScope(Dispatchers.Main + job)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_welcome)

		// To be replaced by retrofit code
//		message.text = "Black Friday! Get 50% cash back on saving your first spot."
		val messageService = ServiceBuilder.buildService(MessageService::class.java)

		welcomeScope.launch {
			withContext(Dispatchers.IO) {
				try {
					val msg = messageService.getMessages("http://10.0.2.2:7000/messages")
					withContext(Dispatchers.Main) {
						message.text = msg
					}
				} catch (e: Exception) {
					Toast.makeText(
						this@WelcomeActivity,
						"Failed to retrieve a message",
						Toast.LENGTH_SHORT
					).show()
				}
			}
		}
	}

	fun getStarted(view: View) {
		val intent = Intent(this, DestinationListActivity::class.java)
		startActivity(intent)
		finish()
	}

	override fun onDestroy() {
		super.onDestroy()
		welcomeScope.cancel()
	}
}
