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
- 会议录制：支持开启/关闭会议录制功能（默认关闭）
- 数据验证：验证会议时间、密码格式等
- 会议保存：将预定的会议保存到内存中

### MePage（我的页面）
- 用户信息卡片：显示头像、昵称、手机号、邮箱
- 历史会议列表：显示已结束的会议记录
  - 显示会议主题和会议号
  - 显示会议时长（自动计算小时和分钟）
  - 支持点击查看会议回放

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
  - 入会密码输入框（选填，装饰功能）
  - "当前设备始终使用此名称入会"复选框
- 设备设置区：
  - 开启麦克风开关（默认关闭）
  - 开启扬声器开关（默认开启）
  - 开启视频开关（默认关闭）
  - 会议录制开关（默认关闭）
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
  - 会议录制开关（默认关闭）
- 底部开始会议按钮：蓝色且始终可点击
- 支持启动快速会议功能（模拟）

### MeetingDetailsPage（会议详情页面）
- 顶部信息栏：
  - 黑色背景（#1F2227）延伸至屏幕顶部边缘
  - 顶部padding 40dp适配系统状态栏
  - 左侧：扬声器按钮和录制按钮
    - 扬声器：切换声音开关（白色图标）
    - 录制：切换会议录制状态（录制中为红色圆点，未录制为灰色圆环）
  - 中间：会议标题和会议时长计时（居中显示）
  - 右侧：红色"结束"按钮
- 中间参会人区域：
  - 显示参会人圆形头像（显示姓名首字母）
  - 显示参会人姓名（刘承龙）
  - 麦克风静音时显示红色静音图标
  - 暂不实现开启摄像头的视频效果
- 左下角弹幕输入区和举手功能：
  - 距底部120dp，位置优化
  - 左侧：聊天图标、表情图标和"说点什么..."提示文字（点击进入聊天页面）
  - 右侧：举手按钮
    - 未举手时显示灰色手掌图标
    - 举手后显示橙色手掌图标
    - 点击切换举手状态
- 主持人视角举手提示：
  - 当有人举手时，在输入区上方显示弹幕提示卡片
  - 显示举手者姓名和橙色手掌图标
  - 提供"解除静音"按钮，主持人可一键解除该用户的静音状态
  - 点击解除静音后，提示卡片自动消失
- 底部功能区（4个按钮）：
  - 解除静音/静音：切换麦克风状态
  - 开启视频/关闭视频：切换摄像头状态
  - 共享屏幕：绿色图标，点击切换屏幕共享状态
  - 管理成员(1)：点击显示成员管理页面
- 屏幕共享功能：
  - 点击"共享屏幕"按钮后，中间区域从参会人视图切换为屏幕共享视图
  - 显示模拟的手机桌面屏幕（蓝色渐变壁纸）
  - 包含顶部状态栏（时间、日期）和应用图标
  - 再次点击"共享屏幕"可返回参会人视图
  - 顶部栏和底部功能栏保持不变
- 支持从快速会议、加入会议、预定会议进入

### MembersManagePage（成员管理页面）
- 弹窗样式：
  - 白色圆角背景，覆盖在会议页面上方
  - 半透明黑色背景遮罩
- 顶部信息栏：
  - 中间："管理成员"标题
  - 右侧：关闭按钮
- 搜索和邀请区：
  - 搜索框：支持搜索成员
  - 右侧邀请按钮：邀请新成员入会（现在完全可用）
- Tab切换：
  - "会议中(N)"：蓝色选���状态，显示当前参会人数
  - "未入会"：灰色未选中状态，显示已邀请但未入会的成员
- 成员列表：
  - 圆形头像（显示姓名首字母）
  - 成员姓名
  - 身份标签："(主持人，我)"
  - 麦克风图标：显示静音/开启状态，受全员静音状态影响
  - 摄像头图标：显示关闭/开启状态
- 底部按钮：
  - "全体静音"：主持人功能，点击后所有成员麦克风图标变为红色静音状态
  - "解除全体静音"：主持人功能，点击后恢复成员麦克风图标原始状态
- 全员静音功能：支持主持人一键静音/解除静音所有成员，麦克风图标实时响应状态变化
- **邀请新成员功能**：
  - 点击右上角邀请按钮打开联系人选择对话框
  - 显示可邀请的联系人（排除已入会和已邀请的成员）
  - 支持多选联系人进行批量邀请
  - 邀请成功后在"未入会"Tab显示已邀请的成员
  - 邀请状态包括：待响应、已接受、已拒绝、已过期
- 从会议详情页"管理成员"按钮进入

