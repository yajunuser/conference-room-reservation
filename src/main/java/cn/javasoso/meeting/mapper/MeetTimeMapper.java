package cn.javasoso.meeting.mapper;

import cn.javasoso.meeting.model.MeetTime;

import java.util.List;

public interface MeetTimeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MeetTime record);

    int insertSelective(MeetTime record);

    MeetTime selectByPrimaryKey(Integer id);

    /**
     * 通过条件查找
     * @param
     * @return
     */
    List<MeetTime> selectBy(MeetTime record);

    /**
     * 展示所有的会议室记录
     * @return
     */
    List<MeetTime> selectAllMeetName();

    /**
     * 更改status状态 把1 变成0
     */
    int updateStatus(MeetTime record);


    int updateByPrimaryKeySelective(MeetTime record);

    int updateByPrimaryKey(MeetTime record);
}