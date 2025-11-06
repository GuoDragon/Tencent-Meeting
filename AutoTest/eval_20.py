'''
功能: 检查打开通讯录、邀请联系人并复制邀请链接的功能
数据库位置: users.json, meetings.json, personal_meeting_rooms.json
'''

import json
import subprocess

def check_contact_and_invitation_link(meetingId='341234546'):
    """
    获取会议的邀请链接

    参数:
        meetingId: 会议ID

    返回:
        会议邀请链接, 如果不存在则返回None
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
            print(f"ADB命令执行失败: {result.stderr.decode('utf-8', errors='ignore')}")
            return None

        # 从个人会议室中查找邀请链接
        with open('personal_meeting_rooms.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: personal_meeting_rooms.json文件为空")
                return None
            rooms = json.loads(content)

        for room in rooms:
            if room['meetingId'] == meetingId:
                return room['meetingLink']

        return None

    except Exception as e:
        print(f"Error: {e}")
        return None

def check_contacts_list():
    """
    获取通讯录中的所有联系人

    返回:
        联系人列表
    """
    try:
        # 从Android模拟器获取users.json文件
        result = subprocess.run([
            r'D:\AndroidStudio\platform-tools\adb.exe',
            'exec-out',
            'run-as',
            'com.example.tencentmeeting',
            'cat',
            'files/users.json'
        ], stdout=open('users.json', 'w'), stderr=subprocess.PIPE)

        if result.returncode != 0:
            print(f"ADB命令执行失败: {result.stderr.decode('utf-8', errors='ignore')}")
            return []

        with open('users.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: users.json文件为空")
                return []
            users = json.loads(content)

        contacts = [{'userId': u['userId'], 'username': u['username']} for u in users]
        return contacts

    except Exception as e:
        print(f"Error: {e}")
        return []

def verify_invitation_link_copied(meetingId, expected_link):
    """
    验证复制的邀请链接是否正确

    参数:
        meetingId: 会议ID
        expected_link: 期望的链接

    返回:
        bool: 如果链接匹配则返回True, 否则返回False
    """
    actual_link = check_contact_and_invitation_link(meetingId)
    return actual_link == expected_link

if __name__ == "__main__":
    # 测试示例: 获取通讯录并复制会议邀请链接
    contacts = check_contacts_list()
    print(f"通讯录中有 {len(contacts)} 个联系人")

    # 获取会议邀请链接
    link = check_contact_and_invitation_link('341234546')
    if link:
        print(f"会议邀请链接: {link}")
    else:
        print("未找到邀请链接")
