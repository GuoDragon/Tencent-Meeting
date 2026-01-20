'''
功能: 检查查看上一场会议记录及会议时长的功能
数据库位置: meetings.json
'''

import json
import subprocess

def check_meeting_duration(meetingId):
    """
    获取指定会议的时长信息

    参数:
        meetingId: 会议ID

    返回:
        会议时长(毫秒), 如果会议未结束或不存在则返回None
    """
    # 从Android模拟器获取文件
    result = subprocess.run([
        r'D:\AndroidStudio\platform-tools\adb.exe',
        'exec-out',
        'run-as',
        'com.example.tencent_meeting_sim',
        'cat',
        'files/meetings.json'
    ], stdout=open('meetings.json', 'w'), stderr=subprocess.PIPE)

    # 检查ADB命令是否成功
    if result.returncode != 0:
        print(f"ADB命令执行失败: {result.stderr.decode('utf-8', errors='ignore')}")
        return None

    # 读取文件
    try:
        with open('meetings.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: 文件为空,可能ADB命令未正确执行或文件在模拟器中不存在")
                return None
            data = json.loads(content)
    except json.JSONDecodeError as e:
        print(f"JSON解析错误: {e}")
        return None
    except FileNotFoundError:
        print("错误: 文件未找到")
        return None

    try:
        for meeting in data:
            if meeting['meetingId'] == meetingId:
                # 只有已结束的会议才能计算时长
                if meeting['status'] == 'ENDED' and meeting['endTime'] is not None:
                    duration = meeting['endTime'] - meeting['startTime']
                    return duration
                else:
                    return None
        return None
    except Exception as e:
        print(f"Error: {e}")
        return None

def check_previous_meeting_with_duration():
    """
    查找上一场会议并返回其记录和时长

    返回:
        包含会议信息和时长的字典, 或None
    """
    # 从Android模拟器获取文件
    result = subprocess.run([
        r'D:\AndroidStudio\platform-tools\adb.exe',
        'exec-out',
        'run-as',
        'com.example.tencent_meeting_sim',
        'cat',
        'files/meetings.json'
    ], stdout=open('meetings.json', 'w'), stderr=subprocess.PIPE)

    # 检查ADB命令是否成功
    if result.returncode != 0:
        print(f"ADB命令执行失败: {result.stderr.decode('utf-8', errors='ignore')}")
        return None

    # 读取文件
    try:
        with open('meetings.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: 文件为空,可能ADB命令未正确执行或文件在模拟器中不存在")
                return None
            data = json.loads(content)
    except json.JSONDecodeError as e:
        print(f"JSON解析错误: {e}")
        return None
    except FileNotFoundError:
        print("错误: 文件未找到")
        return None

    try:
        # 筛选出所有已结束的会议
        ended_meetings = [m for m in data if m['status'] == 'ENDED' and m['endTime'] is not None]

        if not ended_meetings:
            return None

        # 按结束时间排序,获取最近结束的会议
        previous_meeting = max(ended_meetings, key=lambda x: x['endTime'])

        # 计算时长
        duration = previous_meeting['endTime'] - previous_meeting['startTime']

        return {
            'meetingId': previous_meeting['meetingId'],
            'topic': previous_meeting['topic'],
            'duration': duration,
            'duration_minutes': duration / 60000  # 转换为分钟
        }

    except Exception as e:
        print(f"Error: {e}")
        return None

if __name__ == "__main__":
    # 测试示例: 查看上一场会议的记录和时长
    meeting_info = check_previous_meeting_with_duration()
    if meeting_info:
        print(f"会议ID: {meeting_info['meetingId']}")
        print(f"会议主题: {meeting_info['topic']}")
        print(f"会议时长: {meeting_info['duration_minutes']:.2f} 分钟")
