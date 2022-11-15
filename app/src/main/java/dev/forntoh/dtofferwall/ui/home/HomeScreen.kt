package dev.forntoh.dtofferwall.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.forntoh.common.entities.Offer
import dev.forntoh.dtofferwall.ui.OfferWallViewModel

@Composable
fun HomeScreen(
    offerWallViewModel: OfferWallViewModel,
    modifier: Modifier = Modifier
) {
    val offerList by offerWallViewModel.offers.observeAsState(emptyList())

    HomeScreen(
        offerList = offerList,
        onSubmit = offerWallViewModel::updateFilters,
        modifier = modifier.statusBarsPadding(),
        loadMore = offerWallViewModel::nextPage,
    )
}

@Composable
fun HomeScreen(
    offerList: List<Offer>,
    modifier: Modifier = Modifier,
    onSubmit: (String, String, String) -> Unit = { _, _, _ -> },
    loadMore: () -> Unit = {},
) {
    var appId by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = appId,
                onValueChange = { appId = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Application ID")
                }
            )
            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "User ID")
                }
            )
            OutlinedTextField(
                value = token,
                onValueChange = { token = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Token")
                }
            )
            Button(
                onClick = { onSubmit(appId, userId, token) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Find offers")
            }
        }

        OfferList(
            offerList = offerList,
            loadMore = loadMore
        )
    }
}