package io.github.neoruaa.glasmio.sample

import android.content.Context
import io.github.neoruaa.glasmio.glass.GlasmioGlassTuning

/** Host settings survive a fresh process; component examples keep their own local state. */
internal data class DemoPreferences(
    val dark: Boolean,
    val reduceTransparency: Boolean = false,
    val hdrEffects: Boolean = false,
    val fpsCounter: Boolean = false,
    val rtl: Boolean = false,
    val separateNavigationItem: Boolean = true,
    val glassTuning: GlasmioGlassTuning = GlasmioGlassTuning.DEFAULT,
) {
    val hdrOutput: Boolean get() = hdrEffects && !reduceTransparency

    fun save(context: Context) {
        // Slider state is saved only after its drag finishes; keep this small write atomic.
        context.getSharedPreferences(StoreName, Context.MODE_PRIVATE).edit()
            .putBoolean("dark", dark)
            .putBoolean("reduceTransparency", reduceTransparency)
            .putBoolean("hdrEffects", hdrEffects)
            .putBoolean("fpsCounter", fpsCounter)
            .putBoolean("rtl", rtl)
            .putBoolean("separateNavigationItem", separateNavigationItem)
            .putFloat("glassBlurRadiusDp", glassTuning.blurRadiusDp)
            .putFloat("glassRefractionHeightDp", glassTuning.refractionHeightDp)
            .putFloat("glassRefractionAmountDp", glassTuning.refractionAmountDp)
            .putFloat("glassTintStrength", glassTuning.tintStrength)
            .putFloat("glassSurfaceAlphaMultiplier", glassTuning.surfaceAlphaMultiplier)
            .commit()
    }

    companion object {
        private const val StoreName = "demo-settings"

        fun load(context: Context, systemDark: Boolean): DemoPreferences {
            val values = context.getSharedPreferences(StoreName, Context.MODE_PRIVATE)
            val tuning = GlasmioGlassTuning(
                blurRadiusDp = values.getFloat("glassBlurRadiusDp", GlasmioGlassTuning.DEFAULT_BLUR_RADIUS_DP),
                refractionHeightDp = values.getFloat("glassRefractionHeightDp", GlasmioGlassTuning.DEFAULT_REFRACTION_HEIGHT_DP),
                refractionAmountDp = values.getFloat("glassRefractionAmountDp", GlasmioGlassTuning.DEFAULT_REFRACTION_AMOUNT_DP),
                tintStrength = values.getFloat("glassTintStrength", GlasmioGlassTuning.DEFAULT_TINT_STRENGTH),
                surfaceAlphaMultiplier = values.getFloat("glassSurfaceAlphaMultiplier", GlasmioGlassTuning.DEFAULT_SURFACE_ALPHA_MULTIPLIER),
            ).clamped()
            return DemoPreferences(
                dark = values.getBoolean("dark", systemDark),
                reduceTransparency = values.getBoolean("reduceTransparency", false),
                hdrEffects = values.getBoolean("hdrEffects", false),
                fpsCounter = values.getBoolean("fpsCounter", false),
                rtl = values.getBoolean("rtl", false),
                separateNavigationItem = values.getBoolean("separateNavigationItem", true),
                glassTuning = tuning,
            )
        }
    }
}
