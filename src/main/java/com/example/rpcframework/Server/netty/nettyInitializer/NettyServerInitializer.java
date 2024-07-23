package com.example.rpcframework.Server.netty.nettyInitializer;

import com.example.rpcframework.Client.netty.handler.NettyClientHandler;
import com.example.rpcframework.Server.provider.ServiceProvider;
import com.example.rpcframework.common.serializer.myCode.MyDecoder;
import com.example.rpcframework.common.serializer.myCode.MyEncoder;
import com.example.rpcframework.common.serializer.mySerializer.JsonSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //使用自定义的编/解码器
        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new MyDecoder());

        pipeline.addLast(new NettyClientHandler(serviceProvider));
    }
}
