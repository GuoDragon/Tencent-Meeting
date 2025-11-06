'''
功能: 检查通过会议邀请链接加入会议后,数据库是否正确记录参会信息
数据库位置: meetings.json, meeting_participants.json
'''

import json
import subprocess

def check_join_meeting_by_link(meetingLink='meeting.tencent.com/p/4157555988', userId='user001'):
    """
    检查用户是否通过邀请链接成功加入会议

    参数:
        meetingLink: 会议邀请链接
        userId: 加入会议的用户ID

    返回:
        bool: 如果通过链接找到会议且用户已加入则返回True, 否则返回False
    """
    try:
        # 从Android模拟器获取personal_meeting_rooms.json文件
        result = subprocess.run([
            r'D:\AndroidStudio\platform-tools\adb.exe',
            'exec-out',
            'run-as',
            'com.example.tencentmeeting',
            'cat',
            'files/personal_meeting_rooms.json'
        ], stdout=open('personal_meeting_rooms.json', 'w'), stderr=subprocess.PIPE)

        if result.returncode != 0:
            print(f"ADB命令执行失败(personal_rooms): {result.stderr.decode('utf-8', errors='ignore')}")
            return False

        # 读取个人会议室信息
        with open('personal_meeting_rooms.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: personal_meeting_rooms.json文件为空")
                return False
            personal_rooms = json.loads(content)

        # 通过链接找到对应的会议ID
        meeting_id = None
        for room in personal_rooms:
            if room['meetingLink'] == meetingLink:
                meeting_id = room['meetingId']
                break

        if not meeting_id:
            return False

        # 从Android模拟器获取meeting_participants.json文件
        result = subprocess.run([
            r'D:\AndroidStudio\platform-tools\adb.exe',
            'exec-out',
            'run-as',
            'com.example.tencentmeeting',
            'cat',
            'files/meeting_participants.json'
        ], stdout=open('meeting_participants.json', 'w'), stderr=subprocess.PIPE)

        if result.returncode != 0:
            print(f"ADB命令执行失败(participants): {result.stderr.decode('utf-8', errors='ignore')}")
            return False

        # 读取参会人信息
        with open('meeting_participants.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: meeting_participants.json文件为空")
                return False
            participants = json.loads(content)

        # 检查用户是否在参会人列表中
        for participant in participants:
            if participant['userId'] == userId and participant['meetingId'] == meeting_id:
                return True

        return False

    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    # 测试示例: 检查用户是否通过链接加入会议
    print(check_join_meeting_by_link(
        meetingLink='meeting.tencent.com/p/4157555988',
        userId='user001'
    ))
