# UI Components

这个目录包含项目中可复用的公共UI组件。

## 组件列表

### LoadingIndicator
**用途**: 显示加载中状态
**使用场景**: 数据加载时显示

```kotlin
LoadingIndicator()
```

### ErrorView
**用途**: 显示错误信息
**参数**:
- `message`: 错误消息
- `onRetry`: 重试回调函数（可选）

```kotlin
ErrorView(
    message = "加载失败",
    onRetry = { presenter.reload() }
)
```

### EmptyView
**用途**: 显示空状态
**参数**:
- `message`: 空状态提示文本（默认："暂无数据"）

```kotlin
EmptyView(message = "暂无会议记录")
```

### CustomCard
**用途**: 统一样式的卡片容器
**参数**:
- `backgroundColor`: 背景颜色（默认：白色）
- `content`: 卡片内容

```kotlin
CustomCard {
    Text("Card content")
}
```

### CustomButton
**用途**: 统一样式的按钮
**参数**:
- `text`: 按钮文字
- `onClick`: 点击事件
- `enabled`: 是否启用（默认：true）

```kotlin
CustomButton(
    text = "确认",
    onClick = { doSomething() }
)
```

## 使用原则

1. **可复用性**: 这些组件设计为通用组件，可在多个Screen中复用
2. **无状态**: 组件本身不保存状态，所有数据通过参数传递
3. **一致性**: 使用这些组件确保UI风格一致
4. **业务无关**: 这些组件不包含业务逻辑

## 添加新组件

如果需要添加新的公共组件：

1. 确认该组件确实需要在多个地方复用
2. 组件应该是无状态的
3. 组件不应包含业务逻辑
4. 在此README中添加组件说明
