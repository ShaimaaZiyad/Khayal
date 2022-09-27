package com.shaimaziyad.khayal.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.shaimaziyad.khayal.R

@BindingAdapter("setNovelCover")
fun setNovelCover(image: ImageView, uri: String){
    Glide.with(image.context)
        .load(uri.toUri())
        .placeholder(R.drawable.icon)
        .into(image)
}


@BindingAdapter("setSize")
fun setSize(text: TextView, size: Int) {
    text.text = ""
}


// todo : fix the welcome text by using xml file.
@BindingAdapter("setName")
fun setHi(text: TextView, name: String) {
    val welcome = text.context.resources.getString(R.string.welcome) + name
    text.text = name
}


// todo: we can update this to translate any error at run time.
@BindingAdapter("setError")
fun setError(text: TextView, error: String?){
    if (error != null){
        text.show()
        text.text = error
    }else {
        text.hide()
    }
}


@BindingAdapter("showLoading")
fun showLoading(v: View, status: DataStatus){
   when(status){
       DataStatus.LOADING ->{v.show()}
       DataStatus.SUCCESS ->{v.hide()}
       DataStatus.ERROR ->{v.hide()}
   }
}