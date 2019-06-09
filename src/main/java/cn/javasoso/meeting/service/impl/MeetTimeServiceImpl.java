package cn.javasoso.meeting.service.impl;

import cn.javasoso.meeting.exception.ApplyException;
import cn.javasoso.meeting.mapper.MeetTimeMapper;
import cn.javasoso.meeting.model.MeetTime;
import cn.javasoso.meeting.service.MeetTimeServic;
import org.assertj.core.util.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MeetTimeServiceImpl implements MeetTimeServic {
    @Resource
    private MeetTimeMapper meetTimeMapper;

    /**
     * 增加会议室预定记录
     */
    @Override
    public MeetTime addMeetTime(MeetTime record) {
        Preconditions.checkNotNull(record, "参数不能为空");
        Preconditions.checkNotNull(record.getEmployeeId(), "员工id不能为空");
        Preconditions.checkNotNull(record.getMeetRoomId(), "会议室id不能为空");
        Preconditions.checkNotNull(record.getStartTime(), "开始使用的时间不能为空");
        Preconditions.checkNotNull(record.getEndTime(), "结束会议的时间不能为空");
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        record.setCreateTime(now);
        if (record.getStartTime().before(now)) {
            throw new ApplyException("开始时间不能晚于此刻时间" + sdf.format(now));
        }
        if (record.getStartTime().after(record.getEndTime())) {
            throw new ApplyException("请正确的设置起始时间,开始时间要早于结束时间");
        }
        if (record.getStartTime().equals(record.getEndTime())) {
           throw new ApplyException("请设置时间,起始时间不能相等");
        }
        List<MeetTime> meetTimes = getMeetTime(record.getMeetRoomId());
        for (MeetTime m : meetTimes) {
            //判断预预的时间的 开始时间和结束时间是不是都早与现在已经预约过的所有时间  如果一个不合适就null 如果全合适就
            if (!(record.getEndTime().before(m.getStartTime()) || record.getStartTime().after(m.getEndTime()))) {
                //如果有不合适的把不合适的 返回回来  在控制层要判断 返回的对象是不是预约对象
                return m;
            }
        }
        //插入到数据库中
        meetTimeMapper.insertSelective(record);
        return record;
    }

    /**
     * 获得会议室的预定情况
     */
    @Override
    public List<MeetTime> getMeetTime(int meetRoomId) {
        MeetTime meetTime = new MeetTime();
        meetTime.setMeetRoomId(meetRoomId);
        List<MeetTime> meetTimes = meetTimeMapper.selectBy(meetTime);
        return meetTimes;
    }

    /**
     * 获得所有会议室预定情况
     *
     * @return
     */
    @Override
    public List<MeetTime> getMeetTime() {

        List<MeetTime> meetTimes = meetTimeMapper.selectAllMeetName();

        return meetTimes;
    }

    /**
     * 取消预约
     */
    @Override
    public boolean cancelMeetTime(int employeeId, int meetRoomId) {
        Preconditions.checkNotNull(employeeId, "员工id不能为空");
        Preconditions.checkNotNull(meetRoomId, "会议室id不能为空");
        MeetTime meetTime = new MeetTime();
        meetTime.setEmployeeId(employeeId);
        meetTime.setMeetRoomId(meetRoomId);
        meetTime.setStatus(1);
        List<MeetTime> meetTimes = meetTimeMapper.selectBy(meetTime);
        if (meetTimes.size() < 1 || meetTimes == null) {
            throw new ApplyException("未找到预约信息");
        }

        for (MeetTime m : meetTimes) {
            meetTimeMapper.updateStatus(m);
        }
        return true;
    }
    @Override
    public List<MeetTime> selectBy(MeetTime record){
        Preconditions.checkNotNull(record,"参数不能为空");
        Preconditions.checkNotNull(record.getEmployeeId(),"员工的id不能为空");
        List<MeetTime> meetTimes = meetTimeMapper.selectBy(record);
        return meetTimes;
    }
}
