package com.shaimaziyad.khayal.utils


import android.view.View
import android.widget.AutoCompleteTextView
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

@BindingAdapter("notifyFormat")
fun notifyFormat(tv: TextView, date: Date?) {
    if (date != null){
        val date = SimpleDateFormat("h:mm:aa").format(date)
        tv.text = date.toString()
    }
}


@BindingAdapter("joinedAt")
fun joinedAt(tv: TextView, date: Date?) {
    if (date != null){
        val date = SimpleDateFormat("dd/MM/yyyy").format(date)
        tv.text = date.toString()
    }
}

@BindingAdapter("setCount")
fun setCount(tv: TextView, v: Int){
    tv.text = tv.context.getString(R.string.count,v.toString())
}

@BindingAdapter("setReviews")
fun setReviews(tv: TextView, v: Int){
    tv.text = tv.context.getString(R.string.reviews,v.toString())
}

fun setPages(tv: TextView, v:Int){

}

@BindingAdapter("setNovelCategory")
fun setNovelCategory(t: AutoCompleteTextView,value: String?){
    if (!value.isNullOrEmpty()){
        val v = value.toInt()
        t.setText(getNovelCategoryByKey(t.context, v))
    }
}


@BindingAdapter("setNovelType")
fun setNovelType(t: AutoCompleteTextView,value: String?){
    if (!value.isNullOrEmpty()){
        val v = value.toInt()
        t.setText(getNovelTypeKey(t.context, v))
    }
}

@BindingAdapter("setNovelCategory")
fun setNovelCategory(t: TextView,value: String?){
    if (!value.isNullOrEmpty()){
        val v = value.toInt()
        t.text = getNovelCategoryByKey(t.context, v)
    }
}



@BindingAdapter("setNovelType")
fun setNovelType(t: TextView,value: String?){
    if (!value.isNullOrEmpty()){
        val v = value.toInt()
        t.text = getNovelTypeKey(t.context, v)
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