### MeetingChatPage（会议聊天页面）
- 顶部信息栏：
  - 白色背景
  - 中间："聊天"标题（居中显示，粗体18sp）
  - 右侧：关闭按钮（X图标）
- 消息列表区域：
  - 浅灰色背景（#F5F5F5）
  - 支持滚动显示历史消息
  - 消息气泡样式：
    - 我的消息：蓝色气泡，右对齐
    - 其他人消息：白色气泡，左对齐
    - 显示发送者姓名和发送时间（HH:mm格式）
  - 空状态：显示"暂无消息"提示
  - 加载状态：显示圆形加载指示器
- 底部输入区：
  - 白色背景，顶部阴影
  - 圆角文本输入框（提示："请输入消息..."）
  - 右侧圆形发送按钮：
    - 有内容时蓝色可点击
    - 无内容时灰色禁用
  - 支持多行输入（最多3行）
- 消息功能：
  - 根据会议ID加载历史消息
  - 支持发送文本消息
  - 消息保存到内存（关闭APP后消失）
  - 发送消息后自动滚动到最新
  - 当前用户显示为"我"
- 页面导航：
  - 从会议详情页左下角聊天入口打开
  - 点击关闭按钮返回会议详情页

### MeetingReplayPage（会议回放页面）
- 顶部信息栏：
  - 深色背景（#1F2227）
  - 左侧：返回按钮
  - 中间：会议主题（居中显示）
- 视频播放区域：
  - 黑色背景，16:9比例
  - 圆角设计（12dp）
  - 中央大播放按钮（白色半透明背景）
  - 显示"会议回放"文字提示
  - 显示会议时长信息
  - 点击播放/暂停按钮切换播放状态
- 播放控制栏：
  - 进度条：显示当前播放时间和总时长
  - 左侧：播放/暂停按钮（圆形半透明背景）
  - 右侧：倍速按钮（1.0x/1.5x/2.0x切换）
- 会议信息卡片：
  - 深色背景（#3C4148）
  - 显示会议号
  - 显示开始时间（yyyy-MM-dd HH:mm格式）
  - 显示会议时长（自动计算）
  - 显示参会人数
- 功能特性：
  - 仅界面示意，不实际播放视频
  - 所有控制按钮可点击但不执行真实播放
  - 从MePage历史会议列表点击进入
  - MVP架构实现，数据从meetings.json加载

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
│   ├── MeetingInvitation.kt         # 会议邀请数据模型（新增）
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
│   ├── QuickMeetingContract.kt
│   ├── MeetingDetailsContract.kt
│   ├── MembersManageContract.kt
│   └── MeetingChatContract.kt
├── presenter/                     # 业务逻辑层
│   ├── HomePresenter.kt
│   ├── MePresenter.kt
│   ├── ContactPresenter.kt
│   ├── ScheduledMeetingPresenter.kt
│   ├── AddFriendsPresenter.kt
│   ├── FriendsDetailsPresenter.kt
│   ├── JoinMeetingPresenter.kt
│   ├── QuickMeetingPresenter.kt
│   ├── MeetingDetailsPresenter.kt
│   ├── MembersManagePresenter.kt
│   └── MeetingChatPresenter.kt
├── view/                          # UI层
│   ├── HomePage.kt
│   ├── MePage.kt
│   ├── ContactPage.kt
│   ├── ScheduledMeetingPage.kt
│   ├── AddFriendsPage.kt
│   ├── FriendsDetailsPage.kt
│   ├── JoinMeetingPage.kt
│   ├── QuickMeetingPage.kt
│   ├── MeetingDetailsPage.kt
│   ├── MembersManagePage.kt
│   └── MeetingChatPage.kt
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
- `meeting_invitations.json` - 会议邀请记录（新增）

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
- [x] MeetingDetailsPage UI实现（会议详情页面）
- [x] 页面导航功能（QuickMeetingPage/JoinMeetingPage/ScheduledMeetingPage -> MeetingDetailsPage）
- [x] 会议详情显示、参会人列表、设备控制、会议时长计时
- [x] MeetingDetailsPage UI微调（顶部栏添加黑色背景和间距、弹幕区位置优化、用户名统一为刘承龙）
- [x] MeetingDetailsPage UI再次微调（顶部黑色背景延伸至屏幕边缘、删除退出按钮、功能居中显示）
- [x] 数据微调（通讯录第一个好友从刘承龙改为张三）
- [x] MembersManagePage UI实现（成员管理页面）
- [x] 页面导航功能（MeetingDetailsPage -> MembersManagePage）
- [x] 成员列表显示、搜索功能、Tab切换、全员静音/解除全员静音
- [x] MembersManagePage全员静音功能完善（麦克风图标实时响应状态变化）
- [x] MeetingChatPage UI实现（会议聊天页面）
- [x] 页面导航功能（MeetingDetailsPage -> MeetingChatPage）
- [x] 聊天消息显示、发送文本消息、消息气泡样式
- [x] 消息数据管理（内存存储）、消息列表自动滚动
- [x] ShareScreen屏幕共享功能实现
- [x] 屏幕共享状态切换（点击"共享屏幕"按钮切换视图）
- [x] ScreenShareView组件实现（模拟手机桌面、渐变壁纸、应用图标）
- [x] 页面间数据传递优化
- [x] MePage历史会议UI微调（删除右侧箭头，添加更多历史会议记录）
- [x] 会议设���传递优化（快速会议、加入会议的设置状态传递到会议详情页）
- [x] MembersManagePage全员静音功能验证（确认功能正常工作）
- [x] ScheduledMeetingPage预定会议优化（完成预定后返回主页而非直接进入会议）
- [x] HomePage会议列表优化（删除右侧箭头，支持点击进行中会议直接进入）
- [x] MePage历史会议列表滑动修复（添加高度约束，使列表可以正常滚动）
- [x] MePage整体滚动功能完善（添加verticalScroll，修复滑动问题）
- [x] MePage功能选项删除（移除"个人信息"、"会议设置"、"关于"三行）
- [x] ScheduledMeetingPage日期时间选择器实现（支持更改会议开始时间）
- [x] ScheduledMeetingPage编译错误修复（修复字符串拼接语法错误，删除重复函数定义）
- [x] MembersManagePage邀请新成员功能修复（原点击邀请按钮无响应问题）
  - [x] 创建MeetingInvitation数据模型和InvitationStatus枚举
  - [x] 创建meeting_invitations.json数据文件存储邀请记录
  - [x] 扩展DataRepository添加邀请管理方法
  - [x] 更新MembersManageContract接口添加邀请相关方法
  - [x] 在MembersManagePage中添加联系人选择对话框UI
  - [x] 完善MembersManagePresenter的邀请逻辑
  - [x] 更新未入会Tab显示已邀请成员列表
  - [x] 实现完整的邀请流程和状态管理
