# coding: utf-8
'''
推送测试数据到Android模拟器
使用Python直接通过ADB shell推送JSON数据
'''

import subprocess
import json
import os

ADB_PATH = r'D:\AndroidStudio\platform-tools\adb.exe'
DATA_DIR = r'D:\android_template\Tencent-Meeting\app\src\main\assets\data'
PACKAGE_NAME = 'com.example.tencentmeeting'

JSON_FILES = [
    'meeting_participants.json',
    'meetings.json',
    'messages.json',
    'users.json',
    'personal_meeting_rooms.json',
    'hand_raise_records.json',
    'meeting_invitations.json'
]

def push_json_file(local_file, remote_filename):
    """
    通过ADB shell命令直接将JSON内容写入模拟器
    """
    try:
        # 读取本地JSON文件
        with open(local_file, 'r', encoding='utf-8') as f:
            json_content = f.read()

        # 压缩JSON (去除不必要的空格和换行)
        json_data = json.loads(json_content)
        compact_json = json.dumps(json_data, ensure_ascii=False, separators=(',', ':'))

        # 转义引号和特殊字符
        escaped_json = compact_json.replace('\\', '\\\\').replace('"', '\\"').replace('$', '\\$')

        # 通过echo命令写入文件
        shell_cmd = f'run-as {PACKAGE_NAME} sh -c \'echo "{escaped_json}" > files/{remote_filename}\''

        print(f"正在推送 {remote_filename}...", end='')

        result = subprocess.run([
            ADB_PATH,
            'shell',
            shell_cmd
        ], capture_output=True, text=True, timeout=30)

        if result.returncode != 0:
            print(f" [FAIL]")
            if result.stderr:
                print(f"  错误: {result.stderr}")
            return False

        # 验证文件是否写入成功
        verify_cmd = f'run-as {PACKAGE_NAME} sh -c \'test -f files/{remote_filename} && echo "OK"\''
        verify_result = subprocess.run([
            ADB_PATH,
            'shell',
            verify_cmd
        ], capture_output=True, text=True, timeout=10)

        if 'OK' in verify_result.stdout:
            print(f" [OK]")
            return True
        else:
            print(f" [FAIL] 文件未创建")
            return False

    except Exception as e:
        print(f" [FAIL]")
        print(f"  错误: {e}")
        return False

def main():
    print("=" * 60)
    print("推送测试数据到Android模拟器")
    print("=" * 60)

    # 检查ADB连接
    try:
        result = subprocess.run([ADB_PATH, 'devices'], capture_output=True, text=True, timeout=10)
        if 'device' not in result.stdout or 'emulator' not in result.stdout:
            print("[ERROR] 没有检测到Android设备")
            print("请确保:")
            print("  1. Android模拟器正在运行")
            print("  2. ADB可以访问设备")
            return
    except Exception as e:
        print(f"[ERROR] 无法运行ADB: {e}")
        return

    print("[OK] 检测到Android设备")
    print()

    success_count = 0
    fail_count = 0

    for json_file in JSON_FILES:
        local_path = os.path.join(DATA_DIR, json_file)

        if not os.path.exists(local_path):
            print(f"[SKIP] {json_file} (本地文件不存在)")
            continue

        if push_json_file(local_path, json_file):
            success_count += 1
        else:
            fail_count += 1

    print()
    print("=" * 60)
    print(f"推送完成: 成功 {success_count} 个, 失败 {fail_count} 个")
    print("=" * 60)

    if success_count > 0:
        print()
        print("现在可以运行测试脚本了, 例如:")
        print("  python eval_1.py")
        print("  python eval_2.py")

if __name__ == "__main__":
    main()
