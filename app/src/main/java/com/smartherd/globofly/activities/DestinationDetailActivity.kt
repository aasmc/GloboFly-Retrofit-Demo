package com.smartherd.globofly.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.smartherd.globofly.R
import com.smartherd.globofly.helpers.SampleData
import com.smartherd.globofly.models.Destination
import com.smartherd.globofly.services.DestinationService
import com.smartherd.globofly.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_destiny_detail.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class DestinationDetailActivity : AppCompatActivity() {

    private val activityJob = Job()
    private val activityScope = CoroutineScope(Dispatchers.Main + activityJob)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destiny_detail)

        setSupportActionBar(detail_toolbar)
        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras

        if (bundle?.containsKey(ARG_ITEM_ID)!!) {

            val id = intent.getIntExtra(ARG_ITEM_ID, 0)

            loadDetails(id)

            initUpdateButton(id)

            initDeleteButton(id)
        }
    }

    private fun loadDetails(id: Int) {

//		// To be replaced by retrofit code
//		val destination = SampleData.getDestinationById(id)
//
//		destination?.let {
//			et_city.setText(destination.city)
//			et_description.setText(destination.description)
//			et_country.setText(destination.country)
//
//			collapsing_toolbar.title = destination.city
//		}

        val service = ServiceBuilder.buildService(DestinationService::class.java)
        activityScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val destination = service.getDestination(id)
                    withContext(Dispatchers.Main) {
                        destination.let {
                            et_city.setText(destination.city)
                            et_description.setText(destination.description)
                            et_country.setText(destination.country)

                            collapsing_toolbar.title = destination.city
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(
						this@DestinationDetailActivity,
						"Failed to retrieve destination $id",
						Toast.LENGTH_SHORT
					).show()
                }

            }
        }
    }

    private fun initUpdateButton(id: Int) {

        btn_update.setOnClickListener {

            val city = et_city.text.toString()
            val description = et_description.text.toString()
            val country = et_country.text.toString()

            val service = ServiceBuilder.buildService(DestinationService::class.java)

            activityScope.launch {
                withContext(Dispatchers.IO) {}
                try {
                    withContext(Dispatchers.Main) {
                        service.updateDestination(id, city, description, country)
                    }
                } catch (e: Exception) {
                    Toast.makeText(
						this@DestinationDetailActivity,
						"Failed to update a destination",
						Toast.LENGTH_SHORT
					).show()
                }
            }
            finish() // Move back to DestinationListActivity
        }
    }

    private fun initDeleteButton(id: Int) {

        btn_delete.setOnClickListener {

//            // To be replaced by retrofit code
//            SampleData.deleteDestination(id)
            val service = ServiceBuilder.buildService(DestinationService::class.java)

            val deleteRequest = service.deleteDestination(id)
            deleteRequest.enqueue(object : Callback<Unit> {
				override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
					if (response.isSuccessful) {
						finish()
						Toast.makeText(
							this@DestinationDetailActivity,
							"Successfully deleted the destination",
							Toast.LENGTH_SHORT
						)
							.show()
					} else {
						Toast.makeText(
							this@DestinationDetailActivity,
							"Failed to delete a destination",
							Toast.LENGTH_SHORT
						)
							.show()
					}
				}

				override fun onFailure(call: Call<Unit>, t: Throwable) {
					Toast.makeText(
						this@DestinationDetailActivity,
						"Failed to delete, caused by ${t.message}",
						Toast.LENGTH_SHORT
					).show()
				}

			})

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            navigateUpTo(Intent(this, DestinationListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }
}
