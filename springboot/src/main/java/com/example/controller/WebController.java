package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Employee;
import com.example.entity.Product;
import com.example.exception.CustomException;
import com.example.jwt.JwtUtils;
import com.example.service.AdminService;
import com.example.service.EmployeeService;
import com.example.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController//对外提供数据接口
public class WebController {
    @Resource
    private EmployeeService employeeService;

    @Resource
    private AdminService adminService;
    @Resource
    private ProductService productService;
    @Resource
    private JwtUtils jwtUtils;


    @GetMapping("/hello")//访问地址
    public String hello() {
        return "hello";
    }

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {//@RequestBody将json数据转换为java对象
        Account result = null;
        if ("ADMIN".equals(account.getRole())) {
            result = adminService.login(account);
        } else if ("EMP".equals(account.getRole())) {
            result = employeeService.login(account);
        } else {
            throw new CustomException("500", "角色错误");
        }
        //生成token
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(result.getId()));
        String token = jwtUtils.generateToken(map);
        result.setToken(token);
        return Result.success(result);
    }

    /*员工注册*/
    @PostMapping("/register")
    public Result register(@RequestBody Employee employee) {//@RequestBody将json数据转换为java对象
        employeeService.register(employee);
        return Result.success();
    }


    /*修改密码*/
    @PutMapping("/updatePassword")
    public Result register(@RequestBody Account account) {//@RequestBody将json数据转换为java对象
        if ("ADMIN".equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if ("EMP".equals(account.getRole())) {
            employeeService.updatePassword(account);
        } else {
            throw new CustomException("500", "角色错误");
        }
        return Result.success();
    }

    @GetMapping("/barData")
    public Result getBarData() {
        Map<String, Integer> supplierSupplyMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        List<Product> productList =  productService.selectAll(null);

        for (Product product : productList){
            String supplierName = product.getSupplierName();
            int stockQuantity = product.getStockQuantity();
            int sales = product.getSales();

            supplierSupplyMap.put(supplierName, supplierSupplyMap.getOrDefault(supplierName, 0) + stockQuantity + sales);
        }
        map.put("supplier", supplierSupplyMap.keySet());//x轴数据
        map.put("count", supplierSupplyMap.values());//y轴数据
        return Result.success(map);
    }

    @GetMapping("/barData1")//商品销量
    public Result getBarData1() {
        Map<String, Object> salesMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        List<Product> productList =  productService.selectAll(null);

        for (Product product : productList){
            String productName = product.getProductName();
            int sales = product.getSales();
            Double margin= product.getMargin();
            Double marginSales = sales* margin;

            // 为每个产品创建一个内部 Map 存储相关信息
            Map<String, Object> productInfo = new HashMap<>();
            productInfo.put("sales", sales);
            productInfo.put("margin", margin);
            productInfo.put("marginSales", marginSales);

            // 将产品信息存入外层 Map
            salesMap.put(productName, productInfo);
        }
        map.put("productName", salesMap.keySet());//x轴数据
        map.put("productInfo", salesMap.values());//y轴数据
       /* List<Integer> salesList = new ArrayList<>();
        List<Double> marginList = new ArrayList<>();
        List<Double> marginSalesList = new ArrayList<>();

        for (Map<String, Object> info : salesMap.values()) {
            salesList.add((Integer) info.get("sales"));
            marginList.add((Double) info.get("margin"));
            marginSalesList.add((Double) info.get("marginSales"));
        }

        map.put("sales", salesList);
        map.put("margin", marginList);
        map.put("marginSales", marginSalesList);*/

        return Result.success(map);
    }

    @GetMapping("/pieData")
    public Result getPieData() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Product> productList =  productService.selectAll(null);
        Set<String> supplierNameSet =  productList.stream().map(Product::getSupplierName).collect(Collectors.toSet());
        for (String supplierName : supplierNameSet){
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", supplierName);
            long count = productList.stream().filter(product -> product.getSupplierName().equals(supplierName)).count();
            map.put("value", count);
            //统计供应商的产品种类数量
            list.add(map);
        }

        return Result.success(list);
    }



}
