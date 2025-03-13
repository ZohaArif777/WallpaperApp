package com.wallpaper.features.data_class

data class Wallpaper(
    val img: Int = 0,
    val txt: String? = "",
    val isCategory: Boolean = false,
    val imageUrl: String?=" "
)
{
    constructor(imageUrl: String) : this(0, "", false, imageUrl)
}
