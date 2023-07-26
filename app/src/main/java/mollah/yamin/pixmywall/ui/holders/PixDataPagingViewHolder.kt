package mollah.yamin.pixmywall.ui.holders

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.target.Target
import com.google.android.material.chip.Chip
import mollah.yamin.pixmywall.R
import mollah.yamin.pixmywall.databinding.PixWallListItemBinding
import mollah.yamin.pixmywall.models.PixData

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
            binding.requestTarget = object : Target {
                override fun onSuccess(result: Drawable) {
                    super.onSuccess(result)
                    // Handle the successful result.
                    binding.photo.setImageDrawable(result)
                }
            }
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
                PixWallListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PixDataPagingViewHolder(binding, callback)
        }
    }
}
