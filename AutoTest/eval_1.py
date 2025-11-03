'''
功能: 检查用户在会议中关闭麦克风时,数据库是否正确记录了"自身麦克风已静音"的状态
数据库位置: meeting_participants.json
'''

import json
import os

def check_microphone_muted(userId, meetingId, expected_muted_status=True):
    """
    检查指定用户在指定会议中的麦克风静音状态

    参数:
        userId: 用户ID
        meetingId: 会议ID
        expected_muted_status: 期望的静音状态 (True表示已静音, False表示未静音)

    返回:
        bool: 如果实际状态与期望状态匹配则返回True, 否则返回False
    """
    # 数据文件路径
    data_file_path = r'D:\android_template\Tencent-Meeting\app\src\main\assets\data\meeting_participants.json'

    # 读取文件
    with open(data_file_path, 'r', encoding='utf-8') as f:
        data = json.load(f)

    # 查找匹配的用户和会议记录
    try:
        for participant in data:
            if participant['userId'] == userId and participant['meetingId'] == meetingId:
                # 检查isMuted字段是否与期望状态匹配
                if participant['isMuted'] == expected_muted_status:
                    return True
                else:
                    return False
        # 如果没有找到匹配的记录
        return False
    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    # 测试示例: 检查user002在meeting001中是否已静音
    print(check_microphone_muted(
        userId='user002',
        meetingId='meeting001',
        expected_muted_status=True  # 期望麦克风已静音
    ))
