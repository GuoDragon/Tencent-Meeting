#!/bin/bash
cd "D:/android_template/Tencent-Meeting/app/src/main/java"

find "com/appsim/tencent_meeting_sim/presentation/meeting" -name "*.kt" -type f | while read file; do
    sed -i 's/package com\.example\.tencentmeeting\.contract/package com.appsim.tencent_meeting_sim.presentation.meeting/' "$file"
    sed -i 's/package com\.example\.tencentmeeting\.presenter/package com.appsim.tencent_meeting_sim.presentation.meeting/' "$file"
    sed -i 's/package com\.example\.tencentmeeting\.view/package com.appsim.tencent_meeting_sim.presentation.meeting/' "$file"
    sed -i 's/import com\.example\.tencentmeeting\.model\./import com.appsim.tencent_meeting_sim.data.model./g' "$file"
    sed -i 's/import com\.example\.tencentmeeting\.data\./import com.appsim.tencent_meeting_sim.data.repository./g' "$file"
    sed -i 's/import com\.example\.tencentmeeting\.ui\.theme\./import com.appsim.tencent_meeting_sim.ui.theme./g' "$file"
    sed -i 's/import com\.example\.tencentmeeting\.contract\./import com.appsim.tencent_meeting_sim.presentation.meeting./g' "$file"
    sed -i 's/import com\.example\.tencentmeeting\.presenter\./import com.appsim.tencent_meeting_sim.presentation.meeting./g' "$file"

    if [[ "$file" == *"Screen.kt" ]]; then
        bname=$(basename "$file" .kt)
        pname="${bname/Screen/Page}"
        sed -i "s/class ${pname}/class ${bname}/" "$file"
        sed -i "s/fun ${pname}(/fun ${bname}(/" "$file"
    fi
done

echo "Meeting模块package更新完成"
