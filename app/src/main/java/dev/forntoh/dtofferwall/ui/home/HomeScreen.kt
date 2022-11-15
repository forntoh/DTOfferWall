package dev.forntoh.dtofferwall.ui.home

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import dev.forntoh.common.entities.Offer
import dev.forntoh.dtofferwall.ui.OfferWallViewModel

@Composable
fun HomeScreen(
    offerWallViewModel: OfferWallViewModel,
    modifier: Modifier = Modifier
) {
    val offersCollection by offerWallViewModel.offers.observeAsState(emptyList())

    HomeScreen(
        offersCollection = offersCollection,
        onSubmit = offerWallViewModel::updateFilters,
        modifier = modifier.statusBarsPadding(),
        loadMore = offerWallViewModel::nextPage,
    )
}

@Composable
fun HomeScreen(
    offersCollection: List<Offer>,
    modifier: Modifier = Modifier,
    onSubmit: (String, String, String) -> Unit = { _, _, _ -> },
    loadMore: () -> Unit = {},
) {

}