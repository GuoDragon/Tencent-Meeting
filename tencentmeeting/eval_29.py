import logging
import os

from appsim.utils import read_json_from_device


DEFAULT_DEVICE_ID = "122.228.230.214:10343"
PACKAGE_NAME = "com.example.tencent_meeting_sim"
USER_ID = "user001"
USERS_FILE = "users.json"
PERSONAL_MEETING_ROOMS_FILE = "personal_meeting_rooms.json"
INVITATIONS_FILE = "meeting_invitations.json"


def _read_data(device_id, backup_dir, file_name):
    return read_json_from_device(
        device_id=device_id,
        package_name=PACKAGE_NAME,
        device_json_path=f"files/{file_name}",
        backup_dir=backup_dir,
    )


def _get_invitee_id(invitation):
    return invitation.get("inviteeId") or invitation.get("invitedUserId")


def check_personal_room_invitation(
    result=None,
    device_id=None,
    backup_dir=None,
) -> bool:
    """Verify the personal room invited every user whose username starts with 周."""

    user_id = USER_ID
    effective_device_id = device_id or DEFAULT_DEVICE_ID

    if backup_dir is None:
        backup_dir = os.path.join(os.getcwd(), "scripts_backup", "tencentmeeting_eval_29")

    try:
        users_data = _read_data(effective_device_id, backup_dir, USERS_FILE)
        if users_data is None:
            logging.error(f"错误: 无法从设备读取或解析 {USERS_FILE}。")
            return False

        zhou_users = [u for u in users_data if u.get("username", "").startswith("周")]
        zhou_user_ids = {u.get("userId") for u in zhou_users if u.get("userId")}

        if not zhou_user_ids:
            logging.error("未找到周姓用户。")
            return False

        logging.info(f"找到 {len(zhou_user_ids)} 个周姓用户: {zhou_user_ids}")

        rooms_data = _read_data(effective_device_id, backup_dir, PERSONAL_MEETING_ROOMS_FILE)
        if rooms_data is None:
            logging.error(f"错误: 无法从设备读取或解析 {PERSONAL_MEETING_ROOMS_FILE}。")
            return False

        user_room = next((r for r in rooms_data if r.get("userId") == user_id), None)
        if not user_room:
            logging.error(f"未找到用户 {user_id} 的个人会议室")
            return False

        personal_meeting_id = user_room.get("meetingId")
        if not personal_meeting_id:
            logging.error(f"用户 {user_id} 的个人会议室缺少 meetingId。")
            return False

        invitations_data = _read_data(effective_device_id, backup_dir, INVITATIONS_FILE)
        if invitations_data is None:
            logging.error(f"错误: 无法从设备读取或解析 {INVITATIONS_FILE}。")
            return False

        invited_zhou_users = set()
        for invitation in invitations_data:
            if invitation.get("meetingId") == personal_meeting_id:
                invited_user_id = _get_invitee_id(invitation)
                if invited_user_id in zhou_user_ids:
                    invited_zhou_users.add(invited_user_id)

        logging.info(f"已邀请的周姓用户: {invited_zhou_users}")

        missing_users = zhou_user_ids - invited_zhou_users
        if missing_users:
            logging.error(f"验证失败，仍有周姓用户未被邀请: {missing_users}")
            return False

        logging.info(f"邀请验证成功: 所有 {len(zhou_user_ids)} 个周姓用户均已邀请")
        return True

    except Exception as e:
        logging.error(f"验证过程出错: {str(e)}")
        return False


if __name__ == "__main__":
    print(check_personal_room_invitation())
