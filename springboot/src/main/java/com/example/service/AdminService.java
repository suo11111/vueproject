package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Resource
    private AdminMapper adminMapper;

    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    //分页查询
    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    public void add(Admin admin) {
        String username = admin.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if(dbAdmin!=null){
            throw new CustomException("400","店长已存在");
        }
        if(StrUtil.isBlank(admin.getPassword())){//密码为空时
            admin.setPassword("admin");//默认密码
        }
        if(StrUtil.isBlank(admin.getName())){//名称为空时
            admin.setName(admin.getUsername());//默认名称
        }
        //一定要设计角色
        admin.setRole("ADMIN");//默认角色

        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);

    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }
    public void deleteBatch(List<Integer> ids) {
        for(Integer id : ids){
            this.deleteById(id);
        }
    }

    public Admin login(Account account) {
        String username = account.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if(dbAdmin==null){
            throw new CustomException("400","用户名不存在");
        }
        String password = account.getPassword();
        if(!dbAdmin.getPassword().equals(password)){
            throw new CustomException("401","账号或密码错误");
        }
        return dbAdmin;
    }


    public void updatePassword(Account account) {
        Integer id = account.getId();
        Admin admin = this.selectById(id);
        if (!admin.getPassword().equals(account.getPassword())) {//旧密码不正确
            throw new CustomException("401", "原密码错误");
        }
        admin.setPassword(account.getNewPassword());
        this.update(admin);
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }
}
