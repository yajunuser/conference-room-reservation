package cn.javasoso.meeting.controller;

import cn.javasoso.meeting.constant.ResultModel;
import cn.javasoso.meeting.model.Employee;
import cn.javasoso.meeting.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Api(description = "一个用来操作员工的控制器")
@Controller
public class EmployeeController extends BaseController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录入口
     * @param name
     * @param englishName
     * @return
     */

    @ApiOperation(value = "登录")
    @ResponseBody
    @RequestMapping(value = "/logIn", method = RequestMethod.GET)
    public ResultModel logIn(String name, String englishName, HttpServletResponse response) {
        if (StringUtils.isEmpty(name)) {
            return buildErrorResponse("用户名不能为空");
        }
        if (StringUtils.isEmpty(englishName)) {
            return buildErrorResponse("英文名字不能为空");
        }
        Employee employee = new Employee();
        employee.setName(name);
        employee.setEnglishName(englishName);
        employeeService.logIn(employee,response);
        return buildSuccessResponse("登录成功"+name+"-------"+englishName);
    }
    @ApiOperation(value = "注销")
    @ResponseBody
    @RequestMapping(value = "/logOut", method = RequestMethod.GET)
    public ResultModel logOut(HttpServletRequest request){
        employeeService.logOut(request);
        return  buildSuccessResponse("注销成功");
    }

    /**
     * 通过用户名搜索用户的id 密码
     * 这个从新写了一个mapper 的SQl方法以及配置文件xml
     *
     * @param name
     * @return
     */
    @ApiOperation(value = "搜索用户", notes = "根据用户名搜索用户")
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @ResponseBody
    public ResultModel findUser(String name) {
        if (StringUtils.isEmpty(name)) {
            return buildErrorResponse("名字不能为空");
        }
        List<Employee> employees = employeeService.findEmployee(name);
        return buildSuccessResponse(employees);
    }

    /**
     * 添加员工
     */
    @ResponseBody
    @RequestMapping(value = "/addEmployee", method = RequestMethod.GET)
    @ApiOperation(value = "添加员工", notes = "输入中文名字和英文名字")
    public ResultModel addEmployee(String name, String englishName) {
          if(StringUtils.isEmpty(name)){
              return buildErrorResponse("名字不能为空");
          }
        if (StringUtils.isEmpty(englishName)) {
            return buildErrorResponse("英文名字不能为空");
        }
        Employee employee = employeeService.addEmployee(name, englishName);
        if (employee == null) {
            return buildErrorResponse("添加失败");
        }
        return buildSuccessResponse(employee);
    }

    /**
     * 删除员工信息
     *
     * @param name
     * @return
     */
    @ApiOperation(value = "删除员工", notes = "根据员工的名字删除 一删删片 哈哈")
    @RequestMapping(value = "/delEmployee", method = RequestMethod.GET)
    @ResponseBody
    public ResultModel delEmployee(String name) {
        if (StringUtils.isEmpty(name)) {
            return buildErrorResponse("名字不能为空");
        }
        List<Employee> employee = employeeService.delEmployee(name);

        return buildSuccessResponse(employee);
    }

    /**
     * 修改 员工信息
     *
     * @param id
     * @param name
     * @param englishName
     * @return
     */
    @ApiOperation(value = "修改员工资料", notes = "需要传入三个参数")
    @ResponseBody
    @RequestMapping(value = "/updateEmployee", method = RequestMethod.GET)
    public ResultModel updateEmployee(int id, String name, String englishName) {
        if(StringUtils.isEmpty(name)){
            return buildErrorResponse("名字不能为空");
        }
        if (StringUtils.isEmpty(englishName)) {
            return buildErrorResponse("英文名字不能为空");
        }
        Employee employee = employeeService.updateEmployee(id, name, englishName);
        if (employee == null) {
            return buildErrorResponse("修改失败");
        }
        return buildSuccessResponse(employee);
    }
}
