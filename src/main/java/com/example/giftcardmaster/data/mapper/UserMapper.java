package com.example.giftcardmaster.data.mapper;

import com.example.giftcardmaster.data.dbentity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    @Select("select * from user")
    List<UserEntity> getAllUsers();

    @Select("select * from user where id = #{id}")
    UserEntity getUserById(long id);

    @Select("select * from user where username = #{username}")
    UserEntity getUserByUsername(String username);

    @Select("select * from user where email = #{email}")
    UserEntity getUserByEmail(String email);

    @Insert("insert into user(username, email, phone, password, userroleid, updateby, createby) " +
            "values(#{username}, #{email}, #{phone}, #{password}, #{userRoleId}, #{updateBy}, #{createBy}) ")
    void createUser(UserEntity userEntity);

    @Update("update user set username = #{username}, email = #{email}, phone = #{phone}, " +
            "updateby = #{updateBy}, rowversion = #{rowVersion}, password = #{password} " +
            "where id = #{id}")
    void updateUserInfo(UserEntity userEntity);
}
