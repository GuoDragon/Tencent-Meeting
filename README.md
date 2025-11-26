# Tencent-Meeting
腾讯会议模拟

## 项目介绍
这是一个腾讯会议App的模拟版本，采用Android Jetpack Compose技术栈开发。项目遵循MVP架构模式，提供了腾讯会议的核心功能界面。

## 已实现功能
### HomePage（会议首页）
- 用户信息区域（与MePage样式一致）：
  - 白色卡片样式，带阴影效果
  - 圆形头像显示"刘"字（80dp，蓝色背景）
  - 显示用户姓名"刘承龙"（22sp，粗体）
  - 显示手机号和邮箱信息
  - **点击用户信息卡片可跳转到MeTab（"我的"页面）**
- 功能按钮区域：
  - 加入会议
  - 快速会议
  - 预定会议
  - 共享屏幕
- 会议列表：
  - 显示进行中和待开始的会议
  - 右上角历史会议按钮，点击查看历史会议列表
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
- 用户信息卡片：显示头像、昵称、手机号、邮箱，点击可跳转到个人信息页
- 功能网格区域（6个功能按钮，2行3列布局）：
  - 个人会议室（可点击，跳转到个人会议室页面）
  - 录制（可点击，跳转到录制页面）
  - 我的笔记（仅显示，不可点击）
  - AI 助手（仅显示，不可点击）
  - 订单与服务（仅显示，不可点击）
  - 控制Rooms（仅显示，不可点击）
- 设置列表区域（6个独立卡片，带右箭头图标）：
  - 积分中心（仅显示，不可点击）
  - 账号与安全（仅显示，不可点击）
  - 设置（仅显示，不可点击）
  - 隐私（仅显示，不可点击）
  - 帮助与客服（仅显示，不可点击）
  - 关于我们（仅显示，不可点击）
- 退出登录按钮（红色文字，居中显示，仅显示，不可点击）
- 注：历史会议功能已全部迁移至HomePage

### PersonalInformationPage（个人信息页面）
- 顶部背景区域：
  - 灰蓝色渐变背景（300dp高度）
  - 左上角黑色箭头返回按钮（24dp，ArrowBack图标）
  - 居中显示"点击设置背景图"文字提示
- 头像区域：
  - 圆形头像（120dp），灰色背景
  - 使用AccountCircle图标占位
  - 头像位置向上偏移60dp，与背景区域重叠
- 信息卡片：
  - 白色圆角卡片，带阴影效果
  - 名称行：显示用户姓名（刘承龙），右侧有箭头图标，可点击但暂不处理
  - 签名行：显示签名提示文字，右侧有箭头图标，可点击但暂不处理
  - 两行之间用细分割线分隔
- 注：不实现"认证"相关内容
- 导航来源：从MePage点击用户信息卡片进入

### RecordPage（录制页面）
- 顶部导航栏：
  - 白色背景，标准TopAppBar设计
  - 左侧：返回按钮
  - 中间："录制"标题（18sp粗体）
  - 右侧：搜索图标按钮
- 存储信息栏：
  - 白色背景
  - 显示存储使用情况："已使用 0MB/1GB (共0个云端存储文件)"
  - "扩容 >"链接按钮（蓝色文字）
- Tab栏：
  - 三个标签：全部文件（默认选中）、最近浏览、我的录制
  - 蓝色选中状态，白色背景
- 过滤按钮：
  - 右侧显示FilterList图标
  - 灰色图标，可点击但暂不处理
- 空状态区域：
  - 浅灰色背景（#F5F5F5）
  - 中央显示FolderOpen图标（120dp，灰蓝色）
  - 下方显示"暂无录制文件"文字提示
- 浮动操作按钮（FAB）：
  - 右下角蓝色圆形按钮
  - 白色麦克风图标（28dp）
  - 点击可开始录制（功能暂不处理）
- 导航来源：从MePage点击"录制"按钮进入

### PersonalMeetingRoomPage（个人会议室页面）
- 顶部导航栏：
  - 左上角返回按钮
  - 右侧个人资料和分享图标
- 会议室信息卡片（白色圆角卡片，带阴影）：
  - 圆形头像显示用户姓名首字母（80dp蓝色背景）
  - 会议室标题："XXX的个人会议室"
  - 会议号：格式化显示（415 755 5988），带复制按钮
  - 会议链接：完整链接地址，带复制按钮
  - 编辑资料按钮和二维码按钮
- 会议设置列表（白色圆角卡片，带阴影）：
  - 入会密码：显示当前密码或"未设置"，点击打开密码编辑对话框
  - 等候室：显示"关闭"或"开启"，点击打开选择对话框
  - 允许成员在主持人前入会：显示"否"或"是"，点击打开选择对话框
  - 会议水印：显示"未开启"或"开启"，点击打开选择对话框
  - 成员入会时静音：显示静音规则，点击打开选择对话框（3个选项）
  - 允许成员多端入会：显示"否"或"是"，点击打开选择对话框
- 底部"进入会议室"按钮：蓝色大按钮，点击进入MeetingDetailsPage
- 功能特性：
  - 复制会议号和链接到剪贴板
  - 所有设置可通过对话框修改，保存到内存
  - MVP架构实现，数据从personal_meeting_rooms.json加载
  - 密码对话框支持开关和6位数字密码输入
  - 静音规则支持3种模式：关闭、超过6人后自动开启、始终开启

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
  - 共享屏幕：正常情况下白色图标，开启屏幕共享后变为绿色图标，点击切换屏幕共享状态
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
  - 从HistoryMeetingsPage历史会议列表点击进入
  - MVP架构实现，数据从meetings.json加载

