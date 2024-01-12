package com.bumble.appyx.navigation.node

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.navigation.modality.NodeContext

open class ComposableNode(
    nodeContext: NodeContext,
    private val composable: @Composable (Modifier) -> Unit
) : Node(
    nodeContext = nodeContext,
) {

    @Composable
    override fun View(modifier: Modifier) {
        composable(modifier)
    }
}

fun node(nodeContext: NodeContext, composable: @Composable (Modifier) -> Unit): Node =
    ComposableNode(nodeContext, composable)
