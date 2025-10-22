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
- 点击联系人可跳转到好友详情页
- 支持跳转到添加联系人页面

### AddFriendsPage（添加联系人页面）
- 顶部信息栏：居中显示"添加联系人"标题，左侧返回按钮
- 用户信息展示：显示当前用户头像（刘承龙）和账号类型（免费版）
- 邀请链接：自动生成的会议邀请链接文本
- 复制链接功能：一键复制邀请链接到剪贴板
- 分享功能：支持分享到微信（模拟功能）
- 联系人限制提示：显示最多可添加100位联系人的提示

### FriendsDetailsPage（好友详情页面）
- 上半部分（灰蓝色背景）：
  - 左上角返回按钮
  - 大头像显示好友姓名首字母
  - 好友用户名
- 下半部分（白色背景）：
  - 来源信息："通过会议添加"
  - 联系方式：显示好友电话（如果有）
  - 邮箱：显示好友邮箱（如果有）
  - 呼叫按钮：支持一键呼叫好友（模拟功能）

### JoinMeetingPage（加入会议页面）
- 顶部信息栏：居中显示"加入会议"标题，左侧返回按钮
- 会议信息区：
  - 会议号输入框（带下拉箭头）
  - 显示用户姓名（刘承龙）
  - "当前设备始终使用此名称入会"复选框
- 设备设置区：
  - 开启麦克风开关（默认关闭）
  - 开启扬声器开关（默认开启）
  - 开启视频开关（默认关闭）
- 底部加入按钮：
  - 会议号为空时：灰蓝色且禁用
  - 会议号有内容时：蓝色且可点击
- 支持加入会议功能（模拟）

### QuickMeetingPage（快速会议页面）
- 顶部信息栏：居中显示"快速会议"标题，左侧退出按钮
- 中间功能区：
  - 入会姓名：显示用户姓名（刘承龙）
  - 麦克风是否开启开关（默认开启）
  - 摄像头是否开启开关（默认关闭）
  - 扬声器是否开启开关（默认开启）
- 底部开始会议按钮：蓝色且始终可点击
- 支持启动快速会议功能（模拟）

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
│   ├── ScheduledMeetingContract.kt
│   ├── AddFriendsContract.kt
│   ├── FriendsDetailsContract.kt
│   ├── JoinMeetingContract.kt
│   └── QuickMeetingContract.kt
├── presenter/                     # 业务逻辑层
│   ├── HomePresenter.kt
│   ├── MePresenter.kt
│   ├── ContactPresenter.kt
│   ├── ScheduledMeetingPresenter.kt
│   ├── AddFriendsPresenter.kt
│   ├── FriendsDetailsPresenter.kt
│   ├── JoinMeetingPresenter.kt
│   └── QuickMeetingPresenter.kt
├── view/                          # UI层
│   ├── HomePage.kt
│   ├── MePage.kt
│   ├── ContactPage.kt
│   ├── ScheduledMeetingPage.kt
│   ├── AddFriendsPage.kt
│   ├── FriendsDetailsPage.kt
│   ├── JoinMeetingPage.kt
│   └── QuickMeetingPage.kt
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
- [x] AddFriendsPage UI实现（添加联系人页面）
- [x] 页面导航功能（ContactPage -> AddFriendsPage）
- [x] 复制链接到剪贴板功能
- [x] FriendsDetailsPage UI实现（好友详情页面）
- [x] 页面导航功能（ContactPage -> FriendsDetailsPage）
- [x] 好友详情信息展示和呼叫功能
- [x] FriendsDetailsPage UI微调（简化界面，移除三点菜单、统一身份标识和免费版标签）
- [x] JoinMeetingPage UI实现（加入会议页面）
- [x] 页面导航功能（HomePage -> JoinMeetingPage）
- [x] 会议号输入、设备设置和动态按钮状态
- [x] JoinMeetingPage UI微调（入会姓名改为"刘承龙"）
- [x] QuickMeetingPage UI实现（快速会议页面）
- [x] 页面导航功能（HomePage -> QuickMeetingPage）
- [x] 快速会议设备设置和启动会议功能
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
