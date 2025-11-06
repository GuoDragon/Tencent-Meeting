'''
功能: 检查查看未进会议人员并重新邀请的功能
数据库位置: meeting_invitations.json, meeting_participants.json
'''

import json
import subprocess

def check_uninvited_participants(meetingId='meeting003'):
    """
    查找已邀请但未加入会议的人员

    参数:
        meetingId: 会议ID

    返回:
        未加入会议的用户ID列表
    """
    try:
        # 从Android模拟器获取meeting_invitations.json文件
        result = subprocess.run([
            r'D:\AndroidStudio\platform-tools\adb.exe',
            'exec-out',
            'run-as',
            'com.example.tencentmeeting',
            'cat',
            'files/meeting_invitations.json'
        ], stdout=open('meeting_invitations.json', 'w'), stderr=subprocess.PIPE)

        if result.returncode != 0:
            print(f"ADB命令执行失败(invitations): {result.stderr.decode('utf-8', errors='ignore')}")
            return []

        # 读取邀请记录
        with open('meeting_invitations.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: meeting_invitations.json文件为空")
                return []
            invitations = json.loads(content)

        # 获取该会议的所有被邀请人
        invited_users = [inv['inviteeId'] for inv in invitations if inv['meetingId'] == meetingId]

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
            return []

        # 读取参会人记录
        with open('meeting_participants.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: meeting_participants.json文件为空")
                return []
            participants = json.loads(content)

        # 获取已加入的用户
        joined_users = [p['userId'] for p in participants if p['meetingId'] == meetingId]

        # 找出已邀请但未加入的用户
        uninvited = [user_id for user_id in invited_users if user_id not in joined_users]

        return uninvited

    except Exception as e:
        print(f"Error: {e}")
        return []

def verify_reinvitation(meetingId, user_id):
    """
    验证某个用户是否被重新邀请(检查邀请记录)

    参数:
        meetingId: 会议ID
        user_id: 用户ID

    返回:
        bool: 如果该用户有邀请记录则返回True, 否则返回False
    """
    try:
        # 从Android模拟器获取meeting_invitations.json文件
        result = subprocess.run([
            r'D:\AndroidStudio\platform-tools\adb.exe',
            'exec-out',
            'run-as',
            'com.example.tencentmeeting',
            'cat',
            'files/meeting_invitations.json'
        ], stdout=open('meeting_invitations.json', 'w'), stderr=subprocess.PIPE)

        if result.returncode != 0:
            print(f"ADB命令执行失败: {result.stderr.decode('utf-8', errors='ignore')}")
            return False

        with open('meeting_invitations.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: meeting_invitations.json文件为空")
                return False
            invitations = json.loads(content)

        for inv in invitations:
            if inv['meetingId'] == meetingId and inv['inviteeId'] == user_id:
                return True
        return False

    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    # 测试示例: 查看meeting003中未加入的人员
    uninvited = check_uninvited_participants('meeting003')
    print(f"未加入会议的人员: {uninvited}")
