package com.example.mapper;

import com.example.entity.Shelf;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ShelfMapper {

    List<Shelf> selectAll(Shelf shelf);


    @Select("select * from shelves where location=#{location}")
    List<Shelf> selectLocation(String location);

    void insert(Shelf shelf);

    void updateById(Shelf shelf);

    @Delete("delete from shelves where id=#{id}")
    void deleteById(Integer id);
}
