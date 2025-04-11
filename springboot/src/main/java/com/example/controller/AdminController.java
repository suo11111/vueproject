package com.example.controller;

import com.example.common.Result;
import com.example.entity.Admin;
import com.example.entity.Employee;
import com.example.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource//标识为SpringBoot的一个bean
    private AdminService adminService;

    /*
      查询所有的数据
    */

    @GetMapping("/selectAll")
    public Result selectAll(Admin admin){
        List<Admin> list= adminService.selectAll(admin);
        return Result.success(list);

    }



     /*分页查询数据
    pageNum当前页码
    pageSize每页个数*/

    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam Integer pageSize){
        PageInfo<Admin> pageInfo= adminService.selectPage(admin,pageNum,pageSize);

        return Result.success(pageInfo);

    }
    @GetMapping("/selectById/{id}")
    public Result selectName(@PathVariable Integer id){//@PathVariable注解用于获取url中的变量，url中的{name}部分
        Admin admin= adminService.selectById(id);
        return Result.success(admin);

    }

    //添加数据
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin){//@RequestBody注解用于获取json格式的数据
        adminService.add(admin);
        return Result.success();

    }

    //更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin){//@RequestBody注解用于获取json格式的数据
        adminService.update(admin);
        return Result.success();

    }

    //删除数据
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        adminService.deleteById(id);
        return Result.success();

    }
    //批量删除数据
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        adminService.deleteBatch(ids);
        return Result.success();

    }

}
