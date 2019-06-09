package cn.javasoso.meeting.controller;

import cn.javasoso.meeting.constant.ResultModel;
import cn.javasoso.meeting.model.EmployeeIdToken;
import cn.javasoso.meeting.model.MeetTime;
import cn.javasoso.meeting.service.MeetTimeServic;
import cn.javasoso.meeting.service.impl.EmployeeIdTokenServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

;

@Api(description = "会议室预定")
@Controller
public class MeetTimeController extends BaseController {
    @Autowired
    private MeetTimeServic meetTimeServic;
    @Resource
    private EmployeeIdTokenServiceImpl employeeIdTokenServiceImpl;

    /**
     * 预定会议室
     */
    @ResponseBody
    @ApiOperation(value = "添加预定", notes = "需要三个参数")
    @RequestMapping(value = "/addMeetTime", method = RequestMethod.POST)
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "query", name = "startDateString", value = "开始的时间", required = true, dataType = "String", defaultValue = "2019-06-06 18:12:12"),
                    @ApiImplicitParam(paramType = "query", name = "endDateString", value = "结束的时间", required = true, dataType = "String", defaultValue = "2019-06-06 20:12:12")
            })
//    e @RequestBody @PathVariable @RequestHeader @CookieValue
    public ResultModel addMeetTime( HttpServletRequest request, Integer meetRoomId, String startDateString, String endDateString) throws Exception {
        // FIXME 不能使用int  貌似不能判断int类型
        if (StringUtils.isEmpty(meetRoomId)) {
            return buildErrorResponse("会议室id不能为空或0");
        }
        if (StringUtils.isEmpty(startDateString)) {
            return buildErrorResponse("开始日期不能为空");
        }
        if (StringUtils.isEmpty(endDateString)) {
            return buildErrorResponse("结束日期不能为空");
        }
        /**
         * // 提取 请求中的cookie中的uuid 拿uuid 去token表中找 看有没有 ！！
         * //应为返回的是一个List 所以 接收下
         * //可以肯定的通过token去查找 对应的值 要么没有 要么只有一个，走到这一步说明有且是一个
         */
        List<EmployeeIdToken> employeeIdTokens = employeeIdTokenServiceImpl.getToken(request);
        //在这
        // 得到对应此token的 用户id  提取出来
        Integer employeeId = employeeIdTokens.get(0).getEmployeeId();
        //把日期转换成时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换时间的字符串成date  方便存储
        Date startDate = sdf.parse(startDateString);
        Date endDate = sdf.parse(endDateString);
        //包装成实体对象 准备insert
        MeetTime record = new MeetTime();
        record.setEmployeeId(employeeId);
        record.setMeetRoomId(meetRoomId);
        record.setStartTime(startDate);
        record.setEndTime(endDate);
        record.setStatus(1);
        //去存储 并接受返回来的对象 是本身还是冲突的对象
        MeetTime meet = meetTimeServic.addMeetTime(record);
        if (record.equals(meet)) {
            return buildSuccessResponse(record);
        }
        return buildErrorResponse("预约时间冲突",meet);
    }

    /**
     * 取消预定
     */
    @ResponseBody
    @RequestMapping(value = "/cancelMeetTime", method = RequestMethod.GET)
    @ApiOperation(value = "取消预定", notes = "需要输入用户id 和 会议室id")
    public ResultModel cancelMeetTime(HttpServletRequest request, int meetRoomId) {
            String s  = meetRoomId +"";
            if("".equals(s)){
                return buildErrorResponse("会议室id不能为空");
            }
        List<EmployeeIdToken> employeeIdTokens = employeeIdTokenServiceImpl.getToken(request);
// 走到这一步说明他已经登录了
        int employeeId = employeeIdTokens.get(0).getEmployeeId();
        //判断此id 有没有预约记录
        MeetTime meetTime = new MeetTime();
        meetTime.setEmployeeId(employeeId);
        List<MeetTime> meetTimes = meetTimeServic.selectBy(meetTime);
        if(meetTimes==null || meetTimes.size()<1){
            return buildErrorResponse("您没有预约记录");
        }
        meetTimeServic.cancelMeetTime(employeeId, meetRoomId);
        List<MeetTime> sss= null;
        for(MeetTime m :meetTimes){
            if(m.getState().equals(1) && m.getMeetRoomId().equals(meetRoomId)){
                sss.add(m);
            }
        }

        return buildSuccessResponse(sss);

    }

    /**
     * 显示预约记录
     *
     * @return
     */
    @ApiOperation(value = "查看预定情况", notes = "返回所有数据")
    @ResponseBody
    @RequestMapping(value = "/getMeetTimes", method = RequestMethod.GET)
    public ResultModel getMeetTime() {
        return buildSuccessResponse(meetTimeServic.getMeetTime());
    }
}
