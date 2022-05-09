package com.wael.tyvolunty.models

import java.io.Serializable
import com.google.gson.annotations.SerializedName
data class Volunteer (
    @SerializedName("_id") val _id :String,
    @SerializedName("username") val username :String,
    @SerializedName("lastname") val lastname :String,
    @SerializedName("password") val password :String,
    @SerializedName("email") val email :String,
    @SerializedName("age") val age :String,
    @SerializedName("photo") val photo :String,
    @SerializedName("memberDate") val memberDate :String,
    @SerializedName("description") val description :String,
    @SerializedName("phone") val phone :String,
    @SerializedName("token") val token :String,


    ):Serializable