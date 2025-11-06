'''
功能: 检查创建并直接进入快速会议后,数据库是否正确记录
数据库位置: meetings.json
'''

import json
import subprocess

def check_instant_meeting_created(meetingId='meeting001', hostId='user001'):
    """
    检查是否成功创建了快速会议(即时会议)并且状态为进行中

    参数:
        meetingId: 会议ID
        hostId: 主持人用户ID

    返回:
        bool: 如果找到匹配的快速会议且状态为ONGOING则返回True, 否则返回False
    """
    # 从Android模拟器获取文件
    result = subprocess.run([
        r'D:\AndroidStudio\platform-tools\adb.exe',
        'exec-out',
        'run-as',
        'com.example.tencentmeeting',
        'cat',
        'files/meetings.json'
    ], stdout=open('meetings.json', 'w'), stderr=subprocess.PIPE)

    # 检查ADB命令是否成功
    if result.returncode != 0:
        print(f"ADB命令执行失败: {result.stderr.decode('utf-8', errors='ignore')}")
        return False

    # 读取文件
    try:
        with open('meetings.json', 'r', encoding='utf-8') as f:
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

    # 查找匹配的会议记录
    try:
        for meeting in data:
            if (meeting['meetingId'] == meetingId and
                meeting['hostId'] == hostId and
                meeting['meetingType'] == 'INSTANT' and
                meeting['status'] == 'ONGOING'):
                return True
        # 如果没有找到匹配的记录
        return False
    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    # 测试示例: 检查user001是否创建了meeting001快速会议
    print(check_instant_meeting_created(
        meetingId='meeting001',
        hostId='user001'
    ))
