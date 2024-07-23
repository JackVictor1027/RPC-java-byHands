package com.example.rpcframework.common.serializer.myCode;

import com.example.rpcframework.common.Message.MessageType;
import com.example.rpcframework.common.Message.RpcRequest;
import com.example.rpcframework.common.Message.RpcResponse;
import com.example.rpcframework.common.serializer.mySerializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.awt.*;

/*
*  依次按照自定义的消息格式写入，传入的数据为request或者response
*  需要持有一个serializer器，负责将传入的对象序列化成字节数组
* */
public class MyEncoder extends MessageToByteEncoder {
    private Serializer serializer;
    public MyEncoder(Serializer serializer){
        this.serializer=serializer;
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println(msg.getClass());
        //1.写入消息类型
        if(msg instanceof RpcRequest){
            out.writeShort(MessageType.REQUEST.getCode());
        } else if (msg instanceof RpcResponse) {
            out.writeShort(MessageType.RESPONSE.getCode());
        }
        //2.写入序列化方式
        out.writeShort(serializer.getType());
        //得到序列化数组
        byte[] serializeBytes = serializer.serialize(msg);
        //3.写入长度
        out.writeInt(serializeBytes.length);
        //4.写入序列化数组
        out.writeBytes(serializeBytes);
    }
}
