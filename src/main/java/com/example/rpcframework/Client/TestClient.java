package com.example.rpcframework.Client;

import com.example.rpcframework.Client.proxy.ClientProxy;
import com.example.rpcframework.common.pojo.User;
import com.example.rpcframework.common.service.UserService;
import com.example.rpcframework.common.service.Impl.UserServiceImpl;

public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        ClientProxy clientProxy=new ClientProxy();
//        ClientProxy clientProxy=new ClientProxy("127.0.0.1",9999,0);
        UserService proxy=clientProxy.getProxy(UserService.class);

        User user = proxy.getUserByUserId(1);
        user = proxy.getUserByUserId(2);
        user = proxy.getUserByUserId(3);
        user = proxy.getUserByUserId(4);
        user = proxy.getUserByUserId(5);
        System.out.println("从服务端得到的user="+user.toString());

        User u=User.builder().id(100).userName("wxx").sex(true).build();
        Integer id = proxy.insertUserId(u);
        System.out.println("向服务端插入user的id"+id);
    }
}
