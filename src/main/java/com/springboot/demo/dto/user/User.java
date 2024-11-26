package com.springboot.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private String name;
    private String avatar;
    private String userid;
    private String email;
    private String signature;
    private String title;
    private String group;
    private List<UserTag> tags;
    private int notifyCount;
    private int unreadCount;
    private String country;
    private String access;
    private Geographic geographic;
    private String address;
    private String phone;
}
