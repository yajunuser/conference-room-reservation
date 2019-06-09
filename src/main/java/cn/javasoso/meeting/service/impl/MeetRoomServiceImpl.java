package cn.javasoso.meeting.service.impl;

import cn.javasoso.meeting.exception.ApplyException;
import cn.javasoso.meeting.mapper.MeetRoomMapper;
import cn.javasoso.meeting.model.MeetRoom;
import cn.javasoso.meeting.service.MeetRoomService;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.util.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MeetRoomServiceImpl implements MeetRoomService {
    @Resource
    private MeetRoomMapper meetRoomMapper;

    /**
     * 添加会议室 只添加名字
     * 并把可预约状态设置成true
     * 开始的时间和结束的时间由产生预约后t填入 并把状态设置false
     */
    @Override
    public MeetRoom addMeetRoom(String name) {
        MeetRoom meetRoom = new MeetRoom();
        //传入一个需要创建的会议室名字
        meetRoom.setMeetName(name);
        //设置状态为true就是可以预约
        meetRoom.setState(true);
        //添加到列表
        meetRoomMapper.insertSelective(meetRoom);
        return meetRoom;
    }

    /**
     * 通过id 删除一会议室
     */
    @Override
    public MeetRoom delMeetRoom(int id) {
        /**
         * 因为返回值是一个对象
         * 所以先用id 拿出对象
         * 如果为空 说明没有这个id的 对象
         * 如果不为空就删除 并返回这个对象，说明删除的是谁 mapper 返回的是被删除的id
         */
        MeetRoom meetRoom = meetRoomMapper.selectByPrimaryKey(id);
        if(meetRoom != null) {
            meetRoomMapper.deleteByPrimaryKey(id);
            return meetRoom;
        }
        return null;
}

    @Override
    public boolean useMeetRoom(int id,int startTime,int endTimel) {
        /**
         * 根据给出的id 来做查询
         * 默认的使用时间是二个小时
         */
        MeetRoom meetRoom = meetRoomMapper.selectByPrimaryKey(id);
        //判断是否有这个会议室
        if (meetRoom != null) {
            Date d = DateUtils.setMilliseconds(DateUtils.setMinutes(DateUtils.setSeconds(new Date(), 0), 0), 0);
            Date StartTime = DateUtils.setHours(d, startTime);
            Date endTime = DateUtils.setHours(d, endTimel);
            //创建一个新的会议室对象 作为修改的对象
            MeetRoom newMeetRoom = new MeetRoom();
            newMeetRoom.setId(id);
            newMeetRoom.setMeetName(meetRoom.getMeetName());
            newMeetRoom.setStartTime(StartTime);
            newMeetRoom.setState(false);
            newMeetRoom.setEndTime(endTime);
            //判断参数的内容是不是null  好像没有必要，就是想写写这个方法
            Preconditions.checkNotNull(newMeetRoom, "参数不能为空");
            Preconditions.checkNotNull(newMeetRoom.getMeetName(), "会议室的名字不能为空");
            //如果成功了 就是true  没成功就是false
            return meetRoomMapper.updateByPrimaryKeySelective(newMeetRoom) > 0;
        } else {
            return false;
        }
    }

    @Override
    public List<MeetRoom> getAllMeetRoom() {
        List<MeetRoom> meetRooms = meetRoomMapper.getAllMeetRoom();
        if (meetRooms == null || meetRooms.size()<1) {
            throw new ApplyException("没有会议室记录");
        }
        return meetRooms;
    }

    @Override
    public MeetRoom findMeetRoom(int id) {

        return meetRoomMapper.selectByPrimaryKey(id);
    }
}