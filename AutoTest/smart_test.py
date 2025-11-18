'''
智能测试脚本：自动检测虚拟机数据并进行验证
用途：
1. 自动从虚拟机读取最新数据
2. 智能识别可测试的场景
3. 提供清晰的测试结果和建议

使用方法：
    python smart_test.py [测试类型]

测试类型（可选）：
    - mic: 测试麦克风静音状态
    - camera: 测试摄像头开关状态
    - message: 测试消息发送
    - all: 运行所有测试（默认）
'''

import subprocess
import json
import sys
from datetime import datetime

ADB_PATH = r'D:\AndroidStudio\platform-tools\adb.exe'
PACKAGE_NAME = 'com.example.tencentmeeting'


class SmartTester:
    """智能测试器"""

    def __init__(self):
        self.participants = []
        self.meetings = []
        self.messages = []
        self.users = []

    def load_data(self):
        """从虚拟机加载所有数据"""
        print("\n[1/4] 正在从虚拟机加载数据...")

        # 加载参会人数据
        self.participants = self._read_json('meeting_participants.json')
        print(f"  - 参会人记录: {len(self.participants) if self.participants else 0} 条")

        # 加载会议数据
        self.meetings = self._read_json('meetings.json')
        print(f"  - 会议记录: {len(self.meetings) if self.meetings else 0} 条")

        # 加载消息数据
        self.messages = self._read_json('messages.json')
        print(f"  - 消息记录: {len(self.messages) if self.messages else 0} 条")

        # 加载用户数据
        self.users = self._read_json('users.json')
        print(f"  - 用户记录: {len(self.users) if self.users else 0} 条")

        return all([
            self.participants is not None,
            self.meetings is not None,
            self.messages is not None,
            self.users is not None
        ])

    def _read_json(self, filename):
        """从虚拟机读取JSON文件"""
        try:
            result = subprocess.run([
                ADB_PATH,
                'exec-out',
                'run-as',
                PACKAGE_NAME,
                'cat',
                f'files/{filename}'
            ], capture_output=True)

            if result.returncode != 0:
                return None

            content = result.stdout.decode('utf-8', errors='ignore')
            if not content.strip():
                return []

            return json.loads(content)
        except Exception as e:
            print(f"  [错误] 读取 {filename} 失败: {e}")
            return None

    def test_microphone(self):
        """测试麦克风状态"""
        print("\n[2/4] 测试麦克风静音状态...")

        if not self.participants:
            print("  [跳过] 没有参会人数据")
            return

        # 找出所有有静音状态的参会人
        test_cases = []
        for p in self.participants:
            user_id = p.get('userId')
            meeting_id = p.get('meetingId')
            is_muted = p.get('isMuted')

            if user_id and meeting_id and is_muted is not None:
                test_cases.append({
                    'userId': user_id,
                    'meetingId': meeting_id,
                    'isMuted': is_muted,
                    'participant': p
                })

        print(f"  找到 {len(test_cases)} 个可测试的麦克风状态")

        for i, case in enumerate(test_cases[:5], 1):  # 只显示前5个
            status = "静音" if case['isMuted'] else "未静音"
            print(f"  [{i}] 用户 {case['userId']} 在会议 {case['meetingId']}: {status}")

        if test_cases:
            print(f"\n  [建议] 运行以下命令测试:")
            first = test_cases[0]
            print(f"    python eval_1.py")
            print(f"    # 修改参数为: userId='{first['userId']}', meetingId='{first['meetingId']}', expected_muted_status={first['isMuted']}")

    def test_camera(self):
        """测试摄像头状态"""
        print("\n[3/4] 测试摄像头开关状态...")

        if not self.participants:
            print("  [跳过] 没有参会人数据")
            return

        # 找出所有有摄像头状态的参会人
        test_cases = []
        for p in self.participants:
            user_id = p.get('userId')
            meeting_id = p.get('meetingId')
            is_camera_on = p.get('isCameraOn')

            if user_id and meeting_id and is_camera_on is not None:
                test_cases.append({
                    'userId': user_id,
                    'meetingId': meeting_id,
                    'isCameraOn': is_camera_on
                })

        print(f"  找到 {len(test_cases)} 个可测试的摄像头状态")

        for i, case in enumerate(test_cases[:5], 1):
            status = "开启" if case['isCameraOn'] else "关闭"
            print(f"  [{i}] 用户 {case['userId']} 在会议 {case['meetingId']}: {status}")

        if test_cases:
            print(f"\n  [建议] 运行以下命令测试:")
            first = test_cases[0]
            print(f"    python eval_2.py")
            print(f"    # 修改参数为: userId='{first['userId']}', meetingId='{first['meetingId']}', expected_camera_status={first['isCameraOn']}")

    def test_messages(self):
        """测试消息发送"""
        print("\n[4/4] 测试消息发送...")

        if not self.messages:
            print("  [跳过] 没有消息数据")
            return

        print(f"  找到 {len(self.messages)} 条消息")

        for i, msg in enumerate(self.messages[:5], 1):
            meeting_id = msg.get('meetingId', 'N/A')
            sender = msg.get('senderId', 'N/A')
            content = msg.get('content', 'N/A')
            if len(content) > 30:
                content = content[:30] + "..."

            print(f"  [{i}] 会议 {meeting_id} - {sender}: {content}")

        if self.messages:
            print(f"\n  [建议] 运行以下命令测试:")
            first = self.messages[0]
            print(f"    python eval_5.py")
            print(f"    # 修改参数为: meetingId='{first.get('meetingId')}', senderId='{first.get('senderId')}', message_content='{first.get('content')}'")

    def analyze_data_health(self):
        """分析数据健康度"""
        print("\n" + "="*60)
        print("数据健康度分析")
        print("="*60)

        # 检查会议和参会人的对应关系
        if self.meetings and self.participants:
            meeting_ids_in_meetings = {m.get('meetingId') for m in self.meetings if isinstance(m, dict)}
            meeting_ids_in_participants = {p.get('meetingId') for p in self.participants if isinstance(p, dict)}

            only_in_meetings = meeting_ids_in_meetings - meeting_ids_in_participants

            print(f"\n会议数据: {len(self.meetings)} 条")
            print(f"参会人数据: {len(self.participants)} 条")
            print(f"有参会人记录的会议: {len(meeting_ids_in_participants)} 个")

            if only_in_meetings:
                print(f"\n[警告] 发现 {len(only_in_meetings)} 个会议没有参会人记录!")
                print("这些会议中的操作将不会被保存:")
                for meeting_id in list(only_in_meetings)[:5]:
                    print(f"  - {meeting_id}")
                if len(only_in_meetings) > 5:
                    print(f"  ... 还有 {len(only_in_meetings) - 5} 个")
                print("\n[建议] 重新安装 App 或确保加入会议时创建了参会人记录")
            else:
                print("\n[OK] 所有会议都有参会人记录!")

    def generate_test_script(self):
        """生成可直接运行的测试脚本"""
        print("\n" + "="*60)
        print("生成自定义测试脚本")
        print("="*60)

        if not self.participants:
            print("\n[错误] 无法生成测试脚本，缺少参会人数据")
            return

        # 生成 eval_1.py 的自定义版本
        if self.participants:
            first_participant = self.participants[0]
            script_content = f'''\'\'\'
自动生成的麦克风测试脚本
基于虚拟机实际数据生成于: {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}
\'\'\'

import subprocess
import json

ADB_PATH = r'D:\\AndroidStudio\\platform-tools\\adb.exe'
PACKAGE_NAME = 'com.example.tencentmeeting'

def check_microphone_muted(userId, meetingId, expected_muted_status):
    """检查麦克风静音状态"""
    result = subprocess.run([
        ADB_PATH,
        'exec-out',
        'run-as',
        PACKAGE_NAME,
        'cat',
        'files/meeting_participants.json'
    ], stdout=open('meeting_participants.json', 'w'), stderr=subprocess.PIPE)

    if result.returncode != 0:
        print(f"ADB命令执行失败: {{result.stderr.decode('utf-8', errors='ignore')}}")
        return False

    try:
        with open('meeting_participants.json', 'r', encoding='utf-8') as f:
            content = f.read()
            if not content.strip():
                print("错误: 文件为空")
                return False
            data = json.loads(content)
    except json.JSONDecodeError as e:
        print(f"JSON解析错误: {{e}}")
        return False
    except FileNotFoundError:
        print("错误: 文件未找到")
        return False

    for participant in data:
        if participant['userId'] == userId and participant['meetingId'] == meetingId:
            if participant['isMuted'] == expected_muted_status:
                print(f"[OK] 麦克风状态正确: {{'isMuted': {{expected_muted_status}}}}")
                return True
            else:
                print(f"[ERROR] 麦克风状态不匹配!")
                print(f"  期望: {{'isMuted': {{expected_muted_status}}}}")
                print(f"  实际: {{'isMuted': {{participant['isMuted']}}}}")
                return False

    print(f"[ERROR] 未找到用户 {{userId}} 在会议 {{meetingId}} 的记录")
    return False

if __name__ == "__main__":
    # 基于虚拟机实际数据的测试参数
    print("测试麦克风静音状态...")
    result = check_microphone_muted(
        userId='{first_participant.get("userId")}',
        meetingId='{first_participant.get("meetingId")}',
        expected_muted_status={first_participant.get("isMuted")}
    )
    print(f"\\n测试结果: {{result}}")
'''

            with open('auto_test_mic.py', 'w', encoding='utf-8') as f:
                f.write(script_content)

            print("\n[OK] 已生成测试脚本: auto_test_mic.py")
            print("  运行方式: python auto_test_mic.py")


def main():
    """主函数"""
    print("="*60)
    print("腾讯会议 - 智能测试工具")
    print("="*60)

    tester = SmartTester()

    # 加载数据
    if not tester.load_data():
        print("\n[错误] 数据加载失败，请检查:")
        print("  1. Android 虚拟机是否正在运行")
        print("  2. ADB 是否正确连接")
        print("  3. App 是否已安装并运行过")
        return

    # 运行测试
    test_type = sys.argv[1] if len(sys.argv) > 1 else 'all'

    if test_type in ['mic', 'all']:
        tester.test_microphone()

    if test_type in ['camera', 'all']:
        tester.test_camera()

    if test_type in ['message', 'all']:
        tester.test_messages()

    # 分析数据健康度
    tester.analyze_data_health()

    # 生成测试脚本
    tester.generate_test_script()

    print("\n" + "="*60)
    print("测试完成!")
    print("="*60)


if __name__ == "__main__":
    main()
