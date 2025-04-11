package com.example.mapper;

import com.example.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper {
    List<Admin> selectAll(Admin admin);
    void insert(Admin admin);

    void updateById(Admin admin);

    @Delete("delete from admin where id=#{id}")
    void deleteById(Integer id);

    @Select("select * from admin where name=#{name}")
    List<Admin> selectName(String name);

    @Select("select * from admin where username=#{username}")
    Admin selectByUsername(String username);

    @Select("select * from admin where id=#{id}")
    Admin selectById(Integer id);
}
