import logging
import os

try:
    from ._device_utils import (
        compact_digits,
        contains_text,
        current_ui_text,
        default_backup_dir,
        list_records,
        read_json_from_device,
    )
except ImportError:
    from _device_utils import (
        compact_digits,
        contains_text,
        current_ui_text,
        default_backup_dir,
        list_records,
        read_json_from_device,
    )


PACKAGE_NAME = "com.example.tencent_meeting_sim"
TARGET_MEETING_ID = "4157555988"
TARGET_LINK = "meeting.tencent.com/p/4157555988"
MEETINGS_FILE = "meetings.json"
ROOMS_FILE = "personal_meeting_rooms.json"

REPLAY_CONTEXT_MARKERS = [
    "Meeting Replay",
    "Meeting Info",
    "Video",
    "Download",
    "Duration",
    "Start Time",
    "Participants",
    "1.0x",
    "\u4f1a\u8bae\u56de\u653e",
    "\u4f1a\u8bae\u4fe1\u606f",
    "\u65f6\u957f",
    "\u5f00\u59cb\u65f6\u95f4",
    "\u53c2\u4f1a\u4eba",
]
PLAYING_MARKERS = ["Pause", "\u6682\u505c"]


def _target_data_exists(device_id, backup_dir) -> bool:
    rooms = list_records(
        read_json_from_device(
            device_id=device_id,
            package_name=PACKAGE_NAME,
            device_json_path=f"files/{ROOMS_FILE}",
            backup_dir=backup_dir,
        )
    )
    room_exists = any(
        str(room.get("meetingId")) == TARGET_MEETING_ID
        and TARGET_LINK in str(room.get("meetingLink", ""))
        for room in rooms
    )

    meetings = list_records(
        read_json_from_device(
            device_id=device_id,
            package_name=PACKAGE_NAME,
            device_json_path=f"files/{MEETINGS_FILE}",
            backup_dir=backup_dir,
        )
    )
    meeting_exists = any(str(meeting.get("meetingId")) == TARGET_MEETING_ID for meeting in meetings)

    if not (room_exists or meeting_exists):
        logging.error("Target meeting/personal room %s was not found in device data.", TARGET_MEETING_ID)
    return room_exists or meeting_exists


def check_previous_meeting_playback(
    result=None,
    device_id=None,
    backup_dir=None,
) -> bool:
    """Verify the target replay page is open and playback has been started."""

    del result
    if backup_dir is None:
        backup_dir = default_backup_dir("tencentmeeting_eval_2")

    data_ok = _target_data_exists(device_id, backup_dir)
    ui_text = current_ui_text(device_id, backup_dir)
    if not ui_text:
        logging.error("Unable to read current UI state.")
        return False

    target_in_ui = TARGET_MEETING_ID in compact_digits(ui_text)
    replay_context = contains_text(ui_text, REPLAY_CONTEXT_MARKERS)
    playback_started = contains_text(ui_text, PLAYING_MARKERS)

    if not target_in_ui:
        logging.error("Current UI does not show target meeting id %s.", TARGET_MEETING_ID)
    if not replay_context:
        logging.error("Current UI does not look like a meeting replay page.")
    if not playback_started:
        logging.error("Playback has not started; expected a Pause control in the current UI.")

    return data_ok and target_in_ui and replay_context and playback_started


if __name__ == "__main__":
    print(check_previous_meeting_playback())
