package com.cinetech.domain.repository

import com.cinetech.domain.models.CommentsResponse
import com.cinetech.domain.models.LoadCommentsParam


interface CommentsRepository {
    suspend fun loadCommentsByMovieIdUseCase(param: LoadCommentsParam): CommentsResponse
}