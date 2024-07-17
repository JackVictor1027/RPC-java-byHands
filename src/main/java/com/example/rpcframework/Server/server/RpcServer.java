package com.example.rpcframework.Server.server;

public interface RpcServer {
    //开启监听
    //这是一个通用的服务接口
    void start(int port);
    void stop();
}
