package com.wael.tyvolunty.models

import com.google.gson.annotations.SerializedName

data class Calls (
    @SerializedName("_id") val _id :String,
    @SerializedName("name") val name :String,
    @SerializedName("city") val city :String,
    @SerializedName("dateBegin") val dateBegin :String,
    @SerializedName("photo") val photo :String,
    @SerializedName("description") val description :String,
    @SerializedName("recruiter") val recruiter :String,
    @SerializedName("ageGroup") val ageGroup :String,
    @SerializedName("category") val category :String,
    @SerializedName("pending") val pending :String,
    @SerializedName("accepted") val accepted :String,
    @SerializedName("declined") val declined :String,







    )
