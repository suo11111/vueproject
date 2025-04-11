package com.example.service;

import com.example.entity.Supplier;
import com.example.mapper.SupplierMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service//标识为SpringBoot的一个bean
public class SupplierService {

    @Resource
    private SupplierMapper supplierMapper;


    public List<Supplier> selectAll(Supplier supplier) {
        return supplierMapper.selectAll(supplier);
    }

    public List<Supplier> selectName(String name) {
        return supplierMapper.selectName(name);
    }




//分页查询
    public PageInfo<Supplier> selectPage(Supplier supplier, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Supplier> list = supplierMapper.selectAll(supplier);
        return PageInfo.of(list);
    }

    public void add(Supplier supplier) {
        supplierMapper.insert(supplier);
    }

    public void update(Supplier supplier) {
        supplierMapper.updateById(supplier);

    }

    public void deleteById(Integer id) {
        supplierMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for(Integer id : ids){
            this.deleteById(id);
        }
    }
}
