package com.bumble.appyx.navigation.node.cakes

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bumble.appyx.navigation.composable.AppyxComponent
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.ParentNode
import com.bumble.appyx.navigation.node.cakes.CakeListNode.NavTarget
import com.bumble.appyx.navigation.node.cakes.component.spotlighthero.SpotlightHero
import com.bumble.appyx.navigation.node.cakes.component.spotlighthero.SpotlightHeroModel
import com.bumble.appyx.navigation.node.cakes.component.spotlighthero.SpotlightHeroModel.Mode.HERO
import com.bumble.appyx.navigation.node.cakes.component.spotlighthero.SpotlightHeroModel.Mode.LIST
import com.bumble.appyx.navigation.node.cakes.component.spotlighthero.operation.toggleHeroMode
import com.bumble.appyx.navigation.node.cakes.component.spotlighthero.visualisation.SpotlightHeroGestures
import com.bumble.appyx.navigation.node.cakes.component.spotlighthero.visualisation.backdrop.SpotlightHeroBackdropVisualisation
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize

class CakeListNode(
    buildContext: BuildContext,
    private val spotlight: SpotlightHero<NavTarget> =
        SpotlightHero(
            model = SpotlightHeroModel(
                items = List(7) { NavTarget.CakeDetails(it) },
                initialActiveIndex = 0f,
                savedStateMap = buildContext.savedStateMap
            ),
            visualisation = { SpotlightHeroBackdropVisualisation(it) },
             gestureFactory = { SpotlightHeroGestures(it) }
        )
) : ParentNode<NavTarget>(
    buildContext = buildContext,
    appyxComponent = spotlight
) {

    sealed class NavTarget : Parcelable {
        @Parcelize
        data class CakeDetails(
            // TODO create dedicated type
            val index: Int
        ) : NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node =
        when (navTarget) {
            is NavTarget.CakeDetails -> CakeDetailsNode(buildContext) {
                spotlight.toggleHeroMode()
            }
        }

    @Composable
    override fun View(modifier: Modifier) {
        val mode = spotlight.mode.collectAsState(LIST)
        val width by animateFloatAsState(
            targetValue = when (mode.value) {
                LIST -> 0.6f
                HERO -> 1f
            }
        )

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AppyxComponent(
                appyxComponent = spotlight,
                modifier = Modifier
                    .fillMaxWidth(width)
                    .fillMaxHeight(1f)
                    .background(Color.DarkGray)
            )
        }
    }
}
