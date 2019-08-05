package com.example.newsapp.Repository

import com.google.gson.annotations.SerializedName


data class Tags (

    @SerializedName("id") val id : String,
    @SerializedName("type") val type : String,
    @SerializedName("webTitle") val webTitle : String,
    @SerializedName("webUrl") val webUrl : String,
    @SerializedName("apiUrl") val apiUrl : String,
    @SerializedName("references") val references : List<String>,
    @SerializedName("bio") val bio : String,
    @SerializedName("firstName") val firstName : String,
    @SerializedName("lastName") val lastName : String
)