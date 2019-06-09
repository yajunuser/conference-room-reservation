package cn.javasoso.meeting.mapper;

import cn.javasoso.meeting.model.EmployeeIdToken;

import java.util.List;

public interface EmployeeIdTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EmployeeIdToken record);

    int insertSelective(EmployeeIdToken record);

    EmployeeIdToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EmployeeIdToken record);

    int updateByPrimaryKey(EmployeeIdToken record);

    List<EmployeeIdToken> selectBySelective(EmployeeIdToken record);
}