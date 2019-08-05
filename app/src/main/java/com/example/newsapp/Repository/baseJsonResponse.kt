package com.example.newsapp.Repository
import com.google.gson.annotations.SerializedName

data class baseJsonResponse (@SerializedName("response") val response : Response)