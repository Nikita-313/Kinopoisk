package com.cinetech.ui.screen.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import com.cinetech.domain.repository.NetworkMovieRepository
import com.cinetech.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedState: SavedStateHandle,
    networkMovieRepository: NetworkMovieRepository,
) :ViewModel() {

    val searchText: String = savedState.toRoute<Screen.Search>().searchText

    val searchMovies = networkMovieRepository.searchMoviesPaging(searchText).cachedIn(viewModelScope)

}