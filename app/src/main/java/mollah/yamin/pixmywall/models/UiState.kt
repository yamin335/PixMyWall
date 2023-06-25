package mollah.yamin.pixmywall.models

import mollah.yamin.pixmywall.utils.AppConstants.DEFAULT_QUERY

data class UiState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false
)
