package com.wael.tyvolunty.utilis

import com.google.gson.JsonObject
import com.wael.tyvolunty.models.Calls
import com.wael.tyvolunty.models.Recruiter
import com.wael.tyvolunty.models.Volunteer

import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.ArrayList

interface ApiInterface {
    // volunteer******************
    @POST("/Signup")
    fun Signup(@Body map : HashMap<String, String> ): Call<Volunteer>
    @POST("/loginandroid")
    fun login(@Body map : HashMap<String, String> ): Call<Volunteer>
    @POST("/sendmail")
      suspend fun Sendmailvolunteer(@Body map : HashMap<String, String> ): Response<JsonObject>
    @POST("/h/{codesent}")
   suspend fun confirmcode(@Path("codesent") codesent: String?,@Body map: HashMap<String, String>) : Response<String>

   @POST("/k/{userId}")
    suspend fun change(@Path("userId") userId: String?,@Body map: HashMap<String, String>) :Response<JsonObject>

    @POST("/update_volunteerAndroid")
    suspend fun updateVolunteers(@Body map :Map<String, String> ) :Response<Volunteer>
    // Recruiter************
    @POST("/SignupRecrruiter")
   suspend fun SignupRecrruiter(@Body map : HashMap<String, String> ): Response<Recruiter>

    @POST("/fetchByCategoryandroid")
    suspend fun fetchbycategory(@Body map : HashMap<String, String> ): Response<MutableList<Calls>>
    @POST("/VolunteerCallandroid")
    suspend fun postforcall(@Body map : HashMap<String, String> ):Response<JsonObject>

    @POST("/loginRecruiterandroid")
    fun signinRecruiter(@Body map : HashMap<String, String> ): Call<Recruiter>
    @POST("/SignupRecrruiterandroid")
    suspend fun signupRecruiter(@Body map : HashMap<String, String> ): Response<JsonObject>
    @POST("/fetchByCategoryandroidRecruiter")
    suspend fun fetchcallsbyrecruiter(@Body map : HashMap<String, String> ): Response<MutableList<Calls>>
    companion object {

        //var BASE_URL="http://192.168.1.100:3000"
       var BASE_URL = "https://tyvoluntyapp.herokuapp.com"

        fun create() : ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}