package cn.javasoso.meeting.mapper;

import cn.javasoso.meeting.model.MeetRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MeetRoomMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MeetRoom record);

    int insertSelective(MeetRoom record);

    MeetRoom selectByPrimaryKey(Integer id);

    /**
     * 获取所有的会议室名字状态
     * @return
     */
    List<MeetRoom> getAllMeetRoom();

    int updateByPrimaryKeySelective(MeetRoom record);

    int updateByPrimaryKey(MeetRoom record);
}