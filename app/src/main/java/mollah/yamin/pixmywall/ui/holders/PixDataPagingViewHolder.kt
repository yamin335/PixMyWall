package mollah.yamin.pixmywall.ui.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.RoundedCornersTransformation
import mollah.yamin.pixmywall.R
import mollah.yamin.pixmywall.databinding.PixWallListItemBinding
import mollah.yamin.pixmywall.models.PixData
import mollah.yamin.pixmywall.utils.dpToPx

/**
 * View Holder for a [PixData] RecyclerView list item.
 */
class PixDataPagingViewHolder(
    private val binding: PixWallListItemBinding,
    private val callback: (PixData?) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var pixData: PixData? = null

    init {
        binding.root.setOnClickListener {
            callback(pixData)
        }
    }

    fun bind(data: PixData?) {
        if (data == null) {
            binding.info.visibility = View.GONE
        } else {
            this.pixData = data
            binding.info.visibility = View.VISIBLE
            binding.data = data
            binding.photo.load(data.previewURL) {
                crossfade(true)
                diskCachePolicy(CachePolicy.ENABLED)
                memoryCachePolicy(CachePolicy.ENABLED)
                transformations(RoundedCornersTransformation(
                    8.dpToPx(binding.root.context),
                    8.dpToPx(binding.root.context),
                    8.dpToPx(binding.root.context),
                    8.dpToPx(binding.root.context))
                )
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, callback: (PixData?) -> Unit): PixDataPagingViewHolder {
            val binding: PixWallListItemBinding =
                PixWallListItemBinding.inflate(LayoutInflater.from(parent.context))
            return PixDataPagingViewHolder(binding, callback)
        }
    }
}
