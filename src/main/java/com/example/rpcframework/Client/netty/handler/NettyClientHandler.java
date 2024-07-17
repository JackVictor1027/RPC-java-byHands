package com.example.rpcframework.Client.netty.handler;

import com.example.rpcframework.Server.provider.ServiceProvider;
import com.example.rpcframework.common.Message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private ServiceProvider serviceProvider;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponce) throws Exception {
        //接收到response,给channel设计别名，让sendRequest里读取response
        AttributeKey<Object> key = AttributeKey.valueOf("RPCResponse");
        ctx.channel().attr(key).set(rpcResponce);
        ctx.channel().close();

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常处理
        cause.printStackTrace();
        ctx.close();
    }
}
