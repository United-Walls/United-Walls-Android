package com.paraskcd.unitedwalls.components

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.mxalbert.zoomable.OverZoomConfig
import com.mxalbert.zoomable.Zoomable
import com.mxalbert.zoomable.rememberZoomableState

@Composable
fun NetworkImage(imageURL: String, imageDescription: String, size: Dp, shape: Shape) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context = context).data(imageURL).diskCacheKey(imageURL)
            .memoryCacheKey(imageURL).build(),
        imageLoader = ImageLoader.Builder(context = context).components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.crossfade(true).build()
    )

    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = painter,
            contentDescription = imageDescription,
            modifier = Modifier
                .padding(10.dp)
                .clip(shape)
                .size(size)
                .background(MaterialTheme.colorScheme.tertiary)
        )

        if(painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun WallpaperImage(imageURL: String, imageDescription: String, height: Dp) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context = context).data(imageURL).diskCacheKey(imageURL).memoryCacheKey(imageURL).memoryCachePolicy(
            CachePolicy.ENABLED).build(),
        imageLoader = ImageLoader.Builder(context = context).components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.crossfade(true).build()
    )

    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = painter,
            contentDescription = imageDescription,
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(MaterialTheme.colorScheme.tertiary),
            contentScale = ContentScale.FillWidth
        )
        if(painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun WallpaperScreenImage(imageURL: String, imageDescription: String, width: Dp) {
    val context = LocalContext.current
    val state = rememberZoomableState(
        overZoomConfig = OverZoomConfig(minSnapScale = 1f, maxSnapScale = 1f)
    )

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context = context).data(imageURL).diskCacheKey(imageURL).memoryCacheKey(imageURL).build(),
        imageLoader = ImageLoader.Builder(context = context).components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.crossfade(true).build()
    )

    Box(contentAlignment = Alignment.Center) {
        Zoomable(
            state = state,
            modifier = Modifier
                .fillMaxHeight()
                .width(width)
                .padding(8.dp)
                .zIndex(if (state.isZooming) 1f else 0f)
        ) {
            Image(
                painter = painter,
                contentDescription = imageDescription,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(width)
                    .padding(12.dp)
                    .shadow(elevation = 12.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.tertiary),
                contentScale = ContentScale.Crop
            )
        }

        if(painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun WallpaperBackground(imageURL: String, imageDescription: String) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context = context).data(imageURL).diskCacheKey(imageURL).memoryCacheKey(imageURL).build(),
        imageLoader = ImageLoader.Builder(context = context).components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.crossfade(true).build()
    )

    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = painter,
            contentDescription = imageDescription,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}