package com.example.imageviewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FolderAdapter(
    private val folders: MutableList<FolderItem>,
    private val onRemoveClick: (FolderItem) -> Unit
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtFolderName: TextView = itemView.findViewById(R.id.txtFolderName)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_folder, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folders[position]
        holder.txtFolderName.text = folder.name
        holder.btnRemove.setOnClickListener {
            onRemoveClick(folder)
        }
    }

    override fun getItemCount(): Int = folders.size
}
