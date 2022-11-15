package dev.forntoh.dtofferwall.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.forntoh.dtofferwall.ui.home.HomeScreen
import dev.forntoh.dtofferwall.ui.theme.DTOfferWallTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val offerWallViewModel: OfferWallViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DTOfferWallTheme {
                HomeScreen(offerWallViewModel = offerWallViewModel)
            }
        }
    }
}