'''
功能: 检查将个人会议室的等候室设置为开启,并进入会议室后,数据库是否正确记录
数据库位置: personal_meeting_rooms.json, meeting_participants.json
'''

import json
import subprocess

def check_waiting_room_enabled_and_entered(userId='user001'):
    """
    检查指定用户的个人会议室等候室是否开启,并且用户是否已进入会议室

    参数:
        userId: 用户ID (默认为user001,即刘承龙)

    返回:
        bool: 如果等候室已开启且用户已进入会议室则返回True, 否则返回False
    """
    # 第一步: 检查个人会议室的等候室设置
    result1 = subprocess.run([
        r'D:\AndroidStudio\platform-tools\adb.exe',
        'exec-out',
        'run-as',
        'com.example.tencent_meeting_sim',
        'cat',
        'files/personal_meeting_rooms.json'
    ], capture_output=True, text=True)

    # 检查ADB命令是否成功
    if result1.returncode != 0:
        print(f"ADB命令执行失败(personal_meeting_rooms): {result1.stderr}")
        return False

    # 解析个人会议室数据
    try:
        content1 = result1.stdout
        if not content1.strip():
            print("错误: personal_meeting_rooms.json输出为空")
            return False
        rooms_data = json.loads(content1)
    except json.JSONDecodeError as e:
        print(f"JSON解析错误(personal_meeting_rooms): {e}")
        print(f"原始内容前200字符: {content1[:200]}")
        return False

    # 查找用户的个人会议室
    user_room = None
    personal_meeting_id = None
    try:
        for room in rooms_data:
            if room['userId'] == userId:
                user_room = room
                personal_meeting_id = room['meetingId']
                break

        if user_room is None:
            print(f"错误: 未找到用户{userId}的个人会议室")
            return False

        # 检查等候室是否开启
        if not user_room.get('enableWaitingRoom', False):
            print(f"等候室未开启: enableWaitingRoom = {user_room.get('enableWaitingRoom')}")
            return False

        print(f"等候室已开启: 个人会议号 {personal_meeting_id}")

    except Exception as e:
        print(f"Error processing personal_meeting_rooms: {e}")
        return False

    # 第二步: 检查用户是否进入了会议室
    result2 = subprocess.run([
        r'D:\AndroidStudio\platform-tools\adb.exe',
        'exec-out',
        'run-as',
        'com.example.tencent_meeting_sim',
        'cat',
        'files/meeting_participants.json'
    ], capture_output=True, text=True)

    # 检查ADB命令是否成功
    if result2.returncode != 0:
        print(f"ADB命令执行失败(meeting_participants): {result2.stderr}")
        return False

    # 解析参会人数据
    try:
        content2 = result2.stdout
        if not content2.strip():
            print("错误: meeting_participants.json输出为空")
            return False
        participants_data = json.loads(content2)
    except json.JSONDecodeError as e:
        print(f"JSON解析错误(meeting_participants): {e}")
        print(f"原始内容前200字符: {content2[:200]}")
        return False

    # 查找用户在个人会议室中的参会记录
    try:
        for participant in participants_data:
            if participant['userId'] == userId and participant['meetingId'] == personal_meeting_id:
                print(f"成功: 用户{userId}已进入个人会议室{personal_meeting_id}")
                return True

        # 如果没有找到参会记录
        print(f"错误: 用户{userId}尚未进入个人会议室{personal_meeting_id}")
        return False

    except Exception as e:
        print(f"Error processing meeting_participants: {e}")
        return False

if __name__ == "__main__":
    # 测试示例: 检查user001(刘承龙)的个人会议室等候室是否开启并已进入
    result = check_waiting_room_enabled_and_entered(userId='user001')
    print(f"检测结果: {result}")
