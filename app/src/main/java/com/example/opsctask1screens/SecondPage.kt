import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.opsctask1screens.ObservationAdapter
import com.example.opsctask1screens.ObservationModel
import com.example.opsctask1screens.R
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class SecondPage : Fragment() {
    private val client = OkHttpClient.Builder().build()
    private val observations = mutableListOf<ObservationModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second_page, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.observationRecyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val adapter = ObservationAdapter(observations)
        recyclerView.adapter = adapter

        // Fetch data from the API and update the observations list
        fetchData()

        return view
    }

    private fun fetchData() {
        val request = Request.Builder()
            .url("https://api.ebird.org/v2/data/obs/ZA/recent")
            .get()
            .addHeader("X-eBirdApiToken", "6gclai1l9b4t")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()

                    responseData?.let {
                        // Parse the JSON data and update the observations list
                        parseObservations(responseData)

                        // Notify the adapter that the data has changed
                        requireActivity().runOnUiThread {
                            val recyclerView = view?.findViewById<RecyclerView>(R.id.observationRecyclerView)
                            recyclerView?.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }

    private fun parseObservations(responseData: String) {
        try {
            val jsonArray = JSONArray(responseData)
            for (i in 0 until jsonArray.length()) {
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val observation = ObservationModel(
                    jsonObject.getString("id"),
                    jsonObject.getString("speciesCode"),
                    jsonObject.getString("comName"),
                    jsonObject.getString("sciName"),
                    jsonObject.getString("locName")
                )
                observations.add(observation)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
