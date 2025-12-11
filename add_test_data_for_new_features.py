import json
import random

def add_test_data(input_file, output_file):
    # 读取现有数据
    with open(input_file, 'r', encoding='utf-8') as f:
        meetings = json.load(f)

    ongoing_meetings = []

    # 处理每个会议
    for meeting in meetings:
        # 1. 添加 isLocked 字段（如果不存在）
        if 'isLocked' not in meeting:
            meeting['isLocked'] = False

        # 2. 添加 isRecording 和 recordingStartTime 字段
        status = meeting.get('status', 'ENDED')

        if status == 'ONGOING':
            ongoing_meetings.append(meeting)
            # 30% 的进行中会议正在录制
            if random.random() < 0.3:
                meeting['isRecording'] = True
                # 录制开始时间 = 会议开始时间 + 5分钟
                meeting['recordingStartTime'] = meeting['startTime'] + (5 * 60 * 1000)
            else:
                meeting['isRecording'] = False
                meeting['recordingStartTime'] = None
        else:
            # ENDED 和 SCHEDULED 会议不录制
            meeting['isRecording'] = False
            meeting['recordingStartTime'] = None

        # 3. 确保 settings.allowParticipantUnmute 存在
        if 'settings' not in meeting:
            meeting['settings'] = {}

        if 'allowParticipantUnmute' not in meeting['settings']:
            # 60% 允许自解除静音
            meeting['settings']['allowParticipantUnmute'] = random.random() < 0.6

    # 可选：随机锁定 2-3 个进行中的会议
    if ongoing_meetings:
        num_to_lock = min(3, max(2, len(ongoing_meetings) // 10))
        meetings_to_lock = random.sample(ongoing_meetings, num_to_lock)
        for meeting in meetings_to_lock:
            meeting['isLocked'] = True

    # 保存修改后的数据
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(meetings, f, ensure_ascii=False, indent=2)

    # 统计信息
    total = len(meetings)
    recording_count = sum(1 for m in meetings if m.get('isRecording', False))
    locked_count = sum(1 for m in meetings if m.get('isLocked', False))
    allow_unmute_false = sum(1 for m in meetings if not m.get('settings', {}).get('allowParticipantUnmute', True))

    print(f"处理完成:")
    print(f"- 总会议数: {total}")
    print(f"- 正在录制: {recording_count}")
    print(f"- 已锁定: {locked_count}")
    print(f"- 禁止自解除静音: {allow_unmute_false}")

if __name__ == '__main__':
    input_file = r'D:\android_template\Tencent-Meeting\app\src\main\assets\data\meetings.json'
    output_file = input_file  # 直接覆盖原文件
    add_test_data(input_file, output_file)
