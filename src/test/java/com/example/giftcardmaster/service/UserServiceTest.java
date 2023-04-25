package com.example.giftcardmaster.service;

import com.example.giftcardmaster.data.dbentity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    private void getAllUsersTest() {
        List<UserEntity> userList = userService.getAllUsers();
        System.out.println(userList.size());
    }
}