### ScheduledMeetingDetailsPage（预定会议详情页面）
- 顶部信息栏：
  - 淡蓝色背景（#E3F2FD），与系统状态栏颜色统一
  - 顶部间距32dp（避免覆盖系统状态栏，比其他页面稍大）
  - 左侧：返回按钮
  - 中间："会议详情"标题（居中显示，粗体18sp）
- 会议信息展示：
  - 会议主题（24sp粗体）
  - 时间信息卡片：
    - 左侧：开始时间（HH:mm格式，32sp粗体）和日期
    - 中间：状态"待开始"和会议时长
    - 右侧：结束时间（HH:mm格式，32sp粗体）和日期
  - 会议号：右侧添加复制按钮（ContentCopy图标），点击复制到剪贴板
  - 发起人（显示"刘承龙"）
  - 电话入会（显示固定电话号码）
  - 应用按钮（点击无反应）
  - 会议资料按钮（点击无反应）
- 中间五行信息调整：
  - 左侧标题向右移动（增加8dp内边距）
  - 右侧内容向内移动（增加8dp内边距）
- 底部按钮区：
  - 左侧：AI托管按钮（OutlinedButton，点击无反应）
  - 右侧：进入会议按钮（蓝色，点击进入MeetingDetailsPage）
- 导航来源：
  - 从HomePage点击UPCOMING状态的会议进入
- MVP架构实现，数据从meetings.json加载

### HistoryMeetingsPage（历史会议列表页面）
- 顶部信息栏：
  - 白色背景
  - 顶部间距24dp（避免覆盖系统状态栏）
  - 左侧：返回按钮
  - 中间："历史会议"标题（居中显示，粗体18sp）
  - 右侧：三点菜单图标（仅显示）
- 搜索框：
  - 提示文字："会议名称、会议备注、会议号、发起人"
  - 支持实时搜索过滤会议列表
- 会议列表展示：
  - 按日期分组（"10月25日 周六"格式）
  - 每个会议卡片显示：
    - 会议号（灰色小字）
    - 会议主题（16sp粗体）
    - 时间和发起人信息
    - 右侧箭头图标
  - 点击会议卡片 → 导航到MeetingReplayPage
- 空状态：
  - 显示History图标和"暂无历史会议"提示
- 导航来源：
  - 从HomePage点击"历史会议"按钮进入
- MVP架构实现，加载status为ENDED的会议，按时间倒序排列

### ShareScreenInputPage（共享屏幕输入页面）
- 页面设计：
  - 浅蓝到灰白渐变背景
  - 顶部间距24dp（避免覆盖系统状态栏）
  - 左上角"取消"按钮
- 内容区域：
  - 标题："共享屏幕"（28sp粗体，居中）
  - 副标题："请输入 Rooms共享码 或 会议号"（14sp灰色，居中）
  - 会议号输入框：
    - 白色背景，圆角边框
    - 单行输入
  - "开始共享"按钮：
    - 输入框为空时：浅蓝色（#90CAF9），禁用状态
    - 输入框有内容时：深蓝色（#1976D2），可点击
    - 点击后导航到MeetingDetailsPage并自动开启屏幕共享
- 底部帮助文本：
  - "如何共享屏幕?"蓝色文本链接（仅显示）
- 导航来源：
  - 从HomePage点击"共享屏幕"按钮进入
- 导航目标：
  - 输入会议号后进入MeetingDetailsPage，屏幕共享视图自动开启
- MVP架构实现

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
│   ├── PersonalMeetingRoom.kt       # 个人会议室数据模型（新增）
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
│   ├── MeetingChatContract.kt
│   ├── ScheduledMeetingDetailsContract.kt   # 预定会议详情
│   ├── HistoryMeetingsContract.kt           # 历史会议列表
│   ├── ShareScreenInputContract.kt          # 共享屏幕输入
│   ├── PersonalMeetingRoomContract.kt       # 个人会议室
│   ├── PersonalInformationContract.kt       # 个人信息（新增）
│   └── RecordContract.kt                    # 录制页面（新增）
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
│   ├── MeetingChatPresenter.kt
│   ├── ScheduledMeetingDetailsPresenter.kt  # 预定会议详情
│   ├── HistoryMeetingsPresenter.kt          # 历史会议列表
│   ├── ShareScreenInputPresenter.kt         # 共享屏幕输入
│   ├── PersonalMeetingRoomPresenter.kt      # 个人会议室
│   ├── PersonalInformationPresenter.kt      # 个人信息（新增）
│   └── RecordPresenter.kt                   # 录制页面（新增）
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
│   ├── MeetingChatPage.kt
│   ├── ScheduledMeetingDetailsPage.kt       # 预定会议详情
│   ├── HistoryMeetingsPage.kt               # 历史会议列表
│   ├── ShareScreenInputPage.kt              # 共享屏幕输入
│   ├── PersonalMeetingRoomPage.kt           # 个人会议室
│   ├── PersonalInformationPage.kt           # 个人信息（新增）
│   └── RecordPage.kt                        # 录制页面（新增）
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
- `personal_meeting_rooms.json` - 个人会议室设置（新增）

## 数据存储详解

### 数据存储位置
APP运行时的数据存储采用双重路径设计：

**1. 开发环境（初始数据源）**
- 路径：`app/src/main/assets/data/`
- 用途：存放初始数据，打包到APK中
- 特点：只读，用于首次安装时初始化数据

