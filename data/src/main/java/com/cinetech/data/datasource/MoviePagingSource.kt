package com.cinetech.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cinetech.data.mapping.toDomain
import com.cinetech.data.network.MovieService
import com.cinetech.domain.models.PreviewMovie
import kotlinx.coroutines.delay
import kotlin.random.Random

class MoviePagingSource(
    private val movieName: String,
    private val movieService: MovieService,
) : PagingSource<Int, PreviewMovie>() {

    override fun getRefreshKey(state: PagingState<Int, PreviewMovie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PreviewMovie> {
        return try {
            val page = params.key ?: 1
            val response = movieService.searchMoviesByName(page = page, limit = params.loadSize, name = movieName)
            LoadResult.Page(
                data = response.docs.map { it.toDomain() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.docs.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}