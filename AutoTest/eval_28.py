'''
功能: 检查通过会议号Meeting_5d8e21共享屏幕后,数据库是否正确记录
数据库位置: meeting_participants.json
'''

import json
import subprocess

def check_screen_sharing_by_meeting(userId='user001', meetingId='meeting_5d8e21', expected_sharing_status=True):
    """
    检查指定用户在指定会议中是否通过会议号共享屏幕

    参数:
        userId: 用户ID (默认为user001,即刘承龙)
        meetingId: 会议ID (默认为meeting_5d8e21)
        expected_sharing_status: 期望的屏幕共享状态 (True表示正在共享, False表示未共享)

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
    ], capture_output=True, text=True)

    # 检查ADB命令是否成功
    if result.returncode != 0:
        print(f"ADB命令执行失败: {result.stderr}")
        return False

    # 直接从stdout读取内容
    try:
        content = result.stdout
        if not content.strip():
            print("错误: 输出为空,可能ADB命令未正确执行或文件在模拟器中不存在")
            return False
        data = json.loads(content)
    except json.JSONDecodeError as e:
        print(f"JSON解析错误: {e}")
        print(f"原始内容前200字符: {content[:200]}")
        return False

    # 查找匹配的用户和会议记录
    try:
        for participant in data:
            if participant['userId'] == userId and participant['meetingId'] == meetingId:
                # 检查isSharingScreen字段是否与期望状态匹配
                if participant['isSharingScreen'] == expected_sharing_status:
                    print(f"成功: 用户{userId}在会议{meetingId}中的屏幕共享状态为{expected_sharing_status}")
                    return True
                else:
                    print(f"状态不匹配: 期望{expected_sharing_status}, 实际为{participant['isSharingScreen']}")
                    return False
        # 如果没有找到匹配的记录
        print(f"错误: 未找到用户{userId}在会议{meetingId}中的参会记录")
        return False
    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    # 测试示例: 检查user001(刘承龙)是否通过会议号meeting_5d8e21共享屏幕
    result = check_screen_sharing_by_meeting(
        userId='user001',
        meetingId='meeting_5d8e21',
        expected_sharing_status=True  # 期望正在共享屏幕
    )
    print(f"检测结果: {result}")
