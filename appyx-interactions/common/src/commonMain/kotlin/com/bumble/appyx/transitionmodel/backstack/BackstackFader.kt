package com.bumble.appyx.transitionmodel.backstack

import DefaultAnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.ui.Modifier
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.state.BaseUiState
import com.bumble.appyx.interactions.core.ui.state.MatchedUiState
import com.bumble.appyx.interactions.core.ui.property.impl.Alpha
import com.bumble.appyx.transitionmodel.BaseMotionController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class BackstackFader<InteractionTarget : Any>(
    uiContext: UiContext,
    defaultAnimationSpec: SpringSpec<Float> = DefaultAnimationSpec
) : BaseMotionController<InteractionTarget, BackStackModel.State<InteractionTarget>, BackstackFader.UiState>(
    scope = uiContext.coroutineScope,
    defaultAnimationSpec = defaultAnimationSpec,
) {
    override fun defaultUiState(): UiState = UiState()

    class UiState(
        var alpha: Alpha = Alpha(1f),
    ) : BaseUiState<UiState>(
        listOf(alpha.isAnimating)
    ) {

        override fun isVisible() =
            alpha.value > 0.0f

        override val modifier: Modifier
            get() = Modifier
                .then(alpha.modifier)

        override suspend fun snapTo(scope: CoroutineScope, uiState: UiState) {
            scope.launch {
                alpha.snapTo(uiState.alpha.value)
                updateVisibilityState()
            }
        }

        override suspend fun animateTo(
            scope: CoroutineScope,
            uiState: UiState,
            springSpec: SpringSpec<Float>,
        ) {
            scope.launch {
                alpha.animateTo(uiState.alpha.value, springSpec) {
                    updateVisibilityState()
                }
            }
        }

        override fun lerpTo(scope: CoroutineScope, start: UiState, end: UiState, fraction: Float) {
            scope.launch {
                alpha.lerpTo(start.alpha, end.alpha, fraction)
                updateVisibilityState()
            }
        }
    }

    private val visible = UiState(
        alpha = Alpha(1f)
    )

    private val hidden = UiState(
        alpha = Alpha(0f)
    )

    override fun BackStackModel.State<InteractionTarget>.toUiState(): List<MatchedUiState<InteractionTarget, UiState>> =
        listOf(
            MatchedUiState(active, visible)
        ) + (created + stashed + destroyed).map {
            MatchedUiState(it, hidden)
        }
}
