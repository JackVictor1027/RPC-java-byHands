package com.example.rpcframework.Server.netty.handler;

import com.example.rpcframework.Server.provider.ServiceProvider;
import com.example.rpcframework.common.Message.RpcRequest;
import com.example.rpcframework.common.Message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NettyRPCServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private ServiceProvider serviceProvider;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        RpcResponse response=getResponse(request);
        ctx.writeAndFlush(response);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private RpcResponse getResponse(RpcRequest rpcRequest){
        //得到服务名
        String interfaceName = rpcRequest.getInterfaceName();//TODO:getInterfaceName()方法可以明确地获取到该请求所对应的接口名，从而更加准确地确定要处理的请求类型。
        //得到服务端相应服务实现类
        Object service = serviceProvider.getService(interfaceName);
        //反射调用方法
        Method method=null;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsType());
            //正式启用方法(后端的哪一项服务，请求携带的参数)
            Object invoke = method.invoke(service, rpcRequest.getParams());
            return RpcResponse.susssess(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail();
        }
    }
}
