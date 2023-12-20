package kr.ac.kumoh.ce.s20180904.s23termproject

import retrofit2.http.GET

interface WordApi {
    @GET("word")
    suspend fun getwordLIst(): List<Word>
}