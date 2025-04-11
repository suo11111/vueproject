package com.example.service;

import com.example.entity.Product;
import com.example.mapper.ProductMapper;
import com.example.mapper.SupplierMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service//标识为SpringBoot的一个bean
public class ProductService {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private SupplierMapper supplierMapper;

    public List<Product> selectAll(Product product) {
        return productMapper.selectAll(product);
    }

    public List<Product> selectName(String name) {
        return productMapper.selectName(name);
    }


//分页查询
    public PageInfo<Product> selectPage(Product product, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> list = productMapper.selectAll(product);
        return PageInfo.of(list);
    }

    public void add(Product product) {
        productMapper.insert(product);
    }

    public void update(Product product) {
        productMapper.updateById(product);

    }

    public void deleteById(Integer id) {
        productMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for(Integer id : ids){
            this.deleteById(id);
        }
    }

    public List<Product> selectByStockQuantity(Integer stockQuantity) {
        return productMapper.selectByStockQuantity(stockQuantity);
    }

    public Integer getSupplierIdByName(String supplierName) {
        return supplierMapper.selectIdByName(supplierName);

    }

    public List<Integer> getAllProductIds() {
        return productMapper.selectAllProductIds();
    }
}
