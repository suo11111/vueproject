package com.example.mapper;

import com.example.entity.Employee;
import com.example.entity.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployeeMapper {
    List<Employee> selectAll(Employee employee);
    void insert(Employee employee);

    void updateById(Employee employee);

    @Delete("delete from employee where id=#{id}")
    void deleteById(Integer id);

    @Select("select * from employee where name=#{name}")
    List<Employee> selectName(String name);

    @Select("select * from employee where username=#{username}")
    Employee selectByUsername(String username);

    @Select("select * from employee where id=#{id}")
    Employee selectById(Integer id);
}
