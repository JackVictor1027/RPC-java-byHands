package com.example.rpcframework.Client;

import com.example.rpcframework.Client.proxy.ClientProxy;
import com.example.rpcframework.common.pojo.User;
import com.example.rpcframework.common.service.UserService;
import com.example.rpcframework.common.service.Impl.UserServiceImpl;

public class TestClient {
    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy("127.0.0.1",9999);
        UserService proxy = clientProxy.getProxy(UserService.class);

        User user = proxy.getUserByUserId(1);
//        User user=User.builder().id(1).userName("张三").sex(true).build();
        System.out.println("从服务端得到的user="+user.toString());

        User u=User.builder().id(100).userName("jack").sex(true).build();
        Integer id=proxy.insertUserId(u);
        System.out.println("向服务端插入user的id"+id);
    }
}
