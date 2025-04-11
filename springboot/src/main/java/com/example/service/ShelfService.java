package com.example.service;

import com.example.entity.Shelf;
import com.example.mapper.ShelfMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service//标识为SpringBoot的一个bean
public class ShelfService {

    @Resource
    private ShelfMapper shelfMapper;


    public List<Shelf> selectAll(Shelf shelf) {
        return shelfMapper.selectAll(shelf);
    }

    public List<Shelf> selectLocation(String location) {
        return shelfMapper.selectLocation(location);
    }


//分页查询
    public PageInfo<Shelf> selectPage(Shelf shelf, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shelf> list = shelfMapper.selectAll(shelf);
        return PageInfo.of(list);
    }

    public void add(Shelf shelf) {
        shelfMapper.insert(shelf);
    }

    public void update(Shelf shelf) {
        shelfMapper.updateById(shelf);

    }

    public void deleteById(Integer id) {
        shelfMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for(Integer id : ids){
            this.deleteById(id);
        }
    }
}
