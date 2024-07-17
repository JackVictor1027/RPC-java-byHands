package com.example.rpcframework.Client.proxy;

import com.example.rpcframework.Client.IOClient;
import com.example.rpcframework.common.Message.RpcRequest;
import com.example.rpcframework.common.Message.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    //传入参数service接口的class对象，反射封装成一个request
    private String host;
    private int port;

    //jdk动态代理，代理类调用服务提供者的方法时，会自动执行该方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建request
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsType(method.getParameterTypes()).build();
        //IOClient.sendRequest 和服务端进行数据传输
        RpcResponse Response= IOClient.sendRequest(host,port,request);
        return Response.getData();
    }

    public <T>T getProxy(Class<T> clazz) throws NullPointerException{
        Object o= Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},this);
        return (T)o;
    }
}
