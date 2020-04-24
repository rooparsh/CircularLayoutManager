package com.darklabs.circularrecycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_data.view.*

class DataAdapter : RecyclerView.Adapter<DataAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            with(itemView) {
                tv_data.text = "$adapterPosition"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false)
    )

    override fun getItemCount(): Int = 100

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}