'''
功能: 检查用户在会议中开启自身摄像头时,数据库是否正确记录了摄像头开启状态
数据库位置: meeting_participants.json
'''

import json
import subprocess

def check_camera_on(userId='user001', meetingId='meeting_8f4a2b', expected_camera_status=True):
    """
    检查指定用户在指定会议中的摄像头开启状态

    参数:
        userId: 用户ID
        meetingId: 会议ID
        expected_camera_status: 期望的摄像头状态 (True表示已开启, False表示已关闭)

    返回:
        bool: 如果实际状态与期望状态匹配则返回True, 否则返回False
    """
    # 从Android模拟器获取文件
    result = subprocess.run([
        r'D:\AndroidStudio\platform-tools\adb.exe',
        'exec-out',
        'run-as',
        'com.example.tencent_meeting_sim',
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
                # 检查isCameraOn字段是否与期望状态匹配
                if participant['isCameraOn'] == expected_camera_status:
                    return True
                else:
                    return False
        # 如果没有找到匹配的记录
        return False
    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    # 测试示例: 检查user001在meeting_8f4a2b中摄像头是否已开启
    print(check_camera_on(
        userId='user001',
        meetingId='meeting_8f4a2b',
        expected_camera_status=True  # 期望摄像头已开启
    ))
