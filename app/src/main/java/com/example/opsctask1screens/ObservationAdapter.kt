package com.example.opsctask1screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ObservationAdapter(private val dataset: List<ObservationModel>) : RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder>() {

    class ObservationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val speciesCodeTextView: TextView = view.findViewById(R.id.speciesCodeTextView)
        val comNameTextView: TextView = view.findViewById(R.id.comNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_observation, parent, false)
        return ObservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObservationViewHolder, position: Int) {
        val observation = dataset[position]
        holder.speciesCodeTextView.text = observation.speciesCode
        holder.comNameTextView.text = observation.comName
    }

    override fun getItemCount() = dataset.size
}
