package com.example.rpcframework.Server.provider;
import java.util.HashMap;
import java.util.Map;
public class ServiceProvider {
    private Map<String, Object> interfaceProvider;

    public ServiceProvider(){
        this.interfaceProvider=new HashMap<>();//map实例化
    }

    //本地注册服务
    public void provideServiceInterface(Object service){
        String serviceName=service.getClass().getName();
        Class<?>[] interfaceName = service.getClass().getInterfaces();

        //遍历接口名称列表，为每个接口注册服务实现
        for (Class<?> clazz:interfaceName){
            interfaceProvider.put(clazz.getName(),service);
        }
    }

    //获取服务实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
