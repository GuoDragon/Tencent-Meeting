'''
功能: 检查在会议内发送指定内容的消息后,数据库是否正确记录
数据库位置: messages.json
'''

import json
import subprocess

def check_message_sent(meetingId='meeting_8f4a2b', senderId='user001', message_content='大家好'):
    """
    检查指定用户在指定会议中是否发送了指定内容的消息

    参数:
        meetingId: 会议ID
        senderId: 发送者用户ID
        message_content: 消息内容

    返回:
        bool: 如果找到匹配的消息记录则返回True, 否则返回False
    """
    # 从Android模拟器获取文件
    result = subprocess.run([
        r'D:\AndroidStudio\platform-tools\adb.exe',
        'exec-out',
        'run-as',
        'com.example.tencentmeeting',
        'cat',
        'files/messages.json'
    ], stdout=open('messages.json', 'w'), stderr=subprocess.PIPE)

    # 检查ADB命令是否成功
    if result.returncode != 0:
        print(f"ADB命令执行失败: {result.stderr.decode('utf-8', errors='ignore')}")
        return False

    # 读取文件
    try:
        with open('messages.json', 'r', encoding='utf-8') as f:
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

    # 查找匹配的消息记录
    try:
        for message in data:
            if (message['meetingId'] == meetingId and
                message['senderId'] == senderId and
                message['content'] == message_content):
                return True
        # 如果没有找到匹配的记录
        return False
    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    # 测试示例: 检查user001在meeting_8f4a2b中是否发送了"大家好"
    print(check_message_sent(
        meetingId='meeting_8f4a2b',
        senderId='user001',
        message_content='大家好'
    ))
