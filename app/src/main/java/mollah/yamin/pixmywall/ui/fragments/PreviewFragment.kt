package mollah.yamin.pixmywall.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.size.Scale
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.google.android.material.chip.Chip
import mollah.yamin.pixmywall.R
import mollah.yamin.pixmywall.databinding.FragmentPreviewBinding
import mollah.yamin.pixmywall.ui.base.BaseFragment
import mollah.yamin.pixmywall.utils.dpToPx

class PreviewFragment : BaseFragment() {
    private lateinit var binding: FragmentPreviewBinding
    private val args: PreviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPreviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //registerToolbar(binding.toolbar)
        binding.btnBack.setOnClickListener { popBackStack() }

        val data = args.pixData
        binding.data = data

        binding.photo.load(data.largeImageURL) {
            crossfade(true)
            scale(Scale.FIT)
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            transformations(
                RoundedCornersTransformation(
                8.dpToPx(binding.root.context),
                8.dpToPx(binding.root.context),
                8.dpToPx(binding.root.context),
                8.dpToPx(binding.root.context))
            )
        }

        binding.userPhoto.load(data.userImageURL) {
            crossfade(true)
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            transformations(
                CircleCropTransformation()
            )
        }
        var id = 0
        data.tags?.split(",")?.asSequence()?.forEach { tag ->
            binding.tagGroup.addView(createTagChip(binding.root.context, id++, tag.trim()))
        }
    }

    private fun createTagChip(context: Context, chipId: Int, value: String): Chip {
        val chip = LayoutInflater.from(context).inflate(R.layout.list_item_tag_large,null) as Chip
        chip.id = chipId
        chip.text = value
        return chip
    }
}