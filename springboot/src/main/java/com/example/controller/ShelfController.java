package com.example.controller;

import com.example.common.Result;
import com.example.entity.Shelf;
import com.example.service.ShelfService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shelf")
public class ShelfController {

    @Resource//标识为SpringBoot的一个bean
    private ShelfService shelfService;

    /*
      查询所有的数据
    */

    @GetMapping("/selectAll")
    public Result selectAll(Shelf shelf){
        List<Shelf> list= shelfService.selectAll(shelf);
        return Result.success(list);

    }

    @GetMapping("/selectLocation/{location}")
    public Result selectName(@PathVariable String location){//@PathVariable注解用于获取url中的变量，url中的{name}部分
        List<Shelf> list= shelfService.selectLocation(location);
        return Result.success(list);

    }
    @GetMapping("/selectOne")
    public Result selectOne(@RequestParam String name){//@RequestParam注解用于获取url中的参数，url中的?name=xxx部分
        List<Shelf> list= shelfService.selectLocation(name);
        return Result.success(list);

    }


     /*分页查询数据
    pageNum当前页码
    pageSize每页个数*/

    @GetMapping("/selectPage")
    public Result selectPage(Shelf shelf,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam Integer pageSize){
        PageInfo<Shelf> pageInfo= shelfService.selectPage(shelf,pageNum,pageSize);

        return Result.success(pageInfo);

    }


    //添加数据
    @PostMapping("/add")
    public Result add(@RequestBody Shelf shelf){//@RequestBody注解用于获取json格式的数据
        shelfService.add(shelf);
        return Result.success();

    }

    //更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Shelf shelf){//@RequestBody注解用于获取json格式的数据
        shelfService.update(shelf);
        return Result.success();

    }

    //删除数据
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        shelfService.deleteById(id);
        return Result.success();

    }
    //批量删除数据
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        shelfService.deleteBatch(ids);
        return Result.success();

    }

}
