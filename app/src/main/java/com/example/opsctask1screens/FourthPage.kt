package com.example.opsctask1screens

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase




class FourthPage :Fragment() {

    private lateinit var metricRadioButton: RadioButton
    private lateinit var imperialRadioButton: RadioButton
    private lateinit var maxDistanceEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var notificationTextView: TextView

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView= inflater.inflate(R.layout.fragment_fourth_page, container, false)

        // Initialize Firebase components
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("tblUsers")

        // Initialize UI elements
        metricRadioButton = rootView.findViewById(R.id.metricRadioButton)
        imperialRadioButton = rootView.findViewById(R.id.imperialRadioButton)
        maxDistanceEditText = rootView.findViewById(R.id.maxDistanceEditText)
        saveButton = rootView.findViewById(R.id.saveButton)
        notificationTextView = rootView.findViewById(R.id.notificationTextView)

        // Load user settings
        sharedPreferences = requireActivity().getSharedPreferences("UserSettings", MODE_PRIVATE)
        loadUserSettings()

        // Save button click listener
        saveButton.setOnClickListener {
            saveUserSettings()
        }


        return rootView
    }

    private fun loadUserSettings() {
        val isMetric = sharedPreferences.getBoolean("isMetric", true)
        val maxDistance = sharedPreferences.getString("maxDistance", "10")

        metricRadioButton.isChecked = isMetric
        imperialRadioButton.isChecked = !isMetric
        maxDistanceEditText.setText(maxDistance)
    }

    private fun saveUserSettings() {
        val isMetric = metricRadioButton.isChecked
        val maxDistance = maxDistanceEditText.text.toString()

        // Save to shared preferences
        val editor = sharedPreferences.edit()
        editor.putBoolean("isMetric", isMetric)
        editor.putString("maxDistance", maxDistance)
        editor.apply()

        // Save to Firebase
        val userSettings = HashMap<String, Any>()
        userSettings["isMetric"] = isMetric
        userSettings["maxDistance"] = maxDistance

        databaseReference.child("Settings").updateChildren(userSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showNotification("Settings saved successfully")
                } else {
                    showNotification("Failed to save settings")
                }
            }
    }

    private fun showNotification(message: String) {
        notificationTextView.text = message
    }


}