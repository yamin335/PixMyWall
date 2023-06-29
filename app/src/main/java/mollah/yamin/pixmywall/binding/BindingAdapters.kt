package mollah.yamin.pixmywall.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.target.Target
import coil.transform.CircleCropTransformation

/**
 * Binding adapters for data binding
 */

class BindingAdapters {
    @BindingAdapter(value = ["imageUrl", "requestTarget"], requireAll = true)
    fun bindImageWithTarget(imageView: ImageView, url: String?, target: Target?) {
        val request = ImageRequest.Builder(imageView.context)
            .data(url)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .target(target)
            .build()
        ImageLoader.Builder(imageView.context).build().enqueue(request)
    }

    @BindingAdapter(value = ["imageUrl", "requestTarget"], requireAll = true)
    fun bindImage(imageView: ImageView, url: String?, target: Target?) {
        val request = ImageRequest.Builder(imageView.context)
            .data(url)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .target(target)
            .build()
        ImageLoader.Builder(imageView.context).build().enqueue(request)
    }

    @BindingAdapter(value = ["circularImageUrl", "requestTarget"], requireAll = true)
    fun bindCircularImage(imageView: ImageView, url: String?, target: Target?) {
        val request = ImageRequest.Builder(imageView.context)
            .data(url)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .transformations(
                CircleCropTransformation()
            )
            .target(target)
            .build()
        ImageLoader.Builder(imageView.context).build().enqueue(request)
    }

}



