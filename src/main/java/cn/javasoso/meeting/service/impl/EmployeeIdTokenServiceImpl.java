package cn.javasoso.meeting.service.impl;

import cn.javasoso.meeting.exception.ApplyException;
import cn.javasoso.meeting.mapper.EmployeeIdTokenMapper;
import cn.javasoso.meeting.model.EmployeeIdToken;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class EmployeeIdTokenServiceImpl {
@Resource
    private EmployeeIdTokenMapper employeeIdTokenMapper;
    /**
     * 提取 请求中的cookie中的uuid 拿uuid 去token表中找 看有没有 ！！
     */
    public List<EmployeeIdToken> getToken(HttpServletRequest request){
        String token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("token")) {
                token = c.getValue();
            }
        }
        if(token == null){
           throw new ApplyException("您还未登录，请先登录");
        }
        EmployeeIdToken employeeIdToken = new EmployeeIdToken();
        employeeIdToken.setToken(token);
        List<EmployeeIdToken> employeeIdTokens = employeeIdTokenMapper.selectBySelective(employeeIdToken);
        if(employeeIdToken == null||employeeIdTokens.size()<1){
            throw new ApplyException("登录已失效或者超时,请重新登录");
        }
        return employeeIdTokens;
    }

}
