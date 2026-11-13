package io.github.neoruaa.glasmio.glass

import org.junit.Assert.assertEquals
import org.junit.Test

class GlasmioGlassTuningTest {
    @Test
    fun defaultsMatchTheDocumentedFrostedMaterial() {
        assertEquals(5f, GlasmioGlassTuning.DEFAULT.blurRadiusDp)
        assertEquals(12f, GlasmioGlassTuning.DEFAULT.refractionHeightDp)
        assertEquals(13.5f, GlasmioGlassTuning.DEFAULT.refractionAmountDp)
        assertEquals(0.12f, GlasmioGlassTuning.DEFAULT.tintStrength)
        assertEquals(1.25f, GlasmioGlassTuning.DEFAULT.surfaceAlphaMultiplier)
    }

    @Test
    fun valuesAreClampedAndNonFiniteValuesUseDefaults() {
        val safe = GlasmioGlassTuning(
            blurRadiusDp = Float.NaN,
            refractionHeightDp = 50f,
            refractionAmountDp = -2f,
            tintStrength = Float.POSITIVE_INFINITY,
            surfaceAlphaMultiplier = 3f,
        ).clamped()

        assertEquals(5f, safe.blurRadiusDp)
        assertEquals(32f, safe.refractionHeightDp)
        assertEquals(0f, safe.refractionAmountDp)
        assertEquals(0.12f, safe.tintStrength)
        assertEquals(1.6f, safe.surfaceAlphaMultiplier)
    }
}
