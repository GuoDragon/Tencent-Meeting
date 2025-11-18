'''
自动生成的麦克风测试脚本
基于虚拟机实际数据生成于: 2025-11-11 23:01:34
'''

import subprocess
import json

ADB_PATH = r'D:\AndroidStudio\platform-tools\adb.exe'
PACKAGE_NAME = 'com.example.tencentmeeting'

def check_microphone_muted(userId, meetingId, expected_muted_status):
    """检查麦克风静音状态"""
    result = subprocess.run([
        ADB_PATH,
        'exec-out',
        'run-as',
        PACKAGE_NAME,
        'cat',
        'files/meeting_participants.json'
    ], stdout=open('meeting_participants.json', 'w'), stderr=subprocess.PIPE)

    if result.returncode != 0:
        print(f"ADB命令执行失败: {result.stderr.decode('utf-8', errors='ignore')}")
        return False

    try:
        with open('meeting_participants.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: 文件为空")
                return False
            data = json.loads(content)
    except json.JSONDecodeError as e:
        print(f"JSON解析错误: {e}")
        return False
    except FileNotFoundError:
        print("错误: 文件未找到")
        return False

    for participant in data:
        if participant['userId'] == userId and participant['meetingId'] == meetingId:
            if participant['isMuted'] == expected_muted_status:
                print(f"[OK] 麦克风状态正确: {'isMuted': {expected_muted_status}}")
                return True
            else:
                print(f"[ERROR] 麦克风状态不匹配!")
                print(f"  期望: {'isMuted': {expected_muted_status}}")
                print(f"  实际: {'isMuted': {participant['isMuted']}}")
                return False

    print(f"[ERROR] 未找到用户 {userId} 在会议 {meetingId} 的记录")
    return False

if __name__ == "__main__":
    # 基于虚拟机实际数据的测试参数
    print("测试麦克风静音状态...")
    result = check_microphone_muted(
        userId='user001',
        meetingId='meeting_8f4a2b',
        expected_muted_status=False
    )
    print(f"\n测试结果: {result}")
