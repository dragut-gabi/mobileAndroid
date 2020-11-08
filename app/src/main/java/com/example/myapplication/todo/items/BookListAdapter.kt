package com.example.myapplication.todo.items

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.core.TAG
import com.example.myapplication.todo.data.Book
import com.example.myapplication.todo.item.BookEditFragment
import kotlinx.android.synthetic.main.view_item.view.*

class BookListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    var items = emptyList<Book>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onItemClick: View.OnClickListener;

    init {
        onItemClick = View.OnClickListener { view ->
            val item = view.tag as Book
            fragment.findNavController().navigate(R.id.fragment_item_edit, Bundle().apply {
                putString(BookEditFragment.ITEM_ID, item._id)
                putString(BookEditFragment.HORSEPOWER,item.pages.toString())
                putBoolean(BookEditFragment.AUTOMATIC,item.sold)
                putString(BookEditFragment.RELEASE_DATE,item.releaseDate)
                putString(BookEditFragment.RELEASE_DATE,item.releaseDate)
                putString(BookEditFragment.USER_ID,item.userId)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val item = items[position]
        holder.itemView.tag = item
        holder.textView.text = item.title
        holder.itemView.setOnClickListener(onItemClick)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.text
    }
}