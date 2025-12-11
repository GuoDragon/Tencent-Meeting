#!/bin/bash

echo "=== 彻底清理项目构建缓存 ==="

cd "D:/android_template/Tencent-Meeting"

echo "1. 删除 .gradle 缓存目录..."
rm -rf .gradle

echo "2. 删除 app/build 目录..."
rm -rf app/build

echo "3. 删除 build 目录..."
rm -rf build

echo "4. 删除 .idea 目录 (Android Studio缓存)..."
rm -rf .idea

echo "5. 删除 *.iml 文件..."
find . -name "*.iml" -delete

echo "=== 清理完成! ==="
echo ""
echo "请在Android Studio中执行以下操作:"
echo "1. 重新打开项目 (File -> Open)"
echo "2. 等待Gradle同步完成"
echo "3. Build -> Rebuild Project"
echo "4. 从设备上卸载旧应用"
echo "5. Run -> Run 'app'"
