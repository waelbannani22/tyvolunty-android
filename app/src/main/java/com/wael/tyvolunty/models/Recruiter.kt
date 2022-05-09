package com.wael.tyvolunty.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Recruiter (
    @SerializedName("_id") val _id :String,
    @SerializedName("name") val name :String,

    @SerializedName("password") val password :String,
    @SerializedName("email") val email :String,

    @SerializedName("photo") val photo :String,
    @SerializedName("organization") val organization :String,
    @SerializedName("description") val description :String,
    @SerializedName("phone") val phone :String,
    @SerializedName("token") val token :String,


    ): Serializable