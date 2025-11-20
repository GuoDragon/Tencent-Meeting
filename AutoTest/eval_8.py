'''
功能: 检查播放上一场会议回放时,是否能正确找到上一场已结束的会议
数据库位置: meetings.json
'''

import json
import subprocess

def check_previous_meeting_playback(expected_meeting_id=None):
    """
    查找上一场(最近一场)已结束的会议,用于播放回放

    参数:
        expected_meeting_id: 期望的会议ID (可选,用于验证查找结果是否正确)

    返回:
        如果提供了expected_meeting_id: 返回bool (True表示找到的会议ID匹配期望值)
        如果未提供expected_meeting_id: 返回上一场会议的完整信息或None
    """
    # 从Android模拟器获取文件
    result = subprocess.run([
        r'D:\AndroidStudio\platform-tools\adb.exe',
        'exec-out',
        'run-as',
        'com.example.tencentmeeting',
        'cat',
        'files/meetings.json'
    ], capture_output=True, text=True)

    # 检查ADB命令是否成功
    if result.returncode != 0:
        print(f"ADB命令执行失败: {result.stderr}")
        return None if expected_meeting_id is None else False

    # 直接从stdout读取内容
    content = result.stdout
    if not content.strip():
        print("错误: 输出为空,可能ADB命令未正确执行或文件在模拟器中不存在")
        return None if expected_meeting_id is None else False

    # 解析JSON
    try:
        data = json.loads(content)
    except json.JSONDecodeError as e:
        print(f"JSON解析错误: {e}")
        print(f"原始内容前200字符: {content[:200]}")
        return None if expected_meeting_id is None else False

    try:
        # 筛选出所有已结束的会议
        ended_meetings = [m for m in data if m['status'] == 'ENDED' and m['endTime'] is not None]

        if not ended_meetings:
            return None if expected_meeting_id is None else False

        # 按结束时间排序,获取最近结束的会议
        previous_meeting = max(ended_meetings, key=lambda x: x['endTime'])

        # 如果提供了期望的会议ID,进行验证
        if expected_meeting_id is not None:
            return previous_meeting['meetingId'] == expected_meeting_id

        # 否则返回会议完整信息
        return previous_meeting

    except Exception as e:
        print(f"Error: {e}")
        return None if expected_meeting_id is None else False

if __name__ == "__main__":
    # 测试示例: 查找上一场会议
    previous_meeting = check_previous_meeting_playback()
    if previous_meeting:
        print(f"上一场会议ID: {previous_meeting['meetingId']}")
        print(f"会议主题: {previous_meeting['topic']}")
