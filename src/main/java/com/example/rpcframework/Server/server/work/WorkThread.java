package com.example.rpcframework.Server.server.work;

import com.example.rpcframework.Server.provider.ServiceProvider;
import com.example.rpcframework.common.Message.RpcRequest;
import com.example.rpcframework.common.Message.RpcResponse;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
public class WorkThread implements Runnable{
    private Socket socket;
    private ServiceProvider serviceProvider;

    @Override
    public void run() {
        try {
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //读取客户端传过来的request
            RpcRequest rpcRequest = (RpcRequest) ois.readObject();
            //反射调用服务方法获取返回值
            RpcResponse RpcResponse = getResponce(rpcRequest);
            //向客户端写入responce
            oos.writeObject(RpcResponse);
            oos.flush();//清理缓存，不妨碍下次输入
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
    private RpcResponse getResponce(RpcRequest rpcRequest){
        //得到服务名
        String interfaceName = rpcRequest.getInterfaceName();
        //得到服务端相应服务实现类
        Object service = serviceProvider.getService(interfaceName);
        //反射调用方法
        Method method=null;

        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName());
            Object invoke = null;
            invoke = method.invoke(service, rpcRequest.getParams());
            return RpcResponse.susssess(invoke);
        } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail();
        }
    }


}
