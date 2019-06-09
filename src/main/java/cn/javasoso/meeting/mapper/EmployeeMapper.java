package cn.javasoso.meeting.mapper;

import cn.javasoso.meeting.model.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface EmployeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employee record);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Integer id);

    /**
     * 根据条件搜索
     * @param record
     * @return
     */
    List<Employee> selectSelective(Employee record);

    /**
     * 通过条件查找用户 用在查找特定token 和主键id
     * @param record
     * @return
     */

    Employee  selectOneSelective(Employee record);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);
}