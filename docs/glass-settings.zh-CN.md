# Glasmio 毛玻璃设置

Glasmio 提供一套可复用的毛玻璃参数模型和设置面板。应用仍负责保存参数；同一个 `GlasmioGlassTuning` 可以同时驱动预览、页面卡片、弹层和导航栏表面。

## 参数与范围

| 参数 | 默认值 | 范围 | 作用 |
| --- | ---: | ---: | --- |
| `blurRadiusDp` | 5 dp | 0.75–16 dp | 背景雾化半径 |
| `refractionHeightDp` | 12 dp | 0–32 dp | 折射纵向高度 |
| `refractionAmountDp` | 13.5 dp | 0–32 dp | 折射强度 |
| `tintStrength` | 0.12 | 0–0.5 | 强调色覆盖强度 |
| `surfaceAlphaMultiplier` | 1.25 | 0.4–1.6 | 表面颜色透明度倍率 |

每次绘制前应调用 `clamped()`；它会限制越界数值，并把 NaN 或无穷值替换为默认值。

## 接入方式

依赖 `glasmio-glass` 和 `glasmio-preference`，并在应用中通过 `GlasmioBackdropHost` 提供采样背景：

```kotlin
var tuning by remember { mutableStateOf(GlasmioGlassTuning.DEFAULT) }

Column {
    GlasmioTunedGlassSurface(
        tuning = tuning,
        modifier = Modifier.fillMaxWidth().height(88.dp),
        shape = ContinuousRoundedRectangle(24.dp),
    ) {
        Text("实时预览")
    }

    GlasmioGlassTuningPreferences(
        tuning = tuning,
        onTuningChange = { tuning = it },
        onTuningChangeFinished = { value -> saveGlassTuning(value.clamped()) },
    )
}
```

`onTuningChange` 会在拖动过程中持续回调，适合更新本地预览；将 DataStore 或 SharedPreferences 写入放在 `onTuningChangeFinished`，只在用户松手或通过无障碍设置完成后保存，避免每帧同步写盘。

## 简体中文

`glasmio-preference` 为毛玻璃面板提供英文默认资源和 `values-zh-rCN` 中文资源。应用切换到简体中文系统语言时，标题、参数说明和单位会自动本地化。其他业务标题仍由宿主应用自己的资源管理。

该 API 要求 Android 13（API 33）或更高版本，与 Glasmio 其余玻璃渲染 API 一致。低版本设备应由宿主保留自己的兼容表面。
