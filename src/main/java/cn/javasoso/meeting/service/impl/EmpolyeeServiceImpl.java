package cn.javasoso.meeting.service.impl;

import cn.javasoso.meeting.exception.ApplyException;
import cn.javasoso.meeting.mapper.EmployeeIdTokenMapper;
import cn.javasoso.meeting.mapper.EmployeeMapper;
import cn.javasoso.meeting.model.Employee;
import cn.javasoso.meeting.model.EmployeeIdToken;
import cn.javasoso.meeting.service.EmployeeService;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EmpolyeeServiceImpl implements EmployeeService {
    @Resource
    private EmployeeMapper employeeMapper;
    @Resource
    private EmployeeIdTokenMapper employeeIdTokenMapper;
    @Resource
    private EmployeeIdTokenServiceImpl employeeIdTokenServiceImpl;

    /**
     * 通过request 找到token 数据库的 主键值、 用主键值去修改对应的token 值为null
     *
     * @param request
     * @return
     */
    @Override
    public boolean logOut(HttpServletRequest request) {
        List<EmployeeIdToken> employeeIdTokens = employeeIdTokenServiceImpl.getToken(request);

        int id = employeeIdTokens.get(0).getId();
        int employeeId = employeeIdTokens.get(0).getEmployeeId();
        EmployeeIdToken employeeIdToken = new EmployeeIdToken();
        employeeIdToken.setToken("");
        employeeIdToken.setEmployeeId(employeeId);
        employeeIdToken.setCreateTime(new Date());
        employeeIdToken.setId(id);
        int count = employeeIdTokenMapper.updateByPrimaryKey(employeeIdToken);
        if (count < 1) {
            throw new ApplyException("注销失败");
        }
        return true;
    }

    /**
     * 员工登录界面
     */
    @Override
    public boolean logIn(Employee record, HttpServletResponse response) {
        Preconditions.checkNotNull(record, "参数不能为空");
        Preconditions.checkNotNull(record.getEnglishName(), "英文名字不能为空");
        Preconditions.checkNotNull(record.getName(), "名字不能为空");
        Employee employee = employeeMapper.selectOneSelective(record);
        if (employee == null) {
            throw new ApplyException("提示：请先注册");

        }
        String uuid = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("token", uuid);
        //存入数据库 用户id 和这次登录的token
        EmployeeIdToken employeeIdToken = new EmployeeIdToken();
        employeeIdToken.setEmployeeId(employee.getId());
        employeeIdToken.setToken(uuid);
        employeeIdToken.setCreateTime(new Date());
        employeeIdTokenMapper.insert(employeeIdToken);
        //有效期7天
        cookie.setMaxAge(10 * 60);
        response.addCookie(cookie);

        //存入数据库
        return true;
    }

    /**
     * 添加员工的姓名 和英文名字
     *
     * @param name
     * @param englishName
     * @return
     */
    @Override
    public Employee addEmployee(String name, String englishName) {
        //判空
        Preconditions.checkNotNull(name, "名字不能为空");
        Preconditions.checkNotNull(englishName, "英文名字不能为空");

        Employee employee = new Employee();
        employee.setName(name);
        Employee noww = employeeMapper.selectOneSelective(employee);
        if(noww !=null){
            throw new ApplyException("中文名字已存在，请选择别的名字");
        }
        employee.setEnglishName(englishName);
        //以名字和英文名字为对象 去找有没有这个人
        Employee now =employeeMapper.selectOneSelective(employee);
        if(now != null){
            throw new ApplyException("用户已存在,请勿重复注册");
        }
        employee.setCreateTime(new Date());
        employeeMapper.insertSelective(employee);
        return employee;
    }

    /**
     * 根据用户的名字 删除用户
     *
     * @param name
     * @return
     */
    @Override
    public List<Employee> delEmployee(String name) {
        //判断名字是不是为空
        Preconditions.checkNotNull(name, "名字不能为空");
        Employee record = new Employee();
        record.setName(name);
        //按照条件查找对象
        List<Employee> employees = employeeMapper.selectSelective(record);
        //遍历数组得到每个对象的id 只要符合条件都删除
        if (employees == null || employees.size() < 1) {
            throw new ApplyException("用户不存在");
        }
        //名字有可能有很多个对象 所以用数组接收
        //遍历 通过主键id删除
        for (Employee e : employees) {
            employeeMapper.deleteByPrimaryKey(e.getId());
        }
        return employees;
    }

    /**
     * 根据用户中文名字查找用户
     *
     * @param name
     * @return
     */
    @Override
    public List<Employee> findEmployee(String name) {
        Preconditions.checkNotNull(name, "名字不能为空");
        Employee record = new Employee();
        record.setName(name);
        List<Employee> employees = employeeMapper.selectSelective(record);
        if (employees == null || employees.size() < 1) {
            throw new ApplyException("查找的用户不存在");
        }
        return employees;
    }

    /**
     * 更改用户信息  其实这个只需要传入一个对像 就行  写复杂了
     * 但是我就是不想改 咋滴
     *
     * @param id
     * @param name
     * @param englishName
     * @return
     */
    @Override
    public Employee updateEmployee(int id, String name, String englishName) {
        Preconditions.checkNotNull(id, "id不能为空");
        Preconditions.checkNotNull(name, "名字不能为空");
        Preconditions.checkNotNull(englishName, "英文名不能为空");
        Employee record = new Employee();
        record.setId(id);
        Employee now = employeeMapper.selectByPrimaryKey(id);
        if(now == null){
            throw new ApplyException("对应的id用户不存在");
        }
        //创建个实体 装载临时的测定对象 装入名字和英文名字
        Employee employee = new Employee();
        employee.setName(name);
        //now1 标识返回的数据
        employee.setEnglishName(englishName);
        Employee now1 = employeeMapper.selectOneSelective(employee);
        //判断now1 如果不是空说明 需要更改的数据存在 更改失败
        if (now1 != null && !now1.getId().equals(id)) {
            throw new ApplyException("需要更改的数据和已有数据冲突！更换更新数据");
        }

        record.setName(name);
        record.setEnglishName(englishName);
        record.setUpdateTime(new Date());
        int x = employeeMapper.updateByPrimaryKeySelective(record);
        if (x < 1) {
            return null;
        }
        return record;
    }
}