**2. Android虚拟机/设备（运行时数据）**
- 路径：`/data/data/com.example.tencentmeeting/files/`
- 用途：APP运行时读写数据的实际位置
- 特点：可读写，所有用户操作会实时写入此目录
- 访问方式：通过ADB命令访问
  ```bash
  # 查看文件列表
  adb shell run-as com.example.tencentmeeting ls files/

  # 读取文件内容
  adb shell run-as com.example.tencentmeeting cat files/meetings.json

  # 拉取文件到本地
  adb exec-out run-as com.example.tencentmeeting cat files/meetings.json > meetings.json
  ```

### JSON文件详细说明

#### 1. users.json - 用户信息
**用途**：存储所有用户的账号信息（只读参考数据）

**修改操作**：无（此文件为只读基础数据，不会被APP操作修改）

**数据结构**：
```json
[
  {
    "userId": "user001",
    "username": "刘承龙",
    "avatar": "https://example.com/avatars/liuchenglong.jpg",
    "phone": "13912847563",
    "email": "liuchenglong@example.com"
  }
]
```

**字段说明**：
- `userId`: 用户唯一标识
- `username`: 用户姓名
- `avatar`: 头像URL
- `phone`: 手机号
- `email`: 邮箱地址

---

#### 2. meetings.json - 会议信息
**用途**：存储所有会议的完整信息，包括进行中、待开始、已结束的会议

**修改操作**：
- ✅ 预定会议（ScheduledMeetingPresenter.saveMeeting）
- ✅ 创建快速会议（QuickMeetingPresenter.startQuickMeeting）
- ✅ 结束会议（MeetingDetailsPresenter.endMeeting）

**数据结构**：
```json
[
  {
    "meetingId": "meeting_8f4a2b",
    "topic": "项目启动会议",
    "password": "742983",
    "hostId": "user001",
    "startTime": 1697500800000,
    "endTime": 1697504400000,
    "status": "ENDED",
    "meetingType": "SCHEDULED",
    "participantIds": ["user001", "user002", "user003"],
    "settings": {
      "allowParticipantUnmute": true,
      "muteOnEntry": true,
      "cameraOffOnEntry": true,
      "enableWaitingRoom": false
    }
  }
]
```

**字段说明**：
- `meetingId`: 会议唯一标识
- `topic`: 会议主题
- `password`: 入会密码（可选）
- `hostId`: 主持人用户ID
- `startTime`: 开始时间（Unix时间戳，毫秒）
- `endTime`: 结束时间（Unix时间戳，毫秒）
- `status`: 会议状态（ONGOING/UPCOMING/ENDED）
- `meetingType`: 会议类型（QUICK/SCHEDULED/JOIN）
- `participantIds`: 参会人ID列表
- `settings`: 会议设置项

---

#### 3. meeting_participants.json - 参会人员状态
**用途**：跟踪每个参会人在会议中的实时状态

**修改操作**：
- ✅ 加入会议（JoinMeetingPresenter.joinMeeting）
- ✅ 开启快速会议（QuickMeetingPresenter添加主持人）
- ✅ 切换麦克风（MeetingDetailsPresenter.toggleMic）
- ✅ 切换摄像头（MeetingDetailsPresenter.toggleVideo）
- ✅ 共享屏幕（MeetingDetailsPresenter.shareScreen）
- ✅ 举手/放下手（MeetingDetailsPresenter.raiseHand/lowerHand）

**数据结构**：
```json
[
  {
    "userId": "user001",
    "meetingId": "meeting_8f4a2b",
    "isMuted": false,
    "isCameraOn": true,
    "isHandRaised": false,
    "handRaisedTime": null,
    "isSharingScreen": false,
    "joinTime": 1697500800000
  }
]
```

**字段说明**：
- `userId`: 参会人用户ID
- `meetingId`: 所属会议ID
- `isMuted`: 是否静音
- `isCameraOn`: 摄像头是否开启
- `isHandRaised`: 是否举手
- `handRaisedTime`: 举手时间（Unix时间戳）
- `isSharingScreen`: 是否正在共享屏幕
- `joinTime`: 加入会议时间

---

#### 4. messages.json - 会议聊天消息
**用途**：存储会议中发送的所有聊天消息

**修改操作**：
- ✅ 发送消息（MeetingChatPresenter.sendMessage）

**数据结构**：
```json
[
  {
    "messageId": "msg001",
    "meetingId": "meeting_8f4a2b",
    "senderId": "user001",
    "senderName": "刘承龙",
    "content": "欢迎参加项目启动会议",
    "timestamp": 1697500900000
  }
]
```

**字段说明**：
- `messageId`: 消息唯一标识
- `meetingId`: 所属会议ID
- `senderId`: 发送者用户ID
- `senderName`: 发送者姓名
- `content`: 消息内容
- `timestamp`: 发送时间（Unix时间戳）

---

#### 5. hand_raise_records.json - 举手记录
**用途**：记录参会人的举手动作历史（包括举手和放下的时间）

**修改操作**：
- ✅ 举手（MeetingDetailsPresenter.raiseHand）
- ✅ 放下手（MeetingDetailsPresenter.lowerHand）

**数据结构**：
```json
[
  {
    "recordId": "hr001",
    "meetingId": "meeting_8f4a2b",
    "userId": "user002",
    "userName": "陈思远",
    "raiseTime": 1697501800000,
    "lowerTime": 1697502100000
  }
]
```

**字段说明**：
- `recordId`: 记录唯一标识
- `meetingId`: 所属会议ID
- `userId`: 举手用户ID
- `userName`: 举手用户姓名
- `raiseTime`: 举手时间
- `lowerTime`: 放下手时间（null表示仍在举手中）

