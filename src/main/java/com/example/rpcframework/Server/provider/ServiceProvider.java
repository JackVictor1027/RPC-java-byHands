package com.example.rpcframework.Server.provider;
import com.example.rpcframework.Server.serviceRegister.Impl.ZKServiceRegister;
import com.example.rpcframework.Server.serviceRegister.ServiceRegister;
import io.netty.resolver.InetSocketAddressResolver;

import javax.imageio.spi.ServiceRegistry;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
public class ServiceProvider {
    private Map<String, Object> interfaceProvider;
    //注册服务类
    private ServiceRegister serviceRegister;
    private String host;
    private int port;
    public ServiceProvider(String host,int port){
        //需要传入服务端自身的网络地址
        this.host=host;
        this.port=port;
        this.interfaceProvider=new HashMap<>();//map实例化
        this.serviceRegister=new ZKServiceRegister();
    }

    //本地注册服务
    public void provideServiceInterface(Object service,boolean canRetry){
        String serviceName=service.getClass().getName();
        Class<?>[] interfaceName = service.getClass().getInterfaces();

        //遍历接口名称列表，为每个接口注册服务实现
        for (Class<?> clazz:interfaceName){
            //本地的映射表
            interfaceProvider.put(clazz.getName(),service);
            //在注册中心注册服务
            serviceRegister.register(clazz.getName(),new InetSocketAddress(host,port),canRetry);
        }
    }

    //获取服务实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
