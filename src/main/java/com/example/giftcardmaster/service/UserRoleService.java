package com.example.giftcardmaster.service;

import com.example.giftcardmaster.data.dbentity.UserRoleEntity;
import com.example.giftcardmaster.data.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    UserRoleMapper userRoleMapper;

    public List<UserRoleEntity> getAllUserRoles() {
        return userRoleMapper.getAllUserRoles();
    }

    public UserRoleEntity getById(int id) {
        return userRoleMapper.getUserRoleById(id);
    }
}
