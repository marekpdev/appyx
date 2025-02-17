package com.bumble.appyx.interactions.core.ui.property.impl

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import com.bumble.appyx.interactions.core.ui.math.lerpFloat
import com.bumble.appyx.interactions.core.ui.property.Interpolatable
import com.bumble.appyx.interactions.core.ui.property.MotionProperty
import com.bumble.appyx.mapState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Alpha(
    coroutineScope: CoroutineScope,
    target: Target,
    visibilityThreshold: Float = 0.01f,
    displacement: StateFlow<Float> = MutableStateFlow(0f),
) : MotionProperty<Float, AnimationVector1D>(
    coroutineScope = coroutineScope,
    animatable = Animatable(target.value),
    easing = target.easing,
    visibilityThreshold = visibilityThreshold,
    displacement = displacement
), Interpolatable<Alpha.Target> {

    class Target(
        val value: Float,
        val easing: Easing? = null,
    ) : MotionProperty.Target

    override val isVisibleFlow: StateFlow<Boolean> =
        renderValueFlow.mapState(coroutineScope) {
            it > 0f
        }

    override fun calculateRenderValue(base: Float, displacement: Float): Float =
        base - displacement

    override val modifier: Modifier
        get() = Modifier.composed {
            this.alpha(renderValueFlow.collectAsState().value)
        }

    override suspend fun lerpTo(start: Target, end: Target, fraction: Float) {
        snapTo(
            lerpFloat(
                start = start.value,
                end = end.value,
                progress = easingTransform(end.easing, fraction)
            )
        )
    }
}
