package com.example.rpcframework.common.service.Impl;

import com.example.rpcframework.common.pojo.User;
import com.example.rpcframework.common.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("客户端查询了id为"+"id"+"的用户.");
        //模拟从数据库中取用户的行为
        Random random=new Random();
        User user=User.builder().userName(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean())
                .build();
        return user;
    }

    @Override
    public Integer insertUserId(User user) {
        return user.getId();
    }
}
