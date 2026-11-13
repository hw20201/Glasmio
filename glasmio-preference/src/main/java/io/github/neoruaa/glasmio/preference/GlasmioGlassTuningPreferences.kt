package io.github.neoruaa.glasmio.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.neoruaa.glasmio.glass.GlasmioGlassTuning
import io.github.neoruaa.glasmio.glass.LocalGlassBackdrop
import io.github.neoruaa.glasmio.glass.render.Backdrop
import io.github.neoruaa.glasmio.ui.GlasmioSlider
import java.util.Locale

/**
 * A reusable set of localized controls for [GlasmioGlassTuning]. The caller owns persistence
 * and can pass the returned value to [io.github.neoruaa.glasmio.glass.GlasmioTunedGlassSurface].
 * Persist from [onTuningChangeFinished] so continuous drag updates do not block the UI thread.
 */
@Composable
fun GlasmioGlassTuningPreferences(
    tuning: GlasmioGlassTuning,
    onTuningChange: (GlasmioGlassTuning) -> Unit,
    modifier: Modifier = Modifier,
    backdrop: Backdrop = LocalGlassBackdrop.current,
    onTuningChangeFinished: ((GlasmioGlassTuning) -> Unit)? = null,
) {
    val safe = tuning.clamped()
    val latest = remember { mutableStateOf(safe) }
    LaunchedEffect(safe) { latest.value = safe }
    fun update(transform: (GlasmioGlassTuning) -> GlasmioGlassTuning) {
        val next = transform(latest.value).clamped()
        latest.value = next
        onTuningChange(next)
    }
    val finish = { onTuningChangeFinished?.invoke(latest.value) }

    Column(modifier = modifier.fillMaxWidth()) {
        GlasmioPreferenceGroupTitle(stringResource(R.string.glasmio_glass_tuning_title))
        TuningSlider(
            title = stringResource(R.string.glasmio_glass_blur_title),
            description = stringResource(R.string.glasmio_glass_blur_description),
            value = safe.blurRadiusDp,
            valueRange = GlasmioGlassTuning.MIN_BLUR_RADIUS_DP..GlasmioGlassTuning.MAX_BLUR_RADIUS_DP,
            valueLabel = dpLabel(safe.blurRadiusDp),
            backdrop = backdrop,
            showTopSeparator = false,
            onValueChange = { value -> update { current -> current.copy(blurRadiusDp = value) } },
            onValueChangeFinished = finish,
        )
        TuningSlider(
            title = stringResource(R.string.glasmio_glass_refraction_height_title),
            description = stringResource(R.string.glasmio_glass_refraction_height_description),
            value = safe.refractionHeightDp,
            valueRange = GlasmioGlassTuning.MIN_REFRACTION_HEIGHT_DP..GlasmioGlassTuning.MAX_REFRACTION_HEIGHT_DP,
            valueLabel = dpLabel(safe.refractionHeightDp),
            backdrop = backdrop,
            onValueChange = { value -> update { current -> current.copy(refractionHeightDp = value) } },
            onValueChangeFinished = finish,
        )
        TuningSlider(
            title = stringResource(R.string.glasmio_glass_refraction_amount_title),
            description = stringResource(R.string.glasmio_glass_refraction_amount_description),
            value = safe.refractionAmountDp,
            valueRange = GlasmioGlassTuning.MIN_REFRACTION_AMOUNT_DP..GlasmioGlassTuning.MAX_REFRACTION_AMOUNT_DP,
            valueLabel = dpLabel(safe.refractionAmountDp),
            backdrop = backdrop,
            onValueChange = { value -> update { current -> current.copy(refractionAmountDp = value) } },
            onValueChangeFinished = finish,
        )
        TuningSlider(
            title = stringResource(R.string.glasmio_glass_tint_title),
            description = stringResource(R.string.glasmio_glass_tint_description),
            value = safe.tintStrength,
            valueRange = GlasmioGlassTuning.MIN_TINT_STRENGTH..GlasmioGlassTuning.MAX_TINT_STRENGTH,
            valueLabel = stringResource(R.string.glasmio_glass_percent_value, (safe.tintStrength * 100f).toInt()),
            backdrop = backdrop,
            onValueChange = { value -> update { current -> current.copy(tintStrength = value) } },
            onValueChangeFinished = finish,
        )
        TuningSlider(
            title = stringResource(R.string.glasmio_glass_surface_alpha_title),
            description = stringResource(R.string.glasmio_glass_surface_alpha_description),
            value = safe.surfaceAlphaMultiplier,
            valueRange = GlasmioGlassTuning.MIN_SURFACE_ALPHA_MULTIPLIER..GlasmioGlassTuning.MAX_SURFACE_ALPHA_MULTIPLIER,
            valueLabel = stringResource(R.string.glasmio_glass_percent_value, (safe.surfaceAlphaMultiplier * 100f).toInt()),
            backdrop = backdrop,
            onValueChange = { value -> update { current -> current.copy(surfaceAlphaMultiplier = value) } },
            onValueChangeFinished = finish,
        )
    }
}

@Composable
private fun TuningSlider(
    title: String,
    description: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    valueLabel: String,
    backdrop: Backdrop,
    showTopSeparator: Boolean = true,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
) {
    GlasmioPreferenceEntry(
        title = { Text(title) },
        description = description,
        content = {
            GlasmioSlider(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                valueRange = valueRange,
                backdrop = backdrop,
                onValueChangeFinished = onValueChangeFinished,
            )
        },
        trailingContent = { Text(valueLabel) },
        showTopSeparator = showTopSeparator,
    )
}

@Composable
private fun dpLabel(value: Float): String = stringResource(
    R.string.glasmio_glass_dp_value,
    String.format(Locale.ROOT, "%.1f", value),
)
