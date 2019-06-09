package cn.javasoso.meeting.service;

import cn.javasoso.meeting.model.MeetRoom;

import java.util.List;

public interface MeetRoomService {
    /**
     * 增加会议室
     */
    MeetRoom addMeetRoom(String name);

    /**
     * 删除会议室
     */
    MeetRoom delMeetRoom(int id);
    /**
     * 预约会议室
     * 第二个参数几个小时后预约
     * 第三个参数是使用几个小时
     */
    boolean useMeetRoom(int id,int startTime,int endTime);
    /**
     * 显示所有会议室
     */
    List<MeetRoom> getAllMeetRoom();
    /**
     * 查找会议室 根据id
     */
    MeetRoom findMeetRoom(int id);
}
