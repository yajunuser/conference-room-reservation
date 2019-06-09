package cn.javasoso.meeting;

import cn.javasoso.meeting.mapper.MeetRoomMapper;
import cn.javasoso.meeting.model.MeetRoom;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes= MeetApplication.class)
public class MeetRoomDemo {
    @Resource
    private MeetRoomMapper meetRoomMapper;
@Test
    public void getMeets(){

    }
}
