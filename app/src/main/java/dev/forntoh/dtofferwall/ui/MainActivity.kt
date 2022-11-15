package dev.forntoh.dtofferwall.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
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
                Scaffold { innerPaddingModifier ->
                    HomeScreen(
                        offerWallViewModel = offerWallViewModel,
                        modifier = Modifier.padding(innerPaddingModifier)
                    )
                }
            }
        }
    }
}