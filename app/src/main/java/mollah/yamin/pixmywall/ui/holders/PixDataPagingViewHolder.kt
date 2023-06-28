package mollah.yamin.pixmywall.ui.holders

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load

import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.google.android.material.R.style
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
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
        binding.tagGroup.removeAllViews()
        if (data == null) {
            binding.info.visibility = View.GONE
        } else {
            this.pixData = data
            binding.info.visibility = View.VISIBLE
            binding.data = data

            val mContext = binding.root.context
            val imageLoader = ImageLoader.Builder(mContext).build()

            val request = ImageRequest.Builder(mContext)
                .data(data.previewURL)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
//            .transformations(
//                RoundedCornersTransformation(
//                    8.dpToPx(binding.root.context),
//                    8.dpToPx(binding.root.context),
//                    8.dpToPx(binding.root.context),
//                    8.dpToPx(binding.root.context))
//            )
                .target(
                    onStart = { placeholder ->
                        // Handle the placeholder drawable.
                    },
                    onSuccess = { result ->
                        // Handle the successful result.
                        binding.photo.setImageDrawable(result)
                    },
                    onError = { error ->
                        // Handle the error drawable.
                    }
                )
                .build()
            imageLoader.enqueue(request)

//            binding.photo.load(data.previewURL) {
//                crossfade(true)
//                placeholder(R.drawable.img_placeholder)
//            }
            var id = 0
            data.tags?.split(",")?.asSequence()?.forEach { tag ->
                binding.tagGroup.addView(createTagChip(binding.root.context, id++, tag.trim()))
            }
            binding.tagGroup.requestLayout()
        }
    }

    private fun createTagChip(context: Context, chipId: Int, value: String): Chip {
        val chip = LayoutInflater.from(context).inflate(R.layout.list_item_tag,null) as Chip
        chip.id = chipId
        chip.text = value
        return chip
    }

    companion object {
        fun create(parent: ViewGroup, callback: (PixData?) -> Unit): PixDataPagingViewHolder {
            val binding: PixWallListItemBinding =
                PixWallListItemBinding.inflate(LayoutInflater.from(parent.context))
            return PixDataPagingViewHolder(binding, callback)
        }
    }
}
