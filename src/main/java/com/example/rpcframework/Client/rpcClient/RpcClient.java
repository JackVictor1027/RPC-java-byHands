package com.example.rpcframework.Client.rpcClient;

import com.example.rpcframework.common.Message.RpcRequest;
import com.example.rpcframework.common.Message.RpcResponse;

public interface RpcClient {
    //定义底层通信的方法
    RpcResponse sendRequest(RpcRequest request);
}
