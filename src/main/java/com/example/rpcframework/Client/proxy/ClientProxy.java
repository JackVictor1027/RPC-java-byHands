package com.example.rpcframework.Client.proxy;

import com.example.rpcframework.Client.IOClient;
import com.example.rpcframework.Client.circuitBreaker.CircuitBreaker;
import com.example.rpcframework.Client.circuitBreaker.CircuitBreakerProvider;
import com.example.rpcframework.Client.retry.GuavaRetry;
import com.example.rpcframework.Client.rpcClient.Impl.NettyRpcClient;
import com.example.rpcframework.Client.rpcClient.Impl.SimpleSocketRpcCilent;
import com.example.rpcframework.Client.rpcClient.RpcClient;
import com.example.rpcframework.Client.serviceCenter.ServiceCenter;
import com.example.rpcframework.Client.serviceCenter.ZKServiceCenter;
import com.example.rpcframework.Server.server.Impl.SimpleRPCRPCServer;
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
    private RpcClient rpcClient;
    private ServiceCenter serviceCenter;
    private CircuitBreakerProvider circuitBreakerProvider;
    public ClientProxy() throws InterruptedException {
        // 初始化 RpcClient，并处理可能的异常
        try {
            serviceCenter=new ZKServiceCenter();
            this.rpcClient=new NettyRpcClient(serviceCenter);
            this.circuitBreakerProvider=new CircuitBreakerProvider();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize RpcClient", e);
        }
    }
    //jdk动态代理，代理类调用服务提供者的方法时，会自动执行该方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            switch (method.getName()) {
                case "toString":
                    return proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy)) +
                            ", with InvocationHandler " + this;
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "equals":
                    return proxy == args[0];
                default:
                    throw new IllegalStateException("Unexpected method: " + method);
            }
        }
        //构建request
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsType(method.getParameterTypes()).build();
        //获取熔断器
        CircuitBreaker circuitBreaker=circuitBreakerProvider.getCircuitBreaker(method.getName());
        //检查请求端是否需要熔断
        if(!circuitBreaker.allowRequest()){
            //这里可以对熔断做特殊处理
            return null;
        }
        //数据传输
        RpcResponse response;
        //后续添加逻辑：为保持幂等性，只对白名单上的服务进行重试
        if(serviceCenter.checkRetry(request.getInterfaceName())){
            //调用retry框架进行重试操作
            response=new GuavaRetry().sendServiceWithRetry(request,rpcClient);
        }else{
            //只调用一次
            response = rpcClient.sendRequest(request);
        }
        //记录response的状态，上报给熔断器
        if(response.getCode() == 200)circuitBreaker.recordSuccess();
        else if(response.getCode() == 500)circuitBreaker.recordFailure();
        //IOClient.sendRequest 和服务端进行数据传输
        //RpcResponse response= rpcClient.sendRequest(request);
        return response.getData();
    }

    @Override
    public String toString() {
        return "ClientProxy{" +
                "rpcClient=" + rpcClient +
                '}';
    }

    public <T>T getProxy(Class<T> clazz) throws NullPointerException{
        Object o= Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},this);
        return (T)o;
    }

}
