package mollah.yamin.pixmywall.ui.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import mollah.yamin.pixmywall.models.PixData
import mollah.yamin.pixmywall.ui.holders.PixDataPagingViewHolder

/**
 * Adapter for the list of repositories.
 */
class PixDataPagingAdapter constructor(
    private val callback: (PixData?) -> Unit
) : PagingDataAdapter<PixData, ViewHolder>(PIX_DATA_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return PixDataPagingViewHolder.create(parent = parent, callback = callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pixData = getItem(position)
        (holder as PixDataPagingViewHolder).bind(pixData)
    }

    companion object {
        private val PIX_DATA_COMPARATOR = object : DiffUtil.ItemCallback<PixData>() {
            override fun areItemsTheSame(oldItem: PixData, newItem: PixData): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PixData, newItem: PixData): Boolean =
                    oldItem == newItem
        }
    }
}