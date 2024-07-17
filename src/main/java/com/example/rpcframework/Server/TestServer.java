package com.example.rpcframework.Server;


import com.example.rpcframework.Server.provider.ServiceProvider;
import com.example.rpcframework.Server.server.Impl.SimpleRPCRPCServer;
import com.example.rpcframework.Server.server.RpcServer;
import com.example.rpcframework.common.service.Impl.UserServiceImpl;
import com.example.rpcframework.common.service.UserService;

public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);//注册服务

        //启用Rpc服务端
        RpcServer rpcServer=new SimpleRPCRPCServer(serviceProvider);
        rpcServer.start(9999);
    }
}
