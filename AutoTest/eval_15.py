'''
功能: 检查与指定人创建并进入快速会议后,数据库是否正确记录
数据库位置: meetings.json, meeting_participants.json
'''

import json
import subprocess

def check_instant_meeting_with_participants(meetingId='meeting_3d7e91', hostId='user001', participant_ids=['user001', 'user002', 'user003']):
    """
    检查是否成功创建快速会议并且指定的参会人都已加入

    参数:
        meetingId: 会议ID
        hostId: 主持人用户ID
        participant_ids: 期望参会的用户ID列表

    返回:
        bool: 如果会议存在、类型正确且所有指定用户都已加入则返回True, 否则返回False
    """
    try:
        # 从Android模拟器获取meetings.json文件
        result = subprocess.run([
            r'D:\AndroidStudio\platform-tools\adb.exe',
            'exec-out',
            'run-as',
            'com.example.tencent_meeting_sim',
            'cat',
            'files/meetings.json'
        ], stdout=open('meetings.json', 'w'), stderr=subprocess.PIPE)

        if result.returncode != 0:
            print(f"ADB命令执行失败(meetings): {result.stderr.decode('utf-8', errors='ignore')}")
            return False

        # 检查会议是否存在且为快速会议
        with open('meetings.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: meetings.json文件为空")
                return False
            meetings = json.loads(content)

        meeting_found = False
        for meeting in meetings:
            if (meeting['meetingId'] == meetingId and
                meeting['hostId'] == hostId and
                meeting['meetingType'] == 'INSTANT' and
                meeting['status'] == 'ONGOING'):
                meeting_found = True
                break

        if not meeting_found:
            return False

        # 从Android模拟器获取meeting_participants.json文件
        result = subprocess.run([
            r'D:\AndroidStudio\platform-tools\adb.exe',
            'exec-out',
            'run-as',
            'com.example.tencent_meeting_sim',
            'cat',
            'files/meeting_participants.json'
        ], stdout=open('meeting_participants.json', 'w'), stderr=subprocess.PIPE)

        if result.returncode != 0:
            print(f"ADB命令执行失败(participants): {result.stderr.decode('utf-8', errors='ignore')}")
            return False

        # 检查所有指定用户是否都已加入
        with open('meeting_participants.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: meeting_participants.json文件为空")
                return False
            participants = json.loads(content)

        joined_users = [p['userId'] for p in participants if p['meetingId'] == meetingId]

        # 验证所有期望的参会人都在列表中
        for user_id in participant_ids:
            if user_id not in joined_users:
                return False

        return True

    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    # 测试示例: 检查user001与user002、user003创建快速会议
    print(check_instant_meeting_with_participants(
        meetingId='meeting_3d7e91',
        hostId='user001',
        participant_ids=['user001', 'user002', 'user003']  # 包含主持人和邀请的参会人
    ))
