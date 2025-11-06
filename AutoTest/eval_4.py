'''
功能: 检查对某个参会人进行麦克风静音或解除静音操作后,数据库是否正确记录
数据库位置: meeting_participants.json
'''

import json
import subprocess

def check_participant_mute_control(target_userId='user002', meetingId='meeting001', expected_muted_status=True):
    """
    检查对指定参会人的麦克风静音控制是否正确记录

    参数:
        target_userId: 被控制的参会人用户ID
        meetingId: 会议ID
        expected_muted_status: 期望的静音状态 (True表示已静音, False表示未静音)

    返回:
        bool: 如果实际状态与期望状态匹配则返回True, 否则返回False
    """
    # 从Android模拟器获取文件
    result = subprocess.run([
        r'D:\AndroidStudio\platform-tools\adb.exe',
        'exec-out',
        'run-as',
        'com.example.tencentmeeting',
        'cat',
        'files/meeting_participants.json'
    ], stdout=open('meeting_participants.json', 'w'), stderr=subprocess.PIPE)

    # 检查ADB命令是否成功
    if result.returncode != 0:
        print(f"ADB命令执行失败: {result.stderr.decode('utf-8', errors='ignore')}")
        return False

    # 读取文件
    try:
        with open('meeting_participants.json', 'r', encoding='utf-8') as f:
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

    # 查找匹配的参会人记录
    try:
        for participant in data:
            if participant['userId'] == target_userId and participant['meetingId'] == meetingId:
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
    # 测试示例: 检查user002在meeting001中是否被静音
    print(check_participant_mute_control(
        target_userId='user002',
        meetingId='meeting001',
        expected_muted_status=True  # 期望该用户已被静音
    ))
