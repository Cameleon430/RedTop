package com.example.redtop.presenter.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.redtop.databinding.ItemPublicationBinding
import com.example.redtop.presenter.base.PublicationViewState
import com.squareup.picasso.Picasso

class HomeViewItemAdapter :RecyclerView.Adapter<HomeViewItemAdapter.ViewHolder>() {

    //region Properties

    private var items: List<PublicationViewState> = emptyList()

    //endregion

    //region Actions

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPublicationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: PublicationViewState = items[position]
        holder.onBind(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItemsView(publications: List<PublicationViewState>){
        items = publications
        notifyDataSetChanged()
    }

    //endregion

    //region Nested

    class ViewHolder(
        private val binding: ItemPublicationBinding
    ) :RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: PublicationViewState) {
            with(binding) {
                authorTextView.text = item.author
                titleTextView.text = item.title
                timeStampTextView.text = item.timeStamp.toString()
                Picasso.get().load(item.image).into(mediaImageView)
                commentsCountImageView.text = item.commentsCount
            }
        }
    }

    //endregion

}