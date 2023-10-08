package com.bumble.appyx.navigation.node.cakes.component.spotlighthero.visualisation.main

import androidx.compose.foundation.gestures.Orientation
import com.bumble.appyx.navigation.node.cakes.component.spotlighthero.SpotlightHeroModel.State
import com.bumble.appyx.navigation.node.cakes.component.spotlighthero.SpotlightHeroModel.State.ElementState.CREATED
import com.bumble.appyx.navigation.node.cakes.component.spotlighthero.SpotlightHeroModel.State.ElementState.DESTROYED
import com.bumble.appyx.navigation.node.cakes.component.spotlighthero.SpotlightHeroModel.State.ElementState.STANDARD
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.property.impl.GenericFloatProperty
import com.bumble.appyx.interactions.core.ui.property.impl.GenericFloatProperty.Target
import com.bumble.appyx.interactions.core.ui.property.impl.Scale
import com.bumble.appyx.interactions.core.ui.property.impl.position.BiasAlignment.OutsideAlignment.Companion.InContainer
import com.bumble.appyx.interactions.core.ui.property.impl.position.BiasAlignment.OutsideAlignment.Companion.OutsideBottom
import com.bumble.appyx.interactions.core.ui.property.impl.position.BiasAlignment.OutsideAlignment.Companion.OutsideTop
import com.bumble.appyx.interactions.core.ui.property.impl.position.PositionAlignment
import com.bumble.appyx.interactions.core.ui.state.MatchedTargetUiState
import com.bumble.appyx.navigation.node.cakes.component.spotlighthero.SpotlightHeroModel.State.ElementState.SELECTED
import com.bumble.appyx.transitionmodel.BaseVisualisation

class SpotlightHeroMainVisualisation<InteractionTarget : Any>(
    uiContext: UiContext,
    @Suppress("UnusedPrivateMember")
    private val orientation: Orientation = Orientation.Horizontal, // TODO support RTL
) : BaseVisualisation<InteractionTarget, State<InteractionTarget>, MutableUiState, TargetUiState>(
    uiContext = uiContext
) {
    @Suppress("MaxLineLength")
    private val scrollX = GenericFloatProperty(uiContext.coroutineScope, Target(0f)) // TODO sync this with the model's initial value rather than assuming 0
    override val viewpointDimensions: List<Pair<(State<InteractionTarget>) -> Float, GenericFloatProperty>> =
        listOf(
            { state: State<InteractionTarget> -> state.activeIndex } to scrollX
        )

    private val created: TargetUiState = TargetUiState(
        positionAlignment = PositionAlignment.Target(OutsideTop),
        scale = Scale.Target(0f),
    )

    private val standard: TargetUiState = TargetUiState(
        positionAlignment = PositionAlignment.Target(InContainer),
        scale = Scale.Target(1f),
    )

    private val selected: TargetUiState = TargetUiState(
        positionAlignment = PositionAlignment.Target(InContainer),
        scale = Scale.Target(1.5f),
    )

    private val destroyed: TargetUiState = TargetUiState(
        positionAlignment = PositionAlignment.Target(OutsideBottom),
        scale = Scale.Target(0f),
    )

    override fun State<InteractionTarget>.toUiTargets(): List<MatchedTargetUiState<InteractionTarget, TargetUiState>> {
        return positions.flatMapIndexed { index, position ->
            position.elements.map {
                MatchedTargetUiState(
                    element = it.key,
                    targetUiState = TargetUiState(
                        base = when (it.value) {
                            CREATED -> created
                            STANDARD -> standard
                            SELECTED -> selected
                            DESTROYED -> destroyed
                        },
                        positionInList = index
                    )
                )
            }
        }
    }

    override fun mutableUiStateFor(uiContext: UiContext, targetUiState: TargetUiState): MutableUiState =
        targetUiState.toMutableState(uiContext, scrollX.renderValueFlow)

}

