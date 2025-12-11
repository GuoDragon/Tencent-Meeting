#!/bin/bash
# 迁移Presentation层文件的自动化脚本

SRC_BASE="D:/android_template/Tencent-Meeting/app/src/main/java"
OLD_PKG="com/example/tencentmeeting"
NEW_PKG="com/appsim/tencent_meeting_sim"

echo "开始迁移Presentation层..."

# Home模块
echo "处理Home模块..."
cp "$SRC_BASE/$OLD_PKG/contract/HomeContract.kt" "$SRC_BASE/$NEW_PKG/presentation/home/"
cp "$SRC_BASE/$OLD_PKG/presenter/HomePresenter.kt" "$SRC_BASE/$NEW_PKG/presentation/home/"
cp "$SRC_BASE/$OLD_PKG/view/HomePage.kt" "$SRC_BASE/$NEW_PKG/presentation/home/HomeScreen.kt"

# Meeting模块
echo "处理Meeting模块..."
for name in MeetingDetails QuickMeeting JoinMeeting ScheduleMeeting MeetingHistory MeetingHistoryDetails MeetingChat ShareScreen MembersManage; do
    cp "$SRC_BASE/$OLD_PKG/contract/${name}Contract.kt" "$SRC_BASE/$NEW_PKG/presentation/meeting/" 2>/dev/null
    cp "$SRC_BASE/$OLD_PKG/presenter/${name}Presenter.kt" "$SRC_BASE/$NEW_PKG/presentation/meeting/" 2>/dev/null
    if [ -f "$SRC_BASE/$OLD_PKG/view/${name}Page.kt" ]; then
        cp "$SRC_BASE/$OLD_PKG/view/${name}Page.kt" "$SRC_BASE/$NEW_PKG/presentation/meeting/${name}Screen.kt"
    fi
done

# Contact模块
echo "处理Contact模块..."
for name in Contact AddFriends FriendsDetails; do
    cp "$SRC_BASE/$OLD_PKG/contract/${name}Contract.kt" "$SRC_BASE/$NEW_PKG/presentation/contact/" 2>/dev/null
    cp "$SRC_BASE/$OLD_PKG/presenter/${name}Presenter.kt" "$SRC_BASE/$NEW_PKG/presentation/contact/" 2>/dev/null
    if [ -f "$SRC_BASE/$OLD_PKG/view/${name}Page.kt" ]; then
        cp "$SRC_BASE/$OLD_PKG/view/${name}Page.kt" "$SRC_BASE/$NEW_PKG/presentation/contact/${name}Screen.kt"
    fi
done

# Me模块
echo "处理Me模块..."
for name in Me PersonalInformation PersonalMeetingRoom PersonalMeetingRoomPassword Record; do
    cp "$SRC_BASE/$OLD_PKG/contract/${name}Contract.kt" "$SRC_BASE/$NEW_PKG/presentation/me/" 2>/dev/null
    cp "$SRC_BASE/$OLD_PKG/presenter/${name}Presenter.kt" "$SRC_BASE/$NEW_PKG/presentation/me/" 2>/dev/null
    if [ -f "$SRC_BASE/$OLD_PKG/view/${name}Page.kt" ]; then
        cp "$SRC_BASE/$OLD_PKG/view/${name}Page.kt" "$SRC_BASE/$NEW_PKG/presentation/me/${name}Screen.kt"
    fi
done

echo "文件复制完成，开始更新package声明和import..."

# 更新所有presentation目录下的文件
find "$SRC_BASE/$NEW_PKG/presentation" -name "*.kt" -type f | while read file; do
    # 更新package声明
    sed -i 's/package com\.example\.tencentmeeting\.contract/package com.appsim.tencent_meeting_sim.presentation/' "$file"
    sed -i 's/package com\.example\.tencentmeeting\.presenter/package com.appsim.tencent_meeting_sim.presentation/' "$file"
    sed -i 's/package com\.example\.tencentmeeting\.view/package com.appsim.tencent_meeting_sim.presentation/' "$file"

    # 根据文件所在目录确定正确的package
    if [[ "$file" == *"/home/"* ]]; then
        sed -i 's/package com\.appsim\.tencent_meeting_sim\.presentation$/package com.appsim.tencent_meeting_sim.presentation.home/' "$file"
    elif [[ "$file" == *"/meeting/"* ]]; then
        sed -i 's/package com\.appsim\.tencent_meeting_sim\.presentation$/package com.appsim.tencent_meeting_sim.presentation.meeting/' "$file"
    elif [[ "$file" == *"/contact/"* ]]; then
        sed -i 's/package com\.appsim\.tencent_meeting_sim\.presentation$/package com.appsim.tencent_meeting_sim.presentation.contact/' "$file"
    elif [[ "$file" == *"/me/"* ]]; then
        sed -i 's/package com\.appsim\.tencent_meeting_sim\.presentation$/package com.appsim.tencent_meeting_sim.presentation.me/' "$file"
    fi

    # 更新import语句
    sed -i 's/import com\.example\.tencentmeeting\.model\./import com.appsim.tencent_meeting_sim.data.model./g' "$file"
    sed -i 's/import com\.example\.tencentmeeting\.data\./import com.appsim.tencent_meeting_sim.data.repository./g' "$file"
    sed -i 's/import com\.example\.tencentmeeting\.ui\.theme\./import com.appsim.tencent_meeting_sim.ui.theme./g' "$file"
    sed -i 's/import com\.example\.tencentmeeting\.contract\./import com.appsim.tencent_meeting_sim.presentation./g' "$file"
    sed -i 's/import com\.example\.tencentmeeting\.presenter\./import com.appsim.tencent_meeting_sim.presentation./g' "$file"
    sed -i 's/import com\.example\.tencentmeeting\.view\./import com.appsim.tencent_meeting_sim.presentation./g' "$file"

    # 更新Screen文件中的类名 (XxxPage -> XxxScreen)
    if [[ "$file" == *"Screen.kt" ]]; then
        basename=$(basename "$file" .kt)
        pagename="${basename/Screen/Page}"
        sed -i "s/class ${pagename}/class ${basename}/" "$file"
        sed -i "s/fun ${pagename}(/fun ${basename}(/" "$file"
    fi
done

echo "Presentation层迁移完成！"
echo "已处理的文件统计:"
echo "- Home模块: $(ls $SRC_BASE/$NEW_PKG/presentation/home/*.kt 2>/dev/null | wc -l) 个文件"
echo "- Meeting模块: $(ls $SRC_BASE/$NEW_PKG/presentation/meeting/*.kt 2>/dev/null | wc -l) 个文件"
echo "- Contact模块: $(ls $SRC_BASE/$NEW_PKG/presentation/contact/*.kt 2>/dev/null | wc -l) 个文件"
echo "- Me模块: $(ls $SRC_BASE/$NEW_PKG/presentation/me/*.kt 2>/dev/null | wc -l) 个文件"
