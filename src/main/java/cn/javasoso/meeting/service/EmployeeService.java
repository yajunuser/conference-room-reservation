package cn.javasoso.meeting.service;

import cn.javasoso.meeting.model.Employee;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface EmployeeService {
    /**
     * 添加用户
     */
    Employee addEmployee(String name, String englishName);

    /**
     * s删除用户
     */
    List<Employee> delEmployee(String name);

    /**
     * 查找用户 通过名字
     */
    List<Employee> findEmployee(String name);

    /**
     * 修改员工账号密码 根据名字好点 还是根据id 好点
     */
    Employee updateEmployee(int id, String name, String englishName);

    /**
     * 登录接口
     *
     * @param record
     * @return
     */
    boolean logIn(Employee record, HttpServletResponse response);

    /**
     * 注销接口
     */
    boolean logOut(HttpServletRequest request);
}
