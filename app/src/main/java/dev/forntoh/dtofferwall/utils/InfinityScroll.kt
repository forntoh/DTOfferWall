package dev.forntoh.dtofferwall.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*

/**
 * Compose LazyListState to load more data when last item in list is visible
 *
 * @param buffer trigger distance from bottom
 * @param loadMore function to invoke
 * */
@Composable
fun LazyListState.OnBottomReached(
    buffer: Int = 0,
    loadMore: () -> Unit
) {

    require(buffer >= 0) { "buffer cannot be negative, but was $buffer" }

    // state object which tells us if we should load more
    val shouldLoadMore = remember {
        derivedStateOf {

            // get last visible item
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?:
                // list is empty
                // return false here if loadMore should not be invoked if the list is empty
                return@derivedStateOf true

            // Subtract buffer from the total items
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - buffer
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                // if should load more, then invoke loadMore
                if (it) loadMore()
            }
    }
}