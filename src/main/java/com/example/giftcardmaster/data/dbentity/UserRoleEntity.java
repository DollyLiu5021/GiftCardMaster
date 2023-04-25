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
public class UserRoleEntity {

    private int id;

    private String name;

    private String description;

    private Timestamp updateAt;

    private String updateBy;

    private Timestamp createAt;

    private String createBy;

    private long rowVersion;
}
