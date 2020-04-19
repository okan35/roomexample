package com.example.roomexample.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomexample.R
import com.example.roomexample.data.User

class UsersListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<UsersListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var users = emptyList<User>() // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = users[position]
        holder.wordItemView.text = current.firstName
    }

    internal fun setWords(words: List<User>) {
        this.users = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = users.size
}