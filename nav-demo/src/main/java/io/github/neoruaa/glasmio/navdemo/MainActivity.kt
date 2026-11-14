package io.github.neoruaa.glasmio.navdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.neoruaa.glasmio.core.*
import io.github.neoruaa.glasmio.glass.*
import io.github.neoruaa.glasmio.icons.*
import io.github.neoruaa.glasmio.ui.*

/**
 * Minimal standalone reproduction of the Glasmio sample's Navigation demo:
 * a single screen with the liquid-glass bottom tab bar (Home / Favorites /
 * Search) and its jelly slide between tabs, plus the Compact Tab Bar switch
 * and the Toolbars section from the same demo page. No catalog, no settings.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlasmioIconProvider {
                GlasmioTheme {
                    GlasmioBackdropHost(
                        sampledColor = LocalGlassColors.current.groupedBackground,
                        modifier = Modifier.fillMaxSize(),
                        overlayContent = { NavDemoScreen() },
                    )
                }
            }
        }
    }
}

@Composable
private fun NavDemoScreen() {
    var selected by rememberSaveable { mutableIntStateOf(0) }
    var compact by rememberSaveable { mutableStateOf(false) }
    val compactProgress = animateFloatAsState(
        targetValue = if (compact) 1f else 0f,
        animationSpec = spring(dampingRatio = .8f, stiffness = 438.64908f),
        label = "Navigation compact progress",
    )
    var style by rememberSaveable { mutableIntStateOf(0) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, top = 64.dp, end = 20.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            Text("Navigation", fontSize = 34.sp, fontWeight = FontWeight.Bold)
        }
        item {
            GlasmioGroupedList {
                GlasmioListRow("Compact Tab Bar", showTopSeparator = false, trailing = { GlasmioSwitch(compact, { compact = it }) })
            }
        }
        item {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                GlasmioNavigationBar(
                    items = listOf(
                        GlasmioTabItem(0, "Home", SfSymbol.House),
                        GlasmioTabItem(1, "Favorites", SfSymbol.Heart),
                        GlasmioTabItem(2, "Search", SfSymbol.Search),
                    ),
                    selectedKey = selected,
                    onSelected = { selected = it },
                    onExpand = { compact = false },
                    compactProgress = compactProgress,
                    compactSize = 48.dp,
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Toolbars", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
                GlasmioPopUpButton(GlasmioTopBarStyle.entries[style], GlasmioTopBarStyle.entries, { style = it.ordinal }, { it.name })
                GlasmioTopBar("Title", subtitle = "Subtitle", style = GlasmioTopBarStyle.entries[style], actions = {
                    GlasmioIconButton(onClick = {}) { SfIcon("ellipsis", "More") }
                })
            }
        }
    }
}
