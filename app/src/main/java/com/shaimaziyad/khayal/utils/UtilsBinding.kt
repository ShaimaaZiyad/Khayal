package com.shaimaziyad.khayal.utils


import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.shaimaziyad.khayal.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("setImage")
fun setImage(image: ImageView, uri: String?){
    if(!uri.isNullOrBlank()) {
        Glide.with(image.context)
            .load(uri.toUri())
            .placeholder(R.drawable.icon)
            .into(image)
    }
}


@BindingAdapter("setSize")
fun setSize(text: TextView, size: Int) {
    text.text = ""
}


// todo : fix the welcome text by using xml file.
@BindingAdapter("setName")
fun setHi(text: TextView, name: String?) {
    text.text = name
}


// todo: we can update this to translate any error at run time.
@BindingAdapter("setError")
fun setError(text: TextView, error: String?){
    if (!error.isNullOrBlank()){
        text.show()
        text.text = error
    }else {
        text.hide()
    }
}


@BindingAdapter("showLoading")
fun showLoading(v: View, status: DataStatus?) {
    if (status != null){
        when(status){
            DataStatus.LOADING ->{v.show()}
            DataStatus.SUCCESS ->{v.hide()}
            DataStatus.ERROR ->{v.hide()}

        }
    }
}

@BindingAdapter("showIfDataNotExist")
fun showIfDataNotExist(v: View, b: Boolean?) {
    if (b == true){ //data is not found
        v.show()
    }else { // data is exist
        v.hide()
    }
}



@BindingAdapter("hideViewIfCustomer")
fun hideViewIfCustomer(v: View, b: Boolean?) {
    if (b == true){
        v.hide()

    }else{
        v.show()
    }
}

@BindingAdapter("showViewIfCustomer")
fun showViewIfCustomer(v: View, b: Boolean?) {
    if (b != true){
        v.hide()
    }else{
        v.show()
    }
}

@BindingAdapter("updateSubmitStatus")
fun updateSubmitStatus(button: Button, isEdit: Boolean?) {
    if (isEdit == true){
        button.setText(R.string.update)
    }else{
        button.setText(R.string.send)
    }
}

@BindingAdapter("swipeToRefresh")
fun swipeToRefresh(refreshLayout: SwipeRefreshLayout, status: DataStatus?) {
    if (status != null){
        when(status){
            DataStatus.LOADING ->{ } // do nothing
            DataStatus.SUCCESS ->{ refreshLayout.isRefreshing = false}
            DataStatus.ERROR ->{ refreshLayout.isRefreshing = false}
        }
    }
}


@BindingAdapter("joinedAt")
fun joinedAt(tv: TextView, date: Date?) {
    if (date != null){
        val date = SimpleDateFormat("dd/MM/yyyy").format(date)
        tv.text = date.toString()
    }
}



// todo: dis enable send button during uploading novel data


@BindingAdapter("setButtonStatus")
fun buttonStatus(button: Button, status: DataStatus?){
    if (status != null){
        when(status){
            DataStatus.LOADING -> { button.isEnabled = false }
            DataStatus.SUCCESS -> {button.isEnabled = true }
            DataStatus.ERROR -> { button.isEnabled = true}
        }
    }
}