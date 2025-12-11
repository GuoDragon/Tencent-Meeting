#!/bin/bash

BASE_DIR="D:/android_template/Tencent-Meeting/app/src/main/java/com/appsim/tencent_meeting_sim/presentation"

# 修复所有Contract文件中的model imports
# 将 .model. 替换为 .data.model.

find "$BASE_DIR" -name "*Contract.kt" -type f | while read file; do
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.model\./import com.appsim.tencent_meeting_sim.data.model./g' "$file"
  echo "Fixed: $file"
done

echo "All Contract imports fixed!"
