package cn.javasoso.meeting.controller;

import cn.javasoso.meeting.constant.ResultModel;
import cn.javasoso.meeting.model.MeetRoom;
import cn.javasoso.meeting.service.MeetRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 */
@Api(description = "IndexController|一个用来测试会议室的功能的控制器")
@Controller
public class IndexController extends BaseController {

    @Autowired
    private MeetRoomService meetRoomService;
//     LocalDateTime now= LocalDateTime.now();
//     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    String date = sdf.format(now);

    /**
     * 查看所有的会议室，以及预定状态
     *
     * @param
     * @return
     */
    @ApiOperation(value = "查看所有会议室", notes = "显示所有的会议室状态")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public ResultModel index() {
        List<MeetRoom> meetRooms = meetRoomService.getAllMeetRoom();
//        model.addAttribute("meetRooms", meetRooms);
        return buildSuccessResponse(meetRooms);
    }

    /**
     * 添加会议室
     */
    @ApiOperation(value = "添加会议室", notes = "只添加会议室的名字")
    @RequestMapping(value = "/addMeetRoom", method = RequestMethod.GET)
    @ResponseBody
    @ApiImplicitParam(paramType = "query", name = "name", value = "会议室名字", required = true, dataType = "String")
    public ResultModel addMeetRoom(String name) {
        if (StringUtils.isEmpty(name)) {
            return buildErrorResponse("会议室名字不能为空");
        }
        MeetRoom meetRoom = meetRoomService.addMeetRoom(name);
        return buildSuccessResponse(meetRoom);
    }

    /**
     * 删除会议室
     */
    @ApiOperation(value = "删除会议室", notes = "根据会议室的名字删除会议室的所有数据")
    @RequestMapping(value = "/delMeetRoom", method = RequestMethod.GET)
    @ResponseBody
    @ApiImplicitParam(paramType = "query", name = "id", value = "会议室编号id", required = true, dataType = "Integer")
    public String delMeetRoom(@RequestParam Integer id) {
        if (StringUtils.isEmpty(id)) {
            return "200" + "会议室id不能为空";
        }
        MeetRoom meetRoom = meetRoomService.delMeetRoom(id);
        if (meetRoom != null) {
            return "删除成功" + meetRoom.getMeetName();
        }
        return "要删除的会议室不存在";
    }
}