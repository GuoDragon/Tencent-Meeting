import json
from datetime import datetime

def timestamp_to_date(ts):
    """将时间戳转换为可读日期"""
    if ts is None:
        return "null"
    return datetime.fromtimestamp(ts / 1000).strftime('%Y-%m-%d %H:%M:%S')

def fix_timestamps(input_file, output_file):
    with open(input_file, 'r', encoding='utf-8') as f:
        meetings = json.load(f)

    fixed_count = 0
    print("=" * 80)
    print("开始修复时间戳问题...")
    print("=" * 80)

    for meeting in meetings:
        modified = False
        meeting_id = meeting.get('meetingId', 'unknown')
        topic = meeting.get('topic', 'unknown')

        # 修正2026年的 startTime（减3年）
        if meeting['startTime'] > 1735689600000:  # 2025-01-01 之后
            old_start = meeting['startTime']
            if meeting['startTime'] > 1767225600000:  # 2026-01-01 之后
                meeting['startTime'] -= (3 * 365 * 24 * 60 * 60 * 1000)
                modified = True
                print(f"[{meeting_id}] {topic}")
                print(f"  修正 startTime: {timestamp_to_date(old_start)} -> {timestamp_to_date(meeting['startTime'])}")
            elif meeting['startTime'] > 1735689600000:  # 2025年
                meeting['startTime'] -= (2 * 365 * 24 * 60 * 60 * 1000)
                modified = True
                print(f"[{meeting_id}] {topic}")
                print(f"  修正 startTime: {timestamp_to_date(old_start)} -> {timestamp_to_date(meeting['startTime'])}")

        # 修正倒置的会议（startTime > endTime）
        if meeting.get('endTime') is not None:
            if meeting['startTime'] > meeting['endTime']:
                old_start = meeting['startTime']
                # 将 startTime 设为 endTime 前1小时
                meeting['startTime'] = meeting['endTime'] - (60 * 60 * 1000)
                modified = True
                print(f"[{meeting_id}] {topic}")
                print(f"  修正时间倒置: startTime {timestamp_to_date(old_start)} -> {timestamp_to_date(meeting['startTime'])}")
                print(f"               endTime   {timestamp_to_date(meeting['endTime'])}")

        # 为 ENDED 会议补充 endTime
        if meeting.get('status') == 'ENDED' and meeting.get('endTime') is None:
            meeting['endTime'] = meeting['startTime'] + (60 * 60 * 1000)  # +1小时
            modified = True
            print(f"[{meeting_id}] {topic}")
            print(f"  补充 endTime: {timestamp_to_date(meeting['endTime'])}")

        if modified:
            fixed_count += 1

    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(meetings, f, ensure_ascii=False, indent=2)

    print("=" * 80)
    print(f"修复完成！总共修正了 {fixed_count} 个会议")
    print("=" * 80)

    # 验证修复结果
    print("\n验证修复结果...")
    print("=" * 80)

    issue_count = 0

    # 检查是否还有2025/2026年的时间戳
    future_meetings = [m for m in meetings if m['startTime'] > 1735689600000]  # 2025-01-01之后
    if future_meetings:
        print(f"警告：还有 {len(future_meetings)} 个会议的 startTime 在2025年或之后")
        issue_count += len(future_meetings)
    else:
        print("OK: 所有会议的 startTime 都在2025年之前")

    # 检查是否还有时间倒置
    inverted_meetings = [m for m in meetings if m.get('endTime') is not None and m['startTime'] > m['endTime']]
    if inverted_meetings:
        print(f"警告：还有 {len(inverted_meetings)} 个会议的 startTime > endTime")
        issue_count += len(inverted_meetings)
    else:
        print("OK: 所有会议的 startTime <= endTime")

    # 检查 ENDED 状态是否都有 endTime
    ended_without_endtime = [m for m in meetings if m.get('status') == 'ENDED' and m.get('endTime') is None]
    if ended_without_endtime:
        print(f"警告：还有 {len(ended_without_endtime)} 个 ENDED 状态的会议没有 endTime")
        issue_count += len(ended_without_endtime)
    else:
        print("OK: 所有 ENDED 状态的会议都有 endTime")

    print("=" * 80)
    if issue_count == 0:
        print("OK: 所有问题已修复！")
    else:
        print(f"警告: 还有 {issue_count} 个问题需要处理")
    print("=" * 80)

if __name__ == '__main__':
    input_file = 'D:/android_template/Tencent-Meeting/app/src/main/assets/data/meetings.json'
    output_file = input_file
    fix_timestamps(input_file, output_file)
