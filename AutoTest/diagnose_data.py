'''
诊断脚本：检查虚拟机中的数据文件状态
用途：
1. 检查所有 JSON 数据文件是否存在
2. 显示每个文件的内容
3. 分析数据是否符合预期
'''

import subprocess
import json
import os

ADB_PATH = r'D:\AndroidStudio\platform-tools\adb.exe'
PACKAGE_NAME = 'com.example.tencentmeeting'

# 需要检查的数据文件列表
JSON_FILES = [
    'meeting_participants.json',
    'meetings.json',
    'messages.json',
    'users.json',
    'personal_meeting_rooms.json',
    'hand_raise_records.json',
    'meeting_invitations.json'
]

def check_adb_connection():
    """检查 ADB 连接状态"""
    print("=" * 60)
    print("1. 检查 ADB 连接")
    print("=" * 60)
    result = subprocess.run([ADB_PATH, 'devices'], capture_output=True, text=True)
    print(result.stdout)
    if 'device' not in result.stdout or result.stdout.count('\n') < 2:
        print("[ERROR] 未检测到 Android 设备")
        return False
    print("[OK] ADB 连接正常\n")
    return True

def pull_json_file(filename):
    """从虚拟机拉取 JSON 文件"""
    local_file = f"debug_{filename}"

    # 使用 run-as 读取文件并保存到本地
    result = subprocess.run([
        ADB_PATH,
        'exec-out',
        'run-as',
        PACKAGE_NAME,
        'cat',
        f'files/{filename}'
    ], stdout=open(local_file, 'w', encoding='utf-8'), stderr=subprocess.PIPE)

    if result.returncode != 0:
        error_msg = result.stderr.decode('utf-8', errors='ignore')
        return None, f"读取失败: {error_msg}"

    # 读取本地文件
    try:
        with open(local_file, 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                return None, "文件为空"
            data = json.loads(content)
            return data, None
    except json.JSONDecodeError as e:
        return None, f"JSON 解析错误: {e}"
    except Exception as e:
        return None, f"读取错误: {e}"

def analyze_file(filename, data):
    """分析文件数据"""
    print(f"\n{'='*60}")
    print(f"文件: {filename}")
    print(f"{'='*60}")

    if isinstance(data, list):
        print(f"[OK] 类型: 数组")
        print(f"[OK] 记录数: {len(data)}")

        if len(data) > 0:
            print(f"\n前 3 条记录:")
            for i, record in enumerate(data[:3], 1):
                print(f"\n  记录 {i}:")
                if isinstance(record, dict):
                    for key, value in record.items():
                        # 截断长字符串
                        if isinstance(value, str) and len(value) > 50:
                            value = value[:50] + "..."
                        print(f"    {key}: {value}")
                else:
                    print(f"    {record}")
        else:
            print("[WARNING] 数组为空")
    elif isinstance(data, dict):
        print(f"[OK] 类型: 对象")
        print(f"[OK] 字段数: {len(data)}")
        print(f"\n内容:")
        for key, value in data.items():
            if isinstance(value, str) and len(value) > 50:
                value = value[:50] + "..."
            print(f"  {key}: {value}")
    else:
        print(f"[OK] 类型: {type(data)}")
        print(f"[OK] 值: {data}")

def check_data_consistency():
    """检查数据一致性"""
    print(f"\n{'='*60}")
    print(f"3. 数据一致性分析")
    print(f"{'='*60}")

    # 读取关键文件
    participants_data, _ = pull_json_file('meeting_participants.json')
    meetings_data, _ = pull_json_file('meetings.json')

    if participants_data and meetings_data:
        # 提取所有会议 ID
        meeting_ids_in_meetings = set()
        if isinstance(meetings_data, list):
            meeting_ids_in_meetings = {m.get('meetingId') for m in meetings_data if isinstance(m, dict)}

        meeting_ids_in_participants = set()
        if isinstance(participants_data, list):
            meeting_ids_in_participants = {p.get('meetingId') for p in participants_data if isinstance(p, dict)}

        print(f"\nmeetings.json 中的会议 ID: {meeting_ids_in_meetings}")
        print(f"meeting_participants.json 中的会议 ID: {meeting_ids_in_participants}")

        # 检查不匹配的会议
        only_in_meetings = meeting_ids_in_meetings - meeting_ids_in_participants
        only_in_participants = meeting_ids_in_participants - meeting_ids_in_meetings

        if only_in_meetings:
            print(f"\n[WARNING] 只在 meetings.json 中存在的会议: {only_in_meetings}")
            print("   原因：这些会议没有参会人记录，操作不会被保存")

        if only_in_participants:
            print(f"\n[WARNING] 只在 meeting_participants.json 中存在的会议: {only_in_participants}")
            print("   原因：这些参会人记录对应的会议不存在")

        if not only_in_meetings and not only_in_participants:
            print("\n[OK] 数据一致性良好")

def main():
    """主函数"""
    print("\n" + "="*60)
    print("腾讯会议 - 数据文件诊断工具")
    print("="*60 + "\n")

    # 1. 检查 ADB 连接
    if not check_adb_connection():
        return

    # 2. 检查所有数据文件
    print("="*60)
    print("2. 检查数据文件")
    print("="*60)

    for filename in JSON_FILES:
        data, error = pull_json_file(filename)
        if error:
            print(f"\n[ERROR] {filename}: {error}")
        else:
            analyze_file(filename, data)

    # 3. 检查数据一致性
    check_data_consistency()

    # 清理临时文件
    print(f"\n{'='*60}")
    print("4. 清理临时文件")
    print("="*60)
    for filename in JSON_FILES:
        local_file = f"debug_{filename}"
        if os.path.exists(local_file):
            os.remove(local_file)
            print(f"[OK] 已删除: {local_file}")

    print(f"\n{'='*60}")
    print("诊断完成！")
    print("="*60)

if __name__ == "__main__":
    main()
