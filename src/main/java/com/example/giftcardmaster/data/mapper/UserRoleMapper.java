package com.example.giftcardmaster.data.mapper;

import com.example.giftcardmaster.data.dbentity.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserRoleMapper {

    @Select("select * from userrole")
    List<UserRoleEntity> getAllUserRoles();

    @Select("select * from userrole where id = #{id}")
    UserRoleEntity getUserRoleById(int id);
}
