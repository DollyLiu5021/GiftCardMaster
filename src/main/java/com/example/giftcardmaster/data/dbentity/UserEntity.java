package com.example.giftcardmaster.data.dbentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private long id;

    private String username;

    private String email;

    private String password;

    private String phone;

    private int userRoleId;

    private String userRoleName;

    private int active;

    private Timestamp updateAt;

    private String updateBy;

    private Timestamp createAt;

    private String createBy;

    private long rowVersion;
}
