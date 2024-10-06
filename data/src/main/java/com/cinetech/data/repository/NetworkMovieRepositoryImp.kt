package com.cinetech.data.repository

import com.cinetech.data.mapping.toDomain
import com.cinetech.data.network.MovieService
import com.cinetech.domain.models.Response
import com.cinetech.domain.models.SearchMoviePageable
import com.cinetech.domain.models.SearchMoviesParam
import com.cinetech.domain.repository.NetworkMovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NetworkMovieRepositoryImp @Inject constructor(private val movieService: MovieService) : NetworkMovieRepository {

    override fun searchMovie(param: SearchMoviesParam): Flow<Response<out SearchMoviePageable>> {

        return flow {
            emit(Response.Loading)
            val response = movieService.searchMoviesByName(
                name = param.movieName,
                page = param.page,
                limit = param.limitNumber,
            ).toDomain()
            emit(Response.Success(response))
        }.catch { e ->
            val errorMessage = e.message
            emit(
                Response.Error(
                    message = errorMessage,
                    throwable = e
                )
            )
        }.flowOn(Dispatchers.IO)

    }


}
