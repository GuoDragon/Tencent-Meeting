# Tencent-Meeting
腾讯会议模拟

## 项目介绍
这是一个腾讯会议App的模拟版本，采用Android Jetpack Compose技术栈开发。项目遵循MVP架构模式，提供了腾讯会议的核心功能界面。

## 已实现功能
### HomePage（会议首页）
- 功能按钮区域：
  - 加入会议
  - 快速会议
  - 预定会议
- 会议列表：显示进行中和待开始的会议
- 底部导航栏：会议、通讯录、我的三个Tab
- 支持跳转到预定会议页面

### ScheduledMeetingPage（预定会议页面）
- 顶部信息栏：显示居中对齐的"预定会议"标题，左侧取消按钮，右侧完成按钮
- 会议主题：显示默认会议主题（刘承龙预定的会议）
- 开始时间：支持选择会议开始的日期和时间
- 会议时长：支持选择会议时长（15、30、45、60、90、120分钟）
- 重复频率：支持选择会议重复模式（不重复、每天、每周、每月）
- 参会人：支持从通讯录中选择多个参会人员
- 入会密码：支持设置6位数字入会密码
- 数据验证：验证会议时间、密码格式等
- 会议保存：将预定的会议保存到内存中

### MePage（我的页面）
- 用户信息卡片：显示头像、昵称、手机号、邮箱
- 功能选项：个人信息、会议设置、关于
- 历史会议列表：显示已结束的会议记录

### ContactPage（通讯录页面）
- 顶部标题栏：显示"通讯录"标题和添加好友按钮
- 搜索功能：支持通过姓名和手机号搜索联系人
- 联系人列表：显示所有联系人的头像和姓名
- 邀请功能：支持邀请联系人参加会议

## 技术架构
- **架构模式**: MVP (Model-View-Presenter)
- **UI框架**: Jetpack Compose + Material 3
- **数据存储**: JSON文件存储在assets/data目录
- **数据解析**: Gson
- **状态管理**: Compose State
- **响应式设计**: 动态计算屏幕尺寸，适配不同设备

## 项目结构
```
app/src/main/java/com/example/tencentmeeting/
├── MainActivity.kt                 # 主Activity，包含底部导航
├── model/                         # 数据模型
│   ├── User.kt
│   ├── Meeting.kt
│   ├── MeetingParticipant.kt
│   ├── Message.kt
│   └── ...
├── data/                          # 数据层
│   └── DataRepository.kt
├── contract/                      # MVP接口定义
│   ├── HomeContract.kt
│   ├── MeContract.kt
│   ├── ContactContract.kt
│   └── ScheduledMeetingContract.kt
├── presenter/                     # 业务逻辑层
│   ├── HomePresenter.kt
│   ├── MePresenter.kt
│   ├── ContactPresenter.kt
│   └── ScheduledMeetingPresenter.kt
├── view/                          # UI层
│   ├── HomePage.kt
│   ├── MePage.kt
│   ├── ContactPage.kt
│   └── ScheduledMeetingPage.kt
└── ui/theme/                      # 主题样式
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

## 数据文件
项目使用JSON文件模拟数据，存储在`app/src/main/assets/data/`目录：
- `users.json` - 用户信息
- `meetings.json` - 会议信息
- `meeting_participants.json` - 参会人员状态
- `messages.json` - 聊天消息
- `hand_raise_records.json` - 举手记录

## 运行说明
1. 使用Android Studio打开项目
2. 等待Gradle同步完成
3. 连接Android设备或启动模拟器
4. 点击运行按钮

## 开发进度
- [x] 项目架构搭建
- [x] 数据模型定义
- [x] HomePage UI实现（会议功能）
- [x] MePage UI实现（用户信息和历史会议）
- [x] ContactPage UI实现（通讯录功能）
- [x] 底部导航栏
- [x] MVP架构完善
- [x] 用户信息微调（更新为刘承龙）
- [x] ScheduledMeetingPage UI实现（预定会议页面）
- [x] 预定会议功能实现（开始时间、会议时长、重复频率、参会人、入会密码）
- [x] 页面导航功能（HomePage -> ScheduledMeetingPage）
- [x] 会议数据保存到内存
- [x] ScheduledMeetingPage UI微调（标题居中对齐，会议主题改为"刘承龙预定的会议"）
- [ ] 其他功能页面跳转（加入会议、快速会议）
- [ ] 页面间数据传递优化

## 界面设计说明
- **会议页面**：专注于会议功能，显示进行中和待开始的会议
- **通讯录页面**：提供联系人管理，支持搜索和邀请功能
- **我的页面**：展示用户信息和历史会议记录
- **职责分离**：不同类型的信息分布在对应的页面中，提升用户体验
- **界面布局**：主要内容区域向下偏移屏幕高度的10%，为状态栏和系统UI预留空间

## 注意事项
- 项目仅供学习和演示使用
- 所有数据为本地模拟数据，不涉及真实网络请求
- UI设计参考腾讯会议App，但不完全相同
- 页面布局经过优化，符合移动端使用习惯
