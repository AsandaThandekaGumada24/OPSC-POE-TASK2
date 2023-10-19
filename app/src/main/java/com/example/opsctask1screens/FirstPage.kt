import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.opsctask1screens.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class FirstPage : Fragment(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_page, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        val detectLocationButton = view.findViewById<Button>(R.id.detectLocationButton)
        detectLocationButton.setOnClickListener { detectLocation() }

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    private fun detectLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val lat = location.latitude
                    val lng = location.longitude

                    // Move the map camera to the user's location
                    val userLocation = LatLng(lat, lng)
                    googleMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation))

                    fetchHotspots(lat, lng)
                }
            }
        }
    }

    private fun fetchHotspots(lat: Double, lng: Double) {
        val client = OkHttpClient.Builder().build()
        val url = "https://api.ebird.org/v2/ref/hotspot/geo?lat=$lat&lng=$lng"
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("X-eBirdApiToken", "6gclai1l9b4t")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()

                responseData?.let {
                    parseHotspots(responseData)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    private fun parseHotspots(responseData: String) {
        try {
            val jsonArray = JSONArray(responseData)

            for (i in 0 until jsonArray.length()) {
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                val name = jsonObject.getString("name")
                val lat = jsonObject.getDouble("lat")
                val lng = jsonObject.getDouble("lng")

                val hotspotLocation = LatLng(lat, lng)

                googleMap.addMarker(MarkerOptions().position(hotspotLocation).title(name))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
