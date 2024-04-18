package com.example.nbc_applemarket.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data (
    val name : String,
    val title : String,
    val description : String,
    val location : String,
    val price : Int,
    val image : Int,
    val chatNum : Int,
    val likeNum : Int,
    val mannerTemperature : Double,
    var isHeart: Boolean
):Parcelable