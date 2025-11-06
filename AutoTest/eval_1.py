'''
功能: 检查用户在会议中关闭麦克风时,数据库是否正确记录了"自身麦克风已静音"的状态
数据库位置: meeting_participants.json
'''

import json
import subprocess

def check_microphone_muted(userId='user001', meetingId='meeting_8f4a2b', expected_muted_status=True):
    """
    检查指定用户在指定会议中的麦克风静音状态

    参数:
        userId: 用户ID
        meetingId: 会议ID
        expected_muted_status: 期望的静音状态 (True表示已静音, False表示未静音)

    返回:
        bool: 如果实际状态与期望状态匹配则返回True, 否则返回False
    """
    # 从Android模拟器获取文件
    result = subprocess.run([
        r'D:\AndroidStudio\platform-tools\adb.exe',
        'exec-out',
        'run-as',
        'com.example.tencentmeeting',
        'cat',
        'files/meeting_participants.json'
    ], stdout=open('meeting_participants.json', 'w'), stderr=subprocess.PIPE)

    # 检查ADB命令是否成功
    if result.returncode != 0:
        print(f"ADB命令执行失败: {result.stderr.decode('utf-8', errors='ignore')}")
        return False

    # 读取文件
    try:
        with open('meeting_participants.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: 文件为空,可能ADB命令未正确执行或文件在模拟器中不存在")
                return False
            data = json.loads(content)
    except json.JSONDecodeError as e:
        print(f"JSON解析错误: {e}")
        return False
    except FileNotFoundError:
        print("错误: 文件未找到")
        return False

    # 查找匹配的用户和会议记录
    try:
        for participant in data:
            if participant['userId'] == userId and participant['meetingId'] == meetingId:
                # 检查isMuted字段是否与期望状态匹配
                if participant['isMuted'] == expected_muted_status:
                    return True
                else:
                    return False
        # 如果没有找到匹配的记录
        return False
    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    # 测试示例: 检查user002在meeting_8f4a2b中是否已静音
    print(check_microphone_muted(
        userId='user002',
        meetingId='meeting_8f4a2b',
        expected_muted_status=True  # 期望麦克风已静音
    ))
