# Android 语音多语言映射设计

本仓库提供一种**基于中文原文 text + 语音类型 voiceType** 的多语言映射架构设计。
目标是：在工程配置仍然使用中文文本的情况下，运行时可以快速读取已翻译好的目标语言语音文本。

## 内容说明

- `speech_i18n_mapping.md`：完整架构设计与数据结构说明
- `speech_i18n_sample.json`：可直接落地的映射文件示例
- `SpeechTextRepository.kt`：运行时查询的 Kotlin 示例实现

## 快速使用（示意）

1. 将 `speech_i18n_sample.json` 放入 `assets/` 或 `res/raw/`。
2. 在代码中加载并查询：

```kotlin
val repo = SpeechTextRepository.loadFromAssets(context, "speech_i18n_sample.json")
val text = repo.get(
    text = "打开空调",
    voiceType = VoiceType.FEMALE,
    locale = Locale("en", "US"),
)
// text == "Please turn on the air conditioner."
```

更多细节请阅读 `speech_i18n_mapping.md`。
