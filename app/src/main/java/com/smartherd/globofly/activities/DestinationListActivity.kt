package com.smartherd.globofly.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartherd.globofly.R
import com.smartherd.globofly.helpers.DestinationAdapter
import com.smartherd.globofly.services.DestinationService
import com.smartherd.globofly.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_destiny_list.*
import kotlinx.coroutines.*
import java.lang.Exception

class DestinationListActivity : AppCompatActivity() {


    private val activityJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + activityJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destiny_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener {
            val intent = Intent(this@DestinationListActivity, DestinationCreateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        loadDestinations()
    }

    private fun loadDestinations() {

        val destinationService = ServiceBuilder.buildService(DestinationService::class.java)

        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    // query map used for filtering requested data
                    val filter = HashMap<String, String>()
                    val destinationList = destinationService.getDestinationList(filter)
                    withContext(Dispatchers.Main) {
                        destiny_recycler_view.adapter = DestinationAdapter(destinationList)
                    }
                } catch (exception: Exception) {
                    Toast.makeText(
                        this@DestinationListActivity,
                        "Failed to retrieve items",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

//
//		val requestCall = destinationService.getDestinationList()
//		requestCall.enqueue(object : Callback<List<Destination>> {
//			override fun onResponse(
//				call: Call<List<Destination>>,
//				response: Response<List<Destination>>
//			) {
//				Log.d("DestinationListActivity", "onResponse: triggered")
//				if (response.isSuccessful) {
//					val destinationList = response.body()!!
//					destiny_recycler_view.adapter = DestinationAdapter(destinationList)
//				} else {
//
//					Toast.makeText(
//						this@DestinationListActivity,
//						"Failed to retrieve items",
//						Toast.LENGTH_SHORT
//					).show()
//				}
//			}
//
//			override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
//				Log.d("DestinationListActivity", "onFailure: request failed")
//			}
//
//		})

    }

    override fun onDestroy() {
        super.onDestroy()
        uiScope.cancel()
    }
}
