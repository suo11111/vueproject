package com.example.mapper;

import com.example.entity.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;


import java.util.List;

public interface ProductMapper {

    List<Product> selectAll(Product product);


    @Select("select * from products where product_name=#{name}")
    List<Product> selectName(String name);

    void insert(Product product);

    void updateById(Product product);

    @Delete("delete from products where product_id=#{id}")
    void deleteById(Integer id);

    @Select("SELECT product_id FROM products")
    List<Integer> selectAllProductIds();

    List<Product> selectByStockQuantity(Integer stockQuantity);

}
