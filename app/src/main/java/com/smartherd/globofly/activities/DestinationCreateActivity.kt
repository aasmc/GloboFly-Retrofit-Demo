package com.smartherd.globofly.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartherd.globofly.R
import com.smartherd.globofly.helpers.SampleData
import com.smartherd.globofly.models.Destination
import com.smartherd.globofly.services.DestinationService
import com.smartherd.globofly.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_destiny_create.*
import kotlinx.coroutines.*
import java.lang.Exception

class DestinationCreateActivity : AppCompatActivity() {

	private val activityJob = Job()
	private val createActivityScope = CoroutineScope(Dispatchers.Main + activityJob)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_destiny_create)

		setSupportActionBar(toolbar)
		val context = this

		// Show the Up button in the action bar.
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		btn_add.setOnClickListener {
			val newDestination = Destination()
			newDestination.city = et_city.text.toString()
			newDestination.description = et_description.text.toString()
			newDestination.country = et_country.text.toString()

			// To be replaced by retrofit code
//			SampleData.addDestination(newDestination)
			val destinationService = ServiceBuilder.buildService(DestinationService::class.java)

			createActivityScope.launch {
				withContext(Dispatchers.IO) {
					try {
						destinationService.addDestination(newDestination)
					} catch (e: Exception) {
						Toast.makeText(
							this@DestinationCreateActivity,
							"Failed to create a destination",
							Toast.LENGTH_SHORT
						)
							.show()
					}
				}
			}

			finish() // Move back to DestinationListActivity
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		createActivityScope.cancel()
	}
}
