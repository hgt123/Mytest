# 语音多语言映射架构设计

## 目标

在工程配置仍然使用**中文 text** 的情况下，通过**当前 text + 语音类型 voiceType**，
快速读取目标语言的语音文案，并提供明确的回退策略与可维护的数据结构。

## 核心思路

1. **中文原文作为主键**：运行时直接用中文 text 进行查找，避免改动配置格式。
2. **语音类型作为第二维度**：同一句话允许不同音色/风格文案（如 male/female）。
3. **Locale 精确匹配 + 语言回退**：先匹配 `en-US`，再回退到 `en`。
4. **可选别名**：通过 `aliases` 解决同义/标点差异导致的查找失败。

## 数据结构（JSON）

```json
{
  "version": 1,
  "defaultLocale": "zh-CN",
  "aliases": {
    "请打开空调": "打开空调",
    "打开空调。": "打开空调"
  },
  "texts": {
    "打开空调": {
      "default": {
        "zh-CN": "打开空调",
        "en-US": "Turn on the air conditioner."
      },
      "female": {
        "en-US": "Please turn on the air conditioner."
      }
    }
  }
}
```

### 字段说明

- `version`：格式版本，便于灰度升级。
- `defaultLocale`：兜底语言（通常为 `zh-CN`）。
- `aliases`：可选别名映射，用于解决中文标点/同义词差异。
- `texts`：
  - 第一层 key：中文原文（规范化后的 text）。
  - 第二层 key：`voiceType`（如 `default/male/female/child`）。
  - 第三层 key：`Locale`（如 `en-US/ja-JP/en`）。
  - value：最终语音文本。

## 规范化与别名

为避免空格/标点差异导致查找失败，建议做轻量规范化：

1. `trim()` 去首尾空白；
2. 连续空白折叠为单空格；
3. 业务上需要时再增加 `aliases` 兜底。

不建议在运行时做复杂的语义归一化，优先用 `aliases` 显式维护。

## 查询回退策略

建议的优先级如下：

1. 精确 `text + voiceType + localeTag`（如 `en-US`）
2. `text + voiceType + language`（如 `en`）
3. `text + default voiceType + localeTag`
4. `text + default voiceType + language`
5. `text + default voiceType + defaultLocale`
6. 原始中文 `text`（最终兜底）

## 动态参数与占位符

如需动态变量，建议使用 `{name}` 风格占位符，并保持各语言占位符集合一致。
运行时先完成映射，再由业务侧进行参数替换。

## 落地方式（Android）

1. 将 JSON 放入 `assets/` 或 `res/raw/`。
2. 启动时加载并缓存，避免频繁 IO。
3. 用 `SpeechTextRepository.get(text, voiceType, locale)` 读取。

`SpeechTextRepository.kt` 给出参考实现。

## 维护流程建议

- 文案团队维护表格（含 `cnText/voiceType/locale/text`）。
- 构建脚本导出 JSON（可加校验：重复 key、缺失 voiceType）。
- 版本升级时保留 `aliases` 兼容旧文本。
