package com.shaimaziyad.khayal1.utils

import com.airbnb.epoxy.EpoxyDataBindingPattern
import com.shaimaziyad.khayal1.R


@EpoxyDataBindingPattern(rClass = R::class, layoutPrefix = "item")
internal interface Config

/** here inside the layout prefix it must be layouts
 * that provide by epoxy start with the name of prefix that you will write it **/