---

#### 6. meeting_invitations.json - 会议邀请记录
**用途**：存储会议邀请的发送和响应状态

**修改操作**：
- ✅ 邀请成员（MembersManagePresenter.sendInvitations）

**数据结构**：
```json
[
  {
    "invitationId": "invite001",
    "meetingId": "meeting_8f4a2b",
    "inviterId": "user001",
    "inviteeId": "user002",
    "status": "PENDING",
    "invitedTime": 1700000000000,
    "respondedTime": null
  }
]
```

**字段说明**：
- `invitationId`: 邀请唯一标识
- `meetingId`: 所属会议ID
- `inviterId`: 邀请人用户ID
- `inviteeId`: 被邀请人用户ID
- `status`: 邀请状态（PENDING/ACCEPTED/DECLINED/EXPIRED）
- `invitedTime`: 邀请发送时间
- `respondedTime`: 响应时间（null表示未响应）

---

#### 7. personal_meeting_rooms.json - 个人会议室设置
**用途**：存储每个用户的个人会议室配置信息

**修改操作**：
- ✅ 更新入会密码（PersonalMeetingRoomPresenter.updatePassword）
- ✅ 更新等候室设置（PersonalMeetingRoomPresenter.updateWaitingRoom）
- ✅ 更新主持人前入会（PersonalMeetingRoomPresenter.updateAllowBeforeHost）
- ✅ 更新会议水印（PersonalMeetingRoomPresenter.updateWatermark）
- ✅ 更新入会静音规则（PersonalMeetingRoomPresenter.updateMuteOnEntry）
- ✅ 更新多端入会（PersonalMeetingRoomPresenter.updateMultiDevice）

**数据结构**：
```json
[
  {
    "userId": "user001",
    "meetingId": "4157555988",
    "meetingLink": "meeting.tencent.com/p/4157555988",
    "password": "559706",
    "enablePassword": true,
    "enableWaitingRoom": false,
    "allowBeforeHost": false,
    "enableWatermark": false,
    "muteOnEntry": "关闭",
    "allowMultiDevice": false
  }
]
```

**字段说明**：
- `userId`: 用户ID
- `meetingId`: 个人会议号
- `meetingLink`: 个人会议室链接
- `password`: 入会密码
- `enablePassword`: 是否启用密码
- `enableWaitingRoom`: 是否启用等候室
- `allowBeforeHost`: 是否允许成员在主持人前入会
- `enableWatermark`: 是否启用会议水印
- `muteOnEntry`: 入会静音规则（关闭/超过6人后自动开启/始终开启）
- `allowMultiDevice`: 是否允许成员多端入会

---

### 数据持久化机制

#### 存储策略
APP采用混合存储架构，确保数据的实时性和持久性：

1. **内存缓存**：DataRepository维护内存中的数据列表，提供快速读取
2. **文件持久化**：每次数据修改后立即写入JSON文件
3. **初始化流程**：首次启动时从assets复制数据到filesDir

#### Gson配置
所有JSON文件使用Gson库进行序列化/反序列化：
```kotlin
private val gson = GsonBuilder().setPrettyPrinting().create()
```

**特点**：
- ✅ Pretty Printing启用：自动格式化输出
- ✅ 2空格缩进：提高可读性
- ✅ UTF-8编码：支持中文字符
- ✅ 每个属性独占一行：便于人工查看和调试

#### 数据写入时机
所有用户操作都会**实时触发数据持久化**，无需手动保存：

| 用户操作 | 触发时机 | 写入文件 |
|---------|---------|---------|
| 预定会议 | 点击"完成"按钮 | meetings.json |
| 发送消息 | 点击"发送"按钮 | messages.json |
| 创建快速会议 | 点击"开始会议" | meetings.json + meeting_participants.json |
| 加入会议 | 点击"加入" | meeting_participants.json |
| 结束会议 | 点击"结束"按钮 | meetings.json（更新status和endTime） |
| 邀请成员 | 选择联系人确认 | meeting_invitations.json |
| 切换麦克风 | 点击麦克风按钮 | meeting_participants.json |
| 切换摄像头 | 点击视频按钮 | meeting_participants.json |
| 共享屏幕 | 点击共享按钮 | meeting_participants.json |
| 举手/放下手 | 点击举手按钮 | hand_raise_records.json + meeting_participants.json |
| 修改个人会议室 | 修改任意设置 | personal_meeting_rooms.json |

#### 数据完整性保证
- **去重处理**：使用`distinctBy()`避免重复记录
- **异常捕获**：所有文件操作包裹在try-catch中
- **原子操作**：先更新内存，再写入文件，确保一致性

## 自动化测试（AutoTest）
项目包含GUI自动化测试脚本，用于验证APP中各种操作的正确性。

### 数据流程说明
1. **数据源位置**：`app/src/main/assets/data/`
   - 这是APP的数据源，包含所有7个JSON文件
   - 当APP编译运行时，这些数据会被打包到APK中

2. **推送测试数据到虚拟机**：
   - 使用`AutoTest/push_test_data.py`脚本
   - 将assets/data中的JSON文件推送到Android虚拟机的内部存储
   - 目标路径：虚拟机中的`files/`目录

3. **APP运行时数据读写**：
   - APP运行时从虚拟机内部存储读取和写入数据
   - 用户在APP中的操作会更新虚拟机中的JSON文件

4. **自动化测试验证**：
   - 运行`AutoTest/eval_*.py`脚本进行验证
   - 脚本通过ADB从虚拟机拉取最新的JSON数据
   - 脚本检查数据是否符合预期，验证GUI操作是否正确执行

