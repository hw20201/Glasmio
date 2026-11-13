package io.github.neoruaa.glasmio.glass

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.github.neoruaa.glasmio.core.LocalGlassColors
import io.github.neoruaa.glasmio.core.LocalGlassDimensions
import com.kyant.capsule.ContinuousRoundedRectangle
import io.github.neoruaa.glasmio.glass.render.Backdrop

/**
 * Reusable, app-owned controls for the main frosted-glass material.
 * Values are stored in dp where applicable and clamped before rendering.
 */
@Immutable
data class GlasmioGlassTuning(
    val blurRadiusDp: Float = DEFAULT_BLUR_RADIUS_DP,
    val refractionHeightDp: Float = DEFAULT_REFRACTION_HEIGHT_DP,
    val refractionAmountDp: Float = DEFAULT_REFRACTION_AMOUNT_DP,
    val tintStrength: Float = DEFAULT_TINT_STRENGTH,
    val surfaceAlphaMultiplier: Float = DEFAULT_SURFACE_ALPHA_MULTIPLIER,
) {
    fun clamped(): GlasmioGlassTuning = copy(
        blurRadiusDp = blurRadiusDp.safeIn(MIN_BLUR_RADIUS_DP, MAX_BLUR_RADIUS_DP, DEFAULT_BLUR_RADIUS_DP),
        refractionHeightDp = refractionHeightDp.safeIn(MIN_REFRACTION_HEIGHT_DP, MAX_REFRACTION_HEIGHT_DP, DEFAULT_REFRACTION_HEIGHT_DP),
        refractionAmountDp = refractionAmountDp.safeIn(MIN_REFRACTION_AMOUNT_DP, MAX_REFRACTION_AMOUNT_DP, DEFAULT_REFRACTION_AMOUNT_DP),
        tintStrength = tintStrength.safeIn(MIN_TINT_STRENGTH, MAX_TINT_STRENGTH, DEFAULT_TINT_STRENGTH),
        surfaceAlphaMultiplier = surfaceAlphaMultiplier.safeIn(MIN_SURFACE_ALPHA_MULTIPLIER, MAX_SURFACE_ALPHA_MULTIPLIER, DEFAULT_SURFACE_ALPHA_MULTIPLIER),
    )

    companion object {
        const val MIN_BLUR_RADIUS_DP = 0.75f
        const val MAX_BLUR_RADIUS_DP = 16f
        const val DEFAULT_BLUR_RADIUS_DP = 5f
        const val MIN_REFRACTION_HEIGHT_DP = 0f
        const val MAX_REFRACTION_HEIGHT_DP = 32f
        const val DEFAULT_REFRACTION_HEIGHT_DP = 12f
        const val MIN_REFRACTION_AMOUNT_DP = 0f
        const val MAX_REFRACTION_AMOUNT_DP = 32f
        const val DEFAULT_REFRACTION_AMOUNT_DP = 13.5f
        const val MIN_TINT_STRENGTH = 0f
        const val MAX_TINT_STRENGTH = 0.5f
        const val DEFAULT_TINT_STRENGTH = 0.12f
        const val MIN_SURFACE_ALPHA_MULTIPLIER = 0.4f
        const val MAX_SURFACE_ALPHA_MULTIPLIER = 1.6f
        const val DEFAULT_SURFACE_ALPHA_MULTIPLIER = 1.25f

        val DEFAULT = GlasmioGlassTuning()
    }
}

/** Apply one [GlasmioGlassTuning] value to a standard Glasmio surface. */
@Composable
fun GlasmioTunedGlassSurface(
    tuning: GlasmioGlassTuning,
    modifier: Modifier = Modifier,
    backdrop: Backdrop = LocalGlassBackdrop.current,
    shape: Shape = ContinuousRoundedRectangle(LocalGlassDimensions.current.regularCornerRadius),
    tint: Color = LocalGlassColors.current.accent,
    material: NavigationGlassMaterial? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val safe = tuning.clamped()
    GlassSurface(
        modifier = modifier,
        backdrop = backdrop,
        shape = shape,
        navigationSurfaceColor = tint.copy(alpha = tint.alpha * safe.tintStrength),
        navigationSurfaceAlphaMultiplier = safe.surfaceAlphaMultiplier,
        refractionHeight = safe.refractionHeightDp.dp,
        refractionAmount = safe.refractionAmountDp.dp,
        frostRadius = safe.blurRadiusDp.dp,
        material = material,
        content = content,
    )
}

private fun Float.safeIn(minimum: Float, maximum: Float, fallback: Float): Float =
    (if (isFinite()) this else fallback).coerceIn(minimum, maximum)
