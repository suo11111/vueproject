package com.example.controller;

import com.example.common.Result;
import com.example.entity.Supplier;
import com.example.service.SupplierService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Resource//标识为SpringBoot的一个bean
    private SupplierService supplierService;

    /*
      查询所有的数据
    */

    @GetMapping("/selectAll")
    public Result selectAll(Supplier supplier){
        List<Supplier> list= supplierService.selectAll(supplier);
        return Result.success(list);

    }

    @GetMapping("/selectName/{name}")
    public Result selectName(@PathVariable String name){//@PathVariable注解用于获取url中的变量，url中的{name}部分
        List<Supplier> list= supplierService.selectName(name);
        return Result.success(list);

    }
    @GetMapping("/selectOne")
    public Result selectOne(@RequestParam String name){//@RequestParam注解用于获取url中的参数，url中的?name=xxx部分
        List<Supplier> list= supplierService.selectName(name);
        return Result.success(list);

    }


     /*分页查询数据
    pageNum当前页码
    pageSize每页个数*/

    @GetMapping("/selectPage")
    public Result selectPage(Supplier supplier,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam Integer pageSize){
        PageInfo<Supplier> pageInfo= supplierService.selectPage(supplier,pageNum,pageSize);

        return Result.success(pageInfo);

    }


    //添加数据
    @PostMapping("/add")
    public Result add(@RequestBody Supplier supplier){//@RequestBody注解用于获取json格式的数据
        supplierService.add(supplier);
        return Result.success();

    }

    //更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Supplier supplier){//@RequestBody注解用于获取json格式的数据
        supplierService.update(supplier);
        return Result.success();

    }

    //删除数据
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        supplierService.deleteById(id);
        return Result.success();

    }
    //批量删除数据
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        supplierService.deleteBatch(ids);
        return Result.success();

    }

}
