package com.example.mapper;

import com.example.entity.Supplier;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SupplierMapper {

    List<Supplier> selectAll(Supplier supplier);


    @Select("select * from suppliers where name=#{name}")
    List<Supplier> selectName(String name);

    void insert(Supplier supplier);

    void updateById(Supplier supplier);

    @Delete("delete from suppliers where id=#{id}")
    void deleteById(Integer id);

    @Select("SELECT id FROM suppliers WHERE name = #{supplierName}")
    Integer selectIdByName(String supplierName);
}