### 测试脚本列表
- eval_1.py - 检测麦克风静音状态记录
- eval_2.py - 检测摄像头开启状态记录
- eval_3.py - 检测最近结束的会议记录
- eval_4.py - 检测参会者麦克风静音控制记录
- eval_5.py - 检测会议中消息发送记录
- eval_6.py - 检测快速会议创建和进入记录
- eval_7.py - 检测预定会议记录
- eval_8.py - 检测历史会议回放功能
- eval_9.py - 检测通过会议号和密码加入会议
- eval_10.py - 检测通过邀请链接加入会议
- eval_11.py - 检测屏幕共享记录
- eval_12.py - 检测举手功能
- eval_13.py - 检测会议记录和时长
- eval_14.py - 检测参会者列表和发送消息
- eval_15.py - 检测快速会议与参会者
- eval_16.py - 检测预定会议与邀请
- eval_17.py - 检测用户搜索和添加好友
- eval_18.py - 检测预定会议录制功能
- eval_19.py - 检测查看未参会邀请者并重新邀请
- eval_20.py - 检测通讯录列表和复制邀请链接

### 重要说明
- JSON数据文件应该只存在于`app/src/main/assets/data/`目录
- 根目录和AutoTest目录下的JSON文件是临时文件（已添加到.gitignore）
- AutoTest目录下的JSON文件是eval脚本运行时从虚拟机拉取的临时数据
- 所有JSON文件均使用UTF-8编码，格式化为易读格式（缩进和换行）

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
- [x] 微调3：会议录制状态传递功能
  - [x] MeetingDetailsPage添加initialRecordingEnabled参数
    - [x] 在函数参数中添加initialRecordingEnabled参数
    - [x] 使用该参数初始化isRecording状态
  - [x] QuickMeetingPage传递录制状态
    - [x] 修改onNavigateToMeetingDetails函数签名，添加第5个Boolean参数
    - [x] 在导航回调中传递recordingEnabled状态
  - [x] JoinMeetingPage传递录制状态
    - [x] 修改onNavigateToMeetingDetails函数签名，添加第5个Boolean参数
    - [x] 在导航回调中传递recordingEnabled状态
  - [x] MainActivity导航调用更新
    - [x] 添加initialRecordingEnabled状态变量
    - [x] 更新QuickMeetingPage的导航lambda，接收并保存录制状态
    - [x] 更新JoinMeetingPage的导航lambda，接收并保存录制状态
    - [x] 在MeetingDetailsPage调用中传递initialRecordingEnabled参数
  - [x] 实现效果：用户在快速会议或加入会议页面开启"会议录制"后，进入会议详情页时左上角的录制按钮会显示为红色（录制中状态）
- [x] 微调4：HomePage页面优化
  - [x] 添加顶部用户信息区域
    - [x] 显示当前用户头像（圆形图标）
    - [x] 显示用户姓名"刘承龙"
  - [x] 功能按钮区域扩展
    - [x] 在右侧新增"共享屏幕"按钮（第4个按钮）
    - [x] 使用ScreenShare图标
  - [x] 会议列表优化
    - [x] 标题行右侧添加"历史会议"按钮
    - [x] 点击按钮查看历史会议列表（status为ENDED的会议）
    - [x] 按钮样式：灰色背景、小字体、右箭头图标
  - [x] MVP架构扩展
    - [x] HomeContract添加showUserInfo和showHistoryMeetings方法
    - [x] HomePresenter添加loadCurrentUser和onHistoryMeetingsClicked方法
    - [x] 从DataRepository加载刘承龙（user001）的用户信息
    - [x] 支持切换显示当前会议和历史会议列表
- [x] 微调5：HomePage用户信息区域样式统一
  - [x] 将HomePage顶部用户信息区域改为与MePage完全一致的样式
  - [x] 使用Card卡片样式（白色背景，2dp阴影）
  - [x] 头像改为圆形80dp，显示文字"刘"（32sp粗体）
  - [x] 姓名改为22sp粗体显示"刘承龙"
  - [x] 添加手机号和邮箱信息显示
  - [x] 保持视觉一致性，提升用户体验
- [x] 微调6：HomePage布局间距优化
  - [x] 参考截图比例调整页面垂直间距
  - [x] 添加顶部间距24dp，让个人信息区域往下移动
  - [x] 缩小用户信息和功能按钮之间的间距（从屏幕高度10%改为16dp）
  - [x] 让功能区和会议列表区域往上移动
  - [x] 移除不再使用的动态计算变量（configuration、screenHeight、topSpacing）
  - [x] 页面布局更加紧凑，符合实际APP的视觉比例
- [x] 功能扩展7：预定会议详情页和历史会议列表页
  - [x] 创建ScheduledMeetingDetailsPage（预定会议详情页）
    - [x] 创建ScheduledMeetingDetailsContract.kt和Presenter
    - [x] 显示会议主题、时间信息卡片、会议号、发起人、电话入会
    - [x] 应用和会议资料按钮（仅显示，点击无反应）
    - [x] 底部AI托管和进入会议按钮
    - [x] 点击进入会议导航到MeetingDetailsPage
  - [x] 创建HistoryMeetingsPage（历史会议列表页）
    - [x] 创建HistoryMeetingsContract.kt和Presenter
    - [x] 顶部栏：返回、标题、三点菜单
    - [x] 搜索框：支持实时搜索过滤
    - [x] 按日期分组显示会议列表
    - [x] 点击会议导航到MeetingReplayPage
  - [x] 修改HomePage会议项点击逻辑
    - [x] ONGOING状态 → 进入MeetingDetailsPage
    - [x] UPCOMING状态 → 显示ScheduledMeetingDetailsPage
    - [x] 历史会议按钮 → 导航到独立的HistoryMeetingsPage
  - [x] 修改MainActivity添加新页面导航
    - [x] 导入ScheduledMeetingDetailsPage和HistoryMeetingsPage
    - [x] 添加导航状态变量
    - [x] 配置完整的导航流程
  - [x] 布局微调：修复系统状态栏遮挡问题
    - [x] ScheduledMeetingDetailsPage添加顶部间距24dp
    - [x] HistoryMeetingsPage添加顶部间距24dp
