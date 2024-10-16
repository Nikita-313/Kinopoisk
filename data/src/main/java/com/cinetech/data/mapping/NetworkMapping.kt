package com.cinetech.data.mapping

import com.cinetech.data.network.model.ApiComment
import com.cinetech.data.network.model.ApiCommentsResponse
import com.cinetech.data.network.model.ApiLinkedMovie
import com.cinetech.data.network.model.ApiLoadMoviesResponse
import com.cinetech.data.network.model.ApiMovieDto
import com.cinetech.data.network.model.ApiPossibleValueDto
import com.cinetech.data.network.model.ApiSearchMovieDto
import com.cinetech.data.network.model.ApiSearchMovieResponse
import com.cinetech.domain.models.Comment
import com.cinetech.domain.models.CommentsResponse
import com.cinetech.domain.models.LinkedMovie
import com.cinetech.domain.models.Movie
import com.cinetech.domain.models.PossibleValue
import com.cinetech.domain.models.PreviewMovie
import com.cinetech.domain.models.SearchMoviePageable

fun ApiComment.toDomain(): Comment {
    return Comment(
        id = id,
        title = title,
        type = type,
        review = review,
        date = date,
        author = author
    )
}

fun ApiCommentsResponse.toDomain(): CommentsResponse {
    return CommentsResponse(
        docs = docs.map { it.toDomain() },
        total = total,
        limit = limit,
        page = page,
        pages = pages,
    )
}

fun ApiLinkedMovie.toDomain(): LinkedMovie {
    return LinkedMovie(
        id = id,
        name = name,
        year = year,
        preViewUrl = poster?.previewUrl,
        kpRating = rating?.kp,
    )
}

fun ApiLoadMoviesResponse.toDomain(): SearchMoviePageable {
    return SearchMoviePageable(
        docs = docs.map { it.toDomainPreviewMovie() },
        total = total,
        limit = limit,
        page = page,
        pages = pages,
    )
}

fun ApiMovieDto.toDomainPreviewMovie(): PreviewMovie {
    return PreviewMovie(
        id = id ?: 0,
        name = name ?: "",
        alternativeName = alternativeName ?: "",
        year = year ?: 0,
        ageRating = ageRating ?: 0,
        countries = countries?.map { it.name } ?: emptyList(),
        previewUrl = poster?.previewUrl ?: "",
        kpRating = rating?.kp ?: 0.0,
    )
}

fun ApiMovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        name = name ?: "",
        year = year ?: 0,
        ageRating = ageRating ?: 0,
        countries = countries?.map { it.name } ?: emptyList(),
        posterUrl = poster?.url,
        kpRating = rating?.kp ?: 0.0,
        shortDescription = shortDescription ?: "",
        description = description ?: "",
        similarMovies = similarMovies?.map { it.toDomain() } ?: emptyList(),
        kpVotesNumber = votes?.kp?.toIntOrNull() ?: 0 ,
        enName = enName?: "",
        movieLength = movieLength ?: 0,
    )
}

fun ApiPossibleValueDto.toDomain(): PossibleValue {
    return PossibleValue(
        name = name,
        slug = slug,
    )
}

fun ApiSearchMovieDto.toDomain(): PreviewMovie {
    return PreviewMovie(
        id = id,
        name = name,
        alternativeName = enName ?: "",
        year = year,
        countries = country?.let { listOf(it) } ?: emptyList(),
        ageRating = ageRating,
        previewUrl = poster.previewUrl ?: "",
        kpRating = rating?.kp ?: 0.0,
    )
}

fun ApiSearchMovieResponse.toDomain(): SearchMoviePageable {
    return SearchMoviePageable(
        docs = docs.map { it.toDomain() },
        total = total,
        limit = limit,
        page = page,
        pages = pages,
    )
}

