package com.example.giftcardmaster.service;

import com.example.giftcardmaster.data.dbentity.UserEntity;
import com.example.giftcardmaster.data.mapper.UserMapper;
import com.example.giftcardmaster.util.ConstUtil;
import com.example.giftcardmaster.util.DataUtil;
import com.example.giftcardmaster.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    // --------- Getter ---------//

    public List<UserEntity> getAllUsers() {
        return userMapper.getAllUsers();
    }

    public UserEntity getByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    public String getCountryCodeBasedOnIp(String ipAddress) {
        // If cannot get user ip, return default value US
        if (ipAddress == null || ipAddress.isEmpty())
            return "US";

        String apiResponse = HttpUtil.sendGet("http://ip-api.com/json/" + ipAddress);
        if (apiResponse.equals("error"))
            return "US";

        Map map = DataUtil.jsonStr2Map(apiResponse);
        if (map.get("status").equals("fail"))
            return "US";

        return (String) map.get("countryCode");
    }

    // --------- Validator ---------//

    public String validateUserRegisterData(String username, String password, String email) {
        if (userMapper.getUserByUsername(username) != null)
            return "Username Already Exist";
        else if (userMapper.getUserByEmail(email) != null)
            return "Email Already Exist";

        return ConstUtil.SUCCESS;
    }

    public String validateUserEmail(String email) {
        if (userMapper.getUserByEmail(email) != null)
            return "Email Already Exist";
        return ConstUtil.SUCCESS;
    }

    // --------- Create ---------//

    public void createUserEntity(String username, String password, String email) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setUserRoleId(1);
        user.setCreateBy(user.getUsername());
        user.setUpdateBy(null);

        userMapper.createUser(user);
    }

    // --------- Updates ---------//

    public void updateUserInfo(UserEntity userEntity) {
        userEntity.setUpdateBy(userEntity.getUsername());
        userEntity.setRowVersion(userEntity.getRowVersion()+1);
        userMapper.updateUserInfo(userEntity);
    }
}
