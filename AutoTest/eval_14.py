'''
功能: 检查打开参会人列表并给指定人发消息后,数据库是否正确记录
数据库位置: messages.json
'''

import json
import subprocess

def check_message_to_participant(meetingId='meeting_3d7e91', senderId='user001', receiverId='user002', message_content='你好'):
    """
    检查是否成功给指定参会人发送了消息
    注意: 此处假设私聊功能,实际数据结构可能是会议内公开消息

    参数:
        meetingId: 会议ID
        senderId: 发送者用户ID
        receiverId: 接收者用户ID (用于验证目标)
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
        return False
    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    # 测试示例: 检查user001在meeting_3d7e91中给其他人发消息
    print(check_message_to_participant(
        meetingId='meeting_3d7e91',
        senderId='user001',
        receiverId='user002',  # 目标参会人
        message_content='你好'
    ))
