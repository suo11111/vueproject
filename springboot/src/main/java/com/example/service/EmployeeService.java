package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.Employee;
import com.example.entity.Product;
import com.example.exception.CustomException;
import com.example.mapper.EmployeeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Resource
    private EmployeeMapper employeeMapper;

    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }

    public List<Employee> selectAll(Employee employee) {
        return employeeMapper.selectAll(employee);
    }

    //分页查询
    public PageInfo<Employee> selectPage(Employee employee, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        return PageInfo.of(list);
    }

    public void add(Employee employee) {
        String username = employee.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if(dbEmployee!=null){
            throw new CustomException("400","用户名已存在");
        }
        if(StrUtil.isBlank(employee.getPassword())){//密码为空时
            employee.setPassword("123456");//默认密码
        }
        if(StrUtil.isBlank(employee.getName())){//名称为空时
            employee.setName(employee.getUsername());//默认名称
        }
        //一定要设计角色
        employee.setRole("EMP");//默认角色

        employeeMapper.insert(employee);
    }

    public void update(Employee employee) {
        employeeMapper.updateById(employee);

    }

    public void deleteById(Integer id) {
        employeeMapper.deleteById(id);
    }
    public void deleteBatch(List<Integer> ids) {
        for(Integer id : ids){
            this.deleteById(id);
        }
    }

    public Employee login(Account account) {
        String username = account.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if(dbEmployee==null){
            throw new CustomException("400","用户名不存在");
        }
        String password = account.getPassword();
        if(!dbEmployee.getPassword().equals(password)){
            throw new CustomException("401","账号或密码错误");
        }
        return dbEmployee;
    }

    public void register(Employee employee) {
        this.add(employee);
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        Employee employee =  this.selectById(id);
        if(!employee.getPassword().equals(account.getPassword())){//旧密码不正确
            throw new CustomException("401","原密码错误");
        }
        employee.setPassword(account.getNewPassword());
        this.update(employee);
    }
}
