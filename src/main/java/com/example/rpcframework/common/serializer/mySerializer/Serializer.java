package com.example.rpcframework.common.serializer.mySerializer;

public interface Serializer {
    //把对象序列化字节数组
    byte[] serialize(Object obj);
    //从字节数组反序列化消息，使用Java自带序列化方式不用messageType也能得到相应的对象（序列化字节数组里包含类信息）
    //其他方式需指定消息格式，再根据message转化成相应的对象
    Object deserialize(byte[] bytes,int messageType);
    //返回的序列器，是哪个
    // 0:java自带序列化方式，1：json序列化方式
    int getType();
    //根据序号取出序列化器，暂时有两种实现方式，需要其他方式，实现这个接口即可
    static Serializer getSerializer(int type){
        switch (type){
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
