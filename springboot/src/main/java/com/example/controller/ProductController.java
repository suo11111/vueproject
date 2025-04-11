package com.example.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Product;
import com.example.service.ProductService;
import com.example.service.SupplierService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource//标识为SpringBoot的一个bean
    private ProductService productService;


    /*
      查询所有的数据
    */

    @GetMapping("/selectAll")
    public Result selectAll(Product product){
        List<Product> list= productService.selectAll(product);
        return Result.success(list);

    }

    @GetMapping("/selectName/{name}")
    public Result selectName(@PathVariable String name){//@PathVariable注解用于获取url中的变量，url中的{name}部分
        List<Product> list= productService.selectName(name);
        return Result.success(list);

    }
    @GetMapping("/selectOne")
    public Result selectOne(@RequestParam String name){//@RequestParam注解用于获取url中的参数，url中的?name=xxx部分
        List<Product> list= productService.selectName(name);
        return Result.success(list);

    }


     /*分页查询数据
    pageNum当前页码
    pageSize每页个数*/

    @GetMapping("/selectPage")
    public Result selectPage(Product product,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam Integer pageSize){
        PageInfo<Product> pageInfo= productService.selectPage(product,pageNum,pageSize);

        return Result.success(pageInfo);

    }


    //添加数据
    @PostMapping("/add")
    public Result add(@RequestBody Product product){//@RequestBody注解用于获取json格式的数据
        productService.add(product);
        return Result.success();

    }

    //更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Product product){//@RequestBody注解用于获取json格式的数据
        productService.update(product);
        return Result.success();

    }

    //删除数据
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        productService.deleteById(id);
        return Result.success();

    }
    //批量删除数据
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        productService.deleteBatch(ids);
        return Result.success();

    }

    //导出Excel
    @GetMapping("/export")
    public void export(HttpServletResponse response,  @RequestParam(required = false) Integer stockQuantity) throws IOException {
        //1:拿到数据
        List<Product> list;
        if (stockQuantity != null) {
            list = productService.selectByStockQuantity(stockQuantity);
        } else {
            list = productService.selectAll(null);
        }
        //2:构建ExcelWriter
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //3:设置中文表头
        writer.addHeaderAlias("productId","商品编号");
        writer.addHeaderAlias("productName","商品名称");
        writer.addHeaderAlias("category","商品类别");
        writer.addHeaderAlias("price","商品价格");
        writer.addHeaderAlias("stockQuantity","商品库存");
        writer.addHeaderAlias("description","商品描述");
        writer.addHeaderAlias("sales","商品销量");
        writer.addHeaderAlias("margin","商品利润");
        writer.addHeaderAlias("supplierName","供应商名称");
        writer.addHeaderAlias("supplierContact","供应商联系方式");
        writer.setOnlyAlias(true);//只导出有别名的字段
        //4:写出数据到writer
        writer.write(list,true);
        //5:输出文件的名字  以及输出流的头信息
        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("商品信息表", "UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
        //6:写出到输出流,并关闭writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();

    }

    /*Excel导入*/

    @PostMapping("/import")
    public Result importData(MultipartFile file) throws IOException {
        //1.拿到输入流，构建reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //2.读取Excel的数据
        reader.addHeaderAlias("商品名称","productName");
        reader.addHeaderAlias("商品类别","category");
        reader.addHeaderAlias("商品价格","price");
        reader.addHeaderAlias("商品库存","stockQuantity");
        reader.addHeaderAlias("商品描述","description");
        reader.addHeaderAlias("商品销量","sales");
        reader.addHeaderAlias("商品利润","margin");
        reader.addHeaderAlias("供应商名称","supplierName");
        reader.addHeaderAlias("供应商联系方式","supplierContact");

        List<Product> productList = reader.readAll(Product.class);

        // 3. 查询数据库中已有的产品ID
        List<Integer> existingProductIds = productService.getAllProductIds();


        //4.写入数据到数据库
        for(Product product : productList){
            // 根据供应商名称查询供应商ID
            Integer supplierId = productService.getSupplierIdByName(product.getSupplierName());
            if (supplierId != null) {
                product.setSupplierId(supplierId);
            } else {
                // 处理供应商名称不存在的情况，例如记录日志或抛出异常
                return Result.error("200","供应商名称不存在");
            }
            if (existingProductIds.contains(product.getProductId())) {
                productService.update(product); // 更新产品
            } else {
                productService.add(product); // 插入新产品
            }
        }
        return Result.success();
    }

}
