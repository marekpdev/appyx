package com.bumble.appyx.navigation.node.cakes.component.spotlighthero

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import com.bumble.appyx.interactions.core.model.BaseAppyxComponent
import com.bumble.appyx.interactions.core.ui.Visualisation
import com.bumble.appyx.interactions.core.ui.context.TransitionBounds
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.gesture.GestureFactory
import com.bumble.appyx.interactions.core.ui.gesture.GestureSettleConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

open class SpotlightHero<InteractionTarget : Any>(
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    model: SpotlightHeroModel<InteractionTarget>,
    visualisation: (UiContext) -> Visualisation<InteractionTarget, SpotlightHeroModel.State<InteractionTarget>>,
    gestureFactory: (TransitionBounds) -> GestureFactory<InteractionTarget, SpotlightHeroModel.State<InteractionTarget>> = {
        GestureFactory.Noop()
    },
    animationSpec: AnimationSpec<Float> = spring(),
    gestureSettleConfig: GestureSettleConfig = GestureSettleConfig(
        completionThreshold = 0.2f,
        completeGestureSpec = animationSpec,
        revertGestureSpec = animationSpec,
    ),
    disableAnimations: Boolean = false,
    isDebug: Boolean = false
) : BaseAppyxComponent<InteractionTarget, SpotlightHeroModel.State<InteractionTarget>>(
    scope = scope,
    model = model,
    visualisation = visualisation,
    gestureFactory = gestureFactory,
    defaultAnimationSpec = animationSpec,
    gestureSettleConfig = gestureSettleConfig,
    disableAnimations = disableAnimations,
    isDebug = isDebug
) {
    val activeIndex: Flow<Float> = model.output
        .map { it.currentTargetState.activeIndex }
        .distinctUntilChanged()

    val mode: Flow<SpotlightHeroModel.Mode> = model.output
        .map { it.currentTargetState.mode }
        .distinctUntilChanged()

}