- [x] 功能扩展8：共享屏幕输入页面
  - [x] 创建ShareScreenInputPage（共享屏幕输入页）
    - [x] 创建ShareScreenInputContract.kt和Presenter
    - [x] 浅蓝到灰白渐变背景设计
    - [x] 顶部"取消"按钮和标题"共享屏幕"
    - [x] 会议号输入框（白色背景，圆角边框）
    - [x] 动态按钮状态：输入为空时浅蓝色禁用，有内容时深蓝色可点击
    - [x] 底部"如何共享屏幕?"帮助链接
  - [x] 更新HomePage添加共享屏幕导航回调
    - [x] 点击"共享屏幕"按钮 → 导航到ShareScreenInputPage
  - [x] 更新MeetingDetailsPage支持初始屏幕共享状态
    - [x] 添加initialScreenSharing参数
    - [x] 根据参数自动开启屏幕共享视图
  - [x] 更新MainActivity完整导航流程
    - [x] 添加showShareScreenInputPage状态变量
    - [x] 添加initialScreenSharing状态管理
    - [x] 配置ShareScreenInputPage导航逻辑
    - [x] 传递屏幕共享状态到MeetingDetailsPage
- [x] UI样式优化：统一页面渐变背景
  - [x] 为所有主要页面应用垂直渐变背景(淡蓝#E3F2FD → 主色)
  - [x] 消除页面顶部与内容区域的颜色分界线
  - [x] 更新页面列表：
    - [x] HomePage - 淡蓝到灰白渐变
    - [x] ScheduledMeetingPage - 淡蓝到灰白渐变
    - [x] ScheduledMeetingDetailsPage - 淡蓝到灰白渐变
    - [x] HistoryMeetingsPage - 淡蓝到灰白渐变
    - [x] JoinMeetingPage - 淡蓝到灰白渐变
    - [x] QuickMeetingPage - 淡蓝到灰白渐变
    - [x] AddFriendsPage - 淡蓝到蓝灰渐变
    - [x] ContactPage - 淡蓝到灰白渐变
    - [x] MePage - 淡蓝到灰白渐变
    - [x] MeetingChatPage - 淡蓝到灰白渐变
    - [x] ShareScreenInputPage - 已有渐变(淡蓝到灰白)
  - [x] 保持原样的页面：
    - FriendsDetailsPage (已有灰蓝色Header设计)
    - MeetingReplayPage (深色主题)
    - MeetingDetailsPage (深色主题)
- [x] UI优化：状态栏颜色统一
  - [x] 添加Accompanist SystemUIController依赖库
  - [x] 在MainActivity中配置系统状态栏颜色为淡蓝色(#E3F2FD)
  - [x] 设置状态栏图标为深色，确保在浅色背景下可见
  - [x] 消除状态栏与页面内容的颜色分界线
- [x] MePage页面重构
  - [x] 删除历史会议相关功能（已迁移至HomePage）
  - [x] 保留用户信息卡片（个人信息）
  - [x] 新增功能网格区域（2行3列，共6个功能按钮）
  - [x] 新增设置列表区域（6个独立卡片，带右箭头）
  - [x] 新增红色退出登录按钮（底部居中显示）
  - [x] 参考ui/MeTab.jpg完成UI设计
- [x] UI优化：修复页面顶部栏颜色分界线问题
  - [x] 修复7个页面的TopAppBar颜色，统一为淡蓝色(#E3F2FD)
  - [x] ContactPage、AddFriendsPage、JoinMeetingPage顶部栏颜色修复
  - [x] QuickMeetingPage、ScheduledMeetingPage顶部栏颜色修复
  - [x] HistoryMeetingsPage、MeetingChatPage顶部栏颜色修复
  - [x] 所有页面顶部栏与系统状态栏颜色完全统一，无分界线
- [x] MePage页面微调优化
  - [x] 调整顶部间距从动态计算(10%屏幕高度)改为固定24.dp
  - [x] 个人信息区域高度与HomePage保持一致
  - [x] 优化功能网格布局对齐方式(使用SpaceAround + weight)
  - [x] 确保功能网格上下左右对齐更加规范
- [x] 数据优化：��系人信息真实化
  - [x] 修改users.json：
    - 保持"刘承龙"(user001)作为当前用户不变
    - 将其他用户名从"张三"、"王五"等改为更真实随机的中文名："陈思远"、"林雨萱"、"王俊豪"、"张梦琪"
    - 将所有电话号从简单递增(138001380XX)改为随机的11位手机号
    - 同步更新所有邮箱地址
  - [x] 修改meetings.json：
    - 将会议ID从meeting001-007改为更随机的格式(meeting_8f4a2b等)
    - 将密码从123456、888888等简单数字改为随机6位数字
  - [x] 修改messages.json：
    - 同步更新所有消息中的senderName以匹配新用户名
    - 同步更新所有meetingId以匹配新会议ID
  - [x] 修改ContactPresenter.kt：
    - 添加过滤逻辑，在loadContacts()中排除userId为"user001"的用户
    - 确保通讯录列表不显示当前用户"刘承龙"自己
- [x] 数据扩展：联系人列表扩充
  - [x] 扩充users.json至20个用户（user001-user020）
  - [x] 新增15个联系人，包含真实的中文姓名、随机11位手机号、邮箱等信息
  - [x] 通讯录显示19个联系人（排除当前用户刘承龙）
  - [x] 所有新增联系人遵循现有数据格式和命名规范
- [x] 功能扩展9：个人会议室页面
  - [x] 创建PersonalMeetingRoom数据模型
    - [x] userId、meetingId、meetingLink、password等字段
    - [x] 6个会议设置选项的字段
  - [x] 创建personal_meeting_rooms.json数据文件
    - [x] 初始化user001（刘承龙）的个人会议室数据
    - [x] 会议号：4157555988，密码：559706
  - [x] 更新DataRepository添加会议室数据方法
    - [x] getPersonalMeetingRoom()：获取个人会议室信息
    - [x] savePersonalMeetingRoom()：保存会议室设置到内存
  - [x] 创建PersonalMeetingRoomContract.kt和Presenter
    - [x] MVP模式接口定义
    - [x] 业务逻辑处理（加载数据、更新设置）
  - [x] 创建PersonalMeetingRoomPage.kt UI
    - [x] 顶部导航栏（返回、个人资料、分享图标）
    - [x] 会议室信息卡片（头像、标题、会议号、链接、复制功能、编辑资料、二维码）
    - [x] 会议设置列表（6个选项，每个可点击进入对话框）
    - [x] 入会密码对话框（开关+密码输入）
    - [x] 等候室对话框（关闭/开启）
    - [x] 允许成员在主持人前入会对话框（否/是）
    - [x] 会议水印对话框（关闭/开启）
    - [x] 成员入会时静音对话框（关闭/超过6人后自动开启/始终开启）
    - [x] 允许成员多端入会对话框（否/是）
    - [x] 底部"进入会议室"按钮（进入MeetingDetailsPage）
  - [x] 更新MePage添加导航回调
    - [x] "个人会议室"按钮从仅显示改为可点击
    - [x] 添加onPersonalMeetingRoomClick参数
  - [x] 更新MainActivity完整导航流程
    - [x] 添加showPersonalMeetingRoomPage状态
    - [x] 配置PersonalMeetingRoomPage条件渲染
    - [x] 连接MePage回调与PersonalMeetingRoomPage显示
    - [x] 支持从个人会议室进入会议详情页
- [x] UI微调10：个人会议室页面优化
  - [x] 调整"编辑资料"和"二维码"按钮位置
    - [x] 从会议室信息卡片底部移至头像/名字右侧
    - [x] 两个按钮垂直堆叠，每个按钮包含图标和文字
    - [x] 调整布局为左侧（头像+名字）+ 右侧（两个按钮）的Row结构
  - [x] 修复状态栏遮挡问题
    - [x] 增加顶部导航栏padding：从vertical 24.dp改为top 48.dp, bottom 12.dp
    - [x] 确保返回按钮不被系统状态栏遮挡
  - [x] 动态状态栏颜色切换
    - [x] 在MainActivity中添加状态栏颜色逻辑
    - [x] 会议中（MeetingDetailsPage/MeetingChatPage/MeetingReplayPage）：深色状态栏(#1F2227)，浅色图标
    - [x] 其他页面：淡蓝色状态栏(#E3F2FD)，深色图标
    - [x] 通过showMeetingDetailsPage等状态变量判断当前页面类型
- [x] 功能优化11：默认会议参与者设置
  - [x] 修改MeetingDetailsPresenter和MembersManagePresenter参会人加载逻辑
  - [x] 每次进入会议时自动包含当前用户和前5位好友作为默认参会人（共6人）
  - [x] 默认参会人列表（按顺序）：
    - user001 - 刘承龙（当前用户/主持人）
    - user002 - 陈思远
    - user003 - 林雨萱
    - user004 - 王俊豪
    - user005 - 张梦琪
    - user006 - 赵晨阳
  - [x] 适用于所有会议入口（快速会议、加入会议、预定会议、个人会议室、主页）
  - [x] MeetingDetailsPage显示参会人头像和名字
  - [x] MembersManagePage显示完整的6人成员列表
  - [x] 参会人数按钮动态显示："管理成员(6)"
- [x] UI微调12：ScheduledMeetingDetailsPage页面优化
  - [x] 顶部栏背景色统一为淡蓝色(#E3F2FD)，与系统状态栏颜色一致
  - [x] 顶部间距从24dp增加到32dp，留出更多呼吸空间
  - [x] 中间五行信息左右内边距优化：
    - [x] 左侧标题增加8dp左内边距+8dp横向内边距
    - [x] 右侧内容增加8dp右内边距+8dp横向内边距
  - [x] 会议号行新增复制按钮功能：
    - [x] 在会议号右侧添加ContentCopy图标
    - [x] 实现点击复制到剪贴板功能（使用ClipboardManager）
  - [x] 修复导入语句，添加ClipData和ClipboardManager相关导入
- [x] 功能扩展13：个人信息和录制页面
  - [x] 创建PersonalInformationPage（个人信息页）
    - [x] 创建PersonalInformationContract.kt和Presenter
    - [x] 灰蓝色渐变背景顶部区域（300dp高度）
    - [x] 圆形头像（120dp），向上偏移60dp与背景重叠
    - [x] 白色信息卡片：名称行、签名行（可点击但暂不处理）
    - [x] 跳过所有"认证"相关内容按需求不实现
  - [x] 创建RecordPage（录制页）
    - [x] 创建RecordContract.kt和Presenter
    - [x] 标准TopAppBar：返回、标题"录制"、搜索图标
    - [x] 存储信息栏：显示使用量0MB/1GB和文件数量
    - [x] "扩容 >"链接按钮
    - [x] 三个Tab标签：全部文件、最近浏览、我的录制
    - [x] 过滤图标按钮
    - [x] 空状态UI：FolderOpen图标+"暂无录制文件"提示
    - [x] 蓝色麦克风FAB按钮
  - [x] 更新MePage添加导航回调
    - [x] 用户信息卡片改为可点击，添加onPersonalInfoClick参数
    - [x] "录制"按钮从onMeetingClick改为onRecordClick独立回调
  - [x] 更新MainActivity完整导航流程
    - [x] 导入PersonalInformationPage和RecordPage
    - [x] 添加showPersonalInformationPage和showRecordPage状态
    - [x] 配置条件渲染和导航逻辑
    - [x] 连接MePage回调与新页面显示
- [x] UI微调14：PersonalInformationPage退出按钮优化
  - [x] 将左上角文字按钮改为标准的黑色ArrowBack图标
  - [x] 使用24dp图标大小，与其他页面保持一致
  - [x] 保持原有的onNavigateBack()回调功能
  - [x] 视觉风格与ScheduledMeetingDetailsPage等页面统一
- [x] 功能扩展15：HomePage用户信息区域导航优化
  - [x] 为HomePage添加onNavigateToMeTab回调参数
  - [x] UserInfoSection组件添加点击处理逻辑
  - [x] 点击用户信息卡片跳转到MeTab（底部导航Tab 2）
  - [x] MainActivity中实现selectedTab状态切换
  - [x] 提升用户体验，快速访问个人信息和设置
- [x] UI微调16：MeetingDetailsPage共享屏幕按钮颜色优化
  - [x] 修改BottomFunctionBar函数，添加isScreenSharing参数
  - [x] 更新BottomFunctionBar调用处，传递isScreenSharing状态
  - [x] 实现动态颜色逻辑：未共享时白色，共享时绿色
  - [x] 与麦克风、视频按钮的交互模式保持一致
  - [x] 更新README.md文档说明
- [x] 功能扩展17：数据持久化实现
  - [x] DataRepository添加文件写入功能
    - [x] 添加writeJsonToFile通用方法
    - [x] 添加initializeDataFiles初始化方法
    - [x] 实现saveMeetingsToFile、saveMessagesToFile等7个持久化方法
    - [x] 添加addOrUpdateParticipant、addHandRaiseRecord等数据操作方法
    - [x] 使用GsonBuilder.setPrettyPrinting()格式化JSON输出
  - [x] 修改所有Presenter添加持久化调用
    - [x] QuickMeetingPresenter：创建快速会议时保存会议记录
    - [x] ScheduledMeetingPresenter：预定会议后调用saveMeetingsToFile
    - [x] JoinMeetingPresenter：加入会议时创建并保存参会人记录
    - [x] MeetingChatPresenter：发送消息后调用saveMessagesToFile
    - [x] MembersManagePresenter：发送邀请后调用saveInvitationsToFile
    - [x] PersonalMeetingRoomPresenter：所有设置更新后调用savePersonalMeetingRoomsToFile
    - [x] MeetingDetailsPresenter：添加updateParticipantStatus方法
    - [x] MeetingDetailsPresenter：toggleMic/toggleVideo/shareScreen时更新参会人状态
    - [x] MeetingDetailsPresenter：实现raiseHand/lowerHand举手功能
  - [x] MainActivity添加数据初始化
    - [x] onCreate中调用dataRepository.initializeDataFiles()
    - [x] 首次启动时将assets数据复制到filesDir
  - [x] 数据文件写入位置
    - [x] 所有JSON文件写入context.filesDir目录
    - [x] eval脚本通过ADB可以读取files/目录下的JSON文件
    - [x] 用户操作后数据实时保存，支持GUI Agent测试验证
- [x] 功能修复18：会议结束功能完善
  - [x] DataRepository添加updateMeeting()方法
    - [x] 支持更新会议信息并自动保存到文件
    - [x] 使用lambda函数式编程风格进行数据更新
  - [x] MeetingDetailsPresenter.endMeeting()方法完善
    - [x] 点击"结束"按钮时更新会议状态为ENDED
    - [x] 设置会议结束时间(endTime)为当前时间戳
    - [x] 自动调用saveMeetingsToFile()持久化数据
    - [x] 异常处理确保即使保存失败也能正常返回
  - [x] 修复问题：快速会议点击"结束"后会议状态正确更新,不再在首页显示"进行中"
  - [x] 修复问题：结束会议后数据正确写入JSON文件,eval脚本可以检测到会议已结束
- [x] 功能修复19：ScheduledMeetingPage参会人选择优化
  - [x] 过滤当前用户"刘承龙"(user001)不显示在参会人选择列表中
  - [x] ParticipantPickerDialog添加滚动功能,支持查看所有联系人
  - [x] 新增"全选"复选框功能
    - [x] 位于参会人列表顶部,带"全选"文字标签
    - [x] 点击全选复选框可一键选中/取消所有参会人
    - [x] 智能状态同步:手动选中全部参会人时全选框自动勾选
    - [x] 取消任意一个参会人时全选框自动取消

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
