package com.example.controller;

import com.example.common.Result;
import com.example.entity.Employee;
import com.example.service.EmployeeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource//标识为SpringBoot的一个bean
    private EmployeeService employeeService;

    /*
      查询所有的数据
    */

    @GetMapping("/selectAll")
    public Result selectAll(Employee employee){
        List<Employee> list= employeeService.selectAll(employee);
        return Result.success(list);

    }
    @GetMapping("/selectById/{id}")
    public Result selectName(@PathVariable Integer id){//@PathVariable注解用于获取url中的变量，url中的{name}部分
        Employee employee= employeeService.selectById(id);
        return Result.success(employee);

    }


     /*分页查询数据
    pageNum当前页码
    pageSize每页个数*/

    @GetMapping("/selectPage")
    public Result selectPage(Employee employee,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam Integer pageSize){
        PageInfo<Employee> pageInfo= employeeService.selectPage(employee,pageNum,pageSize);

        return Result.success(pageInfo);

    }


    //添加数据
    @PostMapping("/add")
    public Result add(@RequestBody Employee employee){//@RequestBody注解用于获取json格式的数据
        employeeService.add(employee);
        return Result.success();

    }

    //更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Employee employee){//@RequestBody注解用于获取json格式的数据
        employeeService.update(employee);
        return Result.success();

    }

    //删除数据
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        employeeService.deleteById(id);
        return Result.success();

    }
    //批量删除数据
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        employeeService.deleteBatch(ids);
        return Result.success();

    }

}
