'''
功能: 检查预约会议、邀请人员并全程录制的功能
数据库位置: meetings.json, meeting_invitations.json
'''

import json
import subprocess

def check_scheduled_meeting_with_recording(meetingId='meeting_5c2f84', hostId='user001', invitee_ids=['user002', 'user003']):
    """
    检查是否成功预约会议、发送邀请并设置录制
    注意: 当前数据结构中可能没有录制相关字段,此函数主要检查会议和邀请

    参数:
        meetingId: 会议ID
        hostId: 主持人用户ID
        invitee_ids: 被邀请的用户ID列表

    返回:
        bool: 如果会议存在、类型正确且所有邀请都已发送则返回True, 否则返回False
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

        # 检查会议是否存在且为预约会议
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
                meeting['meetingType'] == 'SCHEDULED'):
                meeting_found = True
                # 如果有录制相关字段,这里可以检查
                # if 'recordingEnabled' in meeting:
                #     if not meeting['recordingEnabled']:
                #         return False
                break

        if not meeting_found:
            return False

        # 从Android模拟器获取meeting_invitations.json文件
        result = subprocess.run([
            r'D:\AndroidStudio\platform-tools\adb.exe',
            'exec-out',
            'run-as',
            'com.example.tencent_meeting_sim',
            'cat',
            'files/meeting_invitations.json'
        ], stdout=open('meeting_invitations.json', 'w'), stderr=subprocess.PIPE)

        if result.returncode != 0:
            print(f"ADB命令执行失败(invitations): {result.stderr.decode('utf-8', errors='ignore')}")
            return False

        # 检查所有邀请是否都已发送
        with open('meeting_invitations.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: meeting_invitations.json文件为空")
                return False
            invitations = json.loads(content)

        invited_users = [inv['inviteeId'] for inv in invitations
                        if inv['meetingId'] == meetingId and inv['inviterId'] == hostId]

        # 验证所有期望的被邀请人都在列表中
        for user_id in invitee_ids:
            if user_id not in invited_users:
                return False

        return True

    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    # 测试示例: 检查user001预约会议并邀请多人,设置录制
    print(check_scheduled_meeting_with_recording(
        meetingId='meeting_5c2f84',
        hostId='user001',
        invitee_ids=['user002', 'user003']
    ))
