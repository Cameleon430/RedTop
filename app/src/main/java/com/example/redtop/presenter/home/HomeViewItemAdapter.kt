package com.example.redtop.presenter.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.redtop.databinding.ItemPublicationBinding
import com.example.redtop.presenter.base.PublicationViewState
import com.squareup.picasso.Picasso

class HomeViewItemAdapter(
    private val listener: OnItemSelectListener? = null
) :RecyclerView.Adapter<HomeViewItemAdapter.ViewHolder>() {

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
        holder.onBind(item, listener)
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
        fun onBind(item: PublicationViewState, listener: OnItemSelectListener?) {
            with(binding) {
                root.setOnClickListener {
                    listener?.onClick(item)
                }

                authorTextView.text = item.author
                titleTextView.text = item.title
                timeStampTextView.text = "\u2022 ${item.timeStamp}"
                Picasso.get().load(item.media).into(mediaImageView)
                commentsCountImageView.text = item.commentsCount
            }
        }
    }

    interface OnItemSelectListener{
        fun onClick(publication: PublicationViewState)
    }

    //endregion

}