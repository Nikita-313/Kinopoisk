package com.cinetech.data.network

import com.cinetech.data.network.model.ApiCommentsResponse
import com.cinetech.data.network.model.ApiLoadMoviesResponse
import com.cinetech.data.network.model.ApiMovieDto
import com.cinetech.data.network.model.ApiPossibleValueDto
import com.cinetech.data.network.model.ApiSearchMovieResponse
import com.cinetech.data.network.model.ApiUserPhotoUrl
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieService {

    @GET("/v1.4/movie")
    suspend fun loadMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("countries.name") countriesName: List<String>? = null,
        @Query("ageRating") ageRating: List<String>? = null,
        @Query("year") year: List<String>? = null,
        @Query("selectFields") selectFields: List<String> = arrayListOf("id", "name", "alternativeName", "year", "ageRating", "countries", "poster", "rating")
    ): ApiLoadMoviesResponse

    @GET("/v1.4/movie/search")
    suspend fun searchMoviesByName(
        @Query("query") name: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ApiSearchMovieResponse

    @GET("/v1/movie/possible-values-by-field")
    suspend fun loadPossibleValuesByField(@Query("field") field: String): List<ApiPossibleValueDto>


    @GET("/v1.4/movie/{id}")
    suspend fun loadMovieById(@Path("id") id: Long): ApiMovieDto

    @GET("/v1.4/review")
    suspend fun loadCommentsByMovieId(
        @Query("movieId") movieId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ApiCommentsResponse

    @GET("/v1.4/person/{id}")
    suspend fun loadUserUserPhotoUrlByUId(@Path("id") id: Int): ApiUserPhotoUrl
}
