package mollah.yamin.pixmywall.ui.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
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

    private var currentAnimator: AnimatorSet? = null

    private var isPreviewDialogOpen = false

    private lateinit var imageBitmap: Bitmap

    private lateinit var triple: Triple<View, RectF, Float>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPreviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                if (isPreviewDialogOpen) {
                    startDismissLargePreviewAnimation(triple.first, triple.second, triple.third)
                } else {
                    popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callback
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imageBitmap = (ContextCompat.getDrawable(mContext, R.drawable.img_placeholder)!! as BitmapDrawable).bitmap

        binding.btnBack.setOnClickListener { popBackStack() }
        binding.btnExpand.setOnClickListener { zoomImageFromThumb(binding.startView, imageBitmap) }

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
            listener(
                onSuccess = { _, result ->
                    imageBitmap = (result.drawable as BitmapDrawable).bitmap
                },
                onError = {_, _ ->
                    //binding.photo.setImageDrawable(imageDrawable)
                }
            )
        }

        binding.photo

        binding.userPhoto.load(data.userImageURL) {
            crossfade(true)
            placeholder(R.drawable.img_placeholder)
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

    private fun zoomImageFromThumb(thumbView: View, bitmap: Bitmap) {
        // If there's an animation in progress, cancel it immediately and
        // proceed with this one.
        currentAnimator?.cancel()

        // Load the high-resolution "zoomed-in" image.
        binding.expandedPreview.setImageBitmap(bitmap)

        // Calculate the starting and ending bounds for the zoomed-in image.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the
        // container view. Set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBoundsInt)
        binding.container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        // Using the "center crop" technique, adjust the start bounds to be the
        // same aspect ratio as the final bounds. This prevents unwanted
        // stretching during the animation. Calculate the start scaling factor.
        // The end scaling factor is always 1.0.
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally.
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically.
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it positions the zoomed-in view in the place of the
        // thumbnail.
        thumbView.alpha = 0f

        animateZoomToLargeImage(startBounds, finalBounds, startScale)

        setDismissLargeImageAnimation(thumbView, startBounds, startScale)
    }

    private fun animateZoomToLargeImage(startBounds: RectF, finalBounds: RectF, startScale: Float) {
        binding.expandedDialog.visibility = View.VISIBLE

        // Set the pivot point for SCALE_X and SCALE_Y transformations to the
        // top-left corner of the zoomed-in view. The default is the center of
        // the view.
        binding.expandedDialog.pivotX = 0f
        binding.expandedDialog.pivotY = 0f

        // Construct and run the parallel animation of the four translation and
        // scale properties: X, Y, SCALE_X, and SCALE_Y.
        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    binding.expandedDialog,
                    View.X,
                    startBounds.left,
                    finalBounds.left)
            ).apply {
                with(ObjectAnimator.ofFloat(binding.expandedDialog, View.Y, startBounds.top, finalBounds.top))
                with(ObjectAnimator.ofFloat(binding.expandedDialog, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(binding.expandedDialog, View.SCALE_Y, startScale, 1f))
            }
            duration = 300.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                    isPreviewDialogOpen = true
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }
    }

    private fun setDismissLargeImageAnimation(thumbView: View, startBounds: RectF, startScale: Float) {
        // When the zoomed-in image is tapped, it zooms down to the original
        // bounds and shows the thumbnail instead of the expanded image.
        triple = Triple(thumbView, startBounds, startScale)
        binding.btnCloseDialog.setOnClickListener {
            startDismissLargePreviewAnimation(thumbView, startBounds, startScale)
        }
    }

    private fun startDismissLargePreviewAnimation(thumbView: View, startBounds: RectF, startScale: Float) {
        currentAnimator?.cancel()

        // Animate the four positioning and sizing properties in parallel,
        // back to their original values.
        currentAnimator = AnimatorSet().apply {
            play(ObjectAnimator.ofFloat(binding.expandedDialog, View.X, startBounds.left)).apply {
                with(ObjectAnimator.ofFloat(binding.expandedDialog, View.Y, startBounds.top))
                with(ObjectAnimator.ofFloat(binding.expandedDialog, View.SCALE_X, startScale))
                with(ObjectAnimator.ofFloat(binding.expandedDialog, View.SCALE_Y, startScale))
            }
            duration = 300.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    thumbView.alpha = 1f
                    binding.expandedDialog.visibility = View.GONE
                    currentAnimator = null
                    isPreviewDialogOpen = false
                }

                override fun onAnimationCancel(animation: Animator) {
                    thumbView.alpha = 1f
                    binding.expandedDialog.visibility = View.GONE
                    currentAnimator = null
                }
            })
            start()
        }
    }
}