- [x] UI优化和功能完善
  - [x] MePage：删除个人信息区右侧的设置齿轮图标
  - [x] MePage：历史会议列表显示会议时长（自动计算小时和分钟）
  - [x] JoinMeetingPage：添加入会密码输入框（装饰功能）
  - [x] MeetingDetailsPage：顶部信息栏添加录制按钮（扬声器图标右侧）
- [x] MeetingReplayPage会议回放页面实现
  - [x] 创建MeetingReplayContract.kt（MVP接口定义）
  - [x] 创建MeetingReplayPresenter.kt（业务逻辑）
  - [x] 创建MeetingReplayPage.kt（UI界面）
  - [x] 视频播放区域（16:9比例，播放/暂停按钮，仅界面示意）
  - [x] 播放控制栏（进度条、播放按钮、倍速按钮）
  - [x] 会议信息卡片（会议号、时间、时长、人数）
  - [x] 从MePage历史会议列表点击进入回放页面
  - [x] 完整导航流程实现（MePage -> MeetingReplayPage）
- [x] 微调2：新增举手功能和会议录制开关
  - [x] MeetingDetailsPage举手功能
    - [x] 扩展左下角聊天输入区宽度（从280dp增加到380dp）
    - [x] 在输入框右侧新增举手按钮（使用PanTool图标）
    - [x] 添加举手状态管理（未举手/已举手）
    - [x] 举手后图标变为橙色，再次点击取消举手
  - [x] 主持人视角举手提示
    - [x] 在输入区上方显示举手者信息卡片
    - [x] 显示举手者姓名和橙色手掌图标
    - [x] 提供"解除静音"按钮供主持人操作
    - [x] 点击解除静音后自动移除提示卡片
  - [x] QuickMeetingPage添加会议录制开关
    - [x] 在"扬声器是否开启"选项后新增一行
    - [x] 添加"会议录制"开关（默认关闭）
  - [x] ScheduledMeetingPage添加会议录制开关
    - [x] 在"入会密码"选项后新增一行
    - [x] 添加"会议录制"开关（默认关闭）
  - [x] JoinMeetingPage添加会议录制开关
    - [x] 在"开启视频"选项后新增一行
    - [x] 添加"会议录制"开关（默认关闭）

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
