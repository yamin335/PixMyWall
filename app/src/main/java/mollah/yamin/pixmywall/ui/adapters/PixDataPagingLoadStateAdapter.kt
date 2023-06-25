package mollah.yamin.pixmywall.ui.adapters

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import mollah.yamin.pixmywall.ui.holders.PixDataPagingLoadStateViewHolder

class PixDataPagingLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<PixDataPagingLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: PixDataPagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PixDataPagingLoadStateViewHolder {
        return PixDataPagingLoadStateViewHolder.create(parent, retry)
    }
}
