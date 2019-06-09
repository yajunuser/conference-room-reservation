package cn.javasoso.meeting.service;


import cn.javasoso.meeting.model.MeetTime;

import java.util.List;

public interface MeetTimeServic {
    /**
     *添加会议室预约
     * @param record
     * @return
     */
    MeetTime addMeetTime(MeetTime record);

    /**
     * 获得会议室预约情况
     * @param meetRoomId
     * @return
     */
    List<MeetTime> getMeetTime(int meetRoomId);
    /**
     *
     */
    List<MeetTime> getMeetTime();

    /**
     *   取消预约
     * @param employeeId
     * @param meetRoomId
     * @return
     */
    boolean cancelMeetTime(int employeeId,int meetRoomId);


    /**
     * 查找会议室的预约记录
     */
    List<MeetTime> selectBy(MeetTime record);
}
