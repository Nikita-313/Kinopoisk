package com.cinetech.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cinetech.data.datasource.MoviePagingSourceFactory
import com.cinetech.data.mapping.toDomain
import com.cinetech.data.network.MovieService
import com.cinetech.domain.models.Movie
import com.cinetech.domain.models.PreviewMovie
import com.cinetech.domain.models.SearchMovieParam
import com.cinetech.domain.utils.Response
import com.cinetech.domain.models.SearchMoviePageable
import com.cinetech.domain.models.SearchMoviesByNameParam
import com.cinetech.domain.repository.NetworkMovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NetworkMovieRepositoryImp @Inject constructor(
    private val movieService: MovieService,
    private val moviePagingSourceFactory: MoviePagingSourceFactory,
) : NetworkMovieRepository {

    override fun searchMovieByName(param: SearchMoviesByNameParam): Flow<Response<out SearchMoviePageable>> {

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

    override fun searchMovie(param: SearchMovieParam): Flow<Response<out SearchMoviePageable>> {
        return flow {
            emit(Response.Loading)
            val response = movieService.loadMovies(
                page = param.page,
                limit = param.limitNumber,
                countriesName = param.countries,
                ageRating = param.ageRantingRange?.let { listOf("${it.from}-${it.to}") },
                year = param.yearRange?.let { listOf("${it.from}-${it.to}") }
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

    override fun searchMoviesPaging(query: String, pageSize: Int): Flow<PagingData<PreviewMovie>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { moviePagingSourceFactory.create(query) }
        ).flow
    }

    override fun getMovieById(id:Long): Flow<Response<out Movie>>{
        return flow {
            emit(Response.Loading)
            val response = movieService.loadMovieById(id).toDomain()
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
