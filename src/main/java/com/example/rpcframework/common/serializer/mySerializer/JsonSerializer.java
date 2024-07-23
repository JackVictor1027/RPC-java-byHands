package com.example.rpcframework.common.serializer.mySerializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.rpcframework.common.Message.RpcRequest;
import com.example.rpcframework.common.Message.RpcResponse;

public class JsonSerializer implements Serializer{
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = JSONObject.toJSONBytes(obj);
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj=null;
        //传输的消息分为request与response
        switch (messageType)
        {
            case 0:
                RpcRequest request= JSON.parseObject(bytes,RpcRequest.class);//TODO:这还只是个空壳
                Object[] objects = new Object[request.getParams().length];
                // 把json字串转化成对应的对象，fastjson可以读出基本数据类型，不用转化
                // 对转换后的request中的params属性逐个类型判断
                for (int i = 0; i < objects.length; i++) {
                    Class<?> paramsType = request.getParamsType()[i];//TODO:手写的转化
                    //判断每个对象类型是否和paramsTypes中的一致
                    // 因为请求参数类型有很多种(Query,Body:body,form-data,binary...)
                    if(!paramsType.isAssignableFrom(request.getParams()[i].getClass())){//TODO
                        //如果不一致，就进行类型转换
                        objects[i] = JSONObject.toJavaObject((JSONObject)request.getParams()[i],request.getParamsType()[i]);
                    }else{
                        //如果一致就直接赋给objects[i]
                        objects[i] = request.getParams()[i];
                    }
                }
                request.setParams(objects);
                obj=request;
                break;
            case 1:
                RpcResponse response = JSON.parseObject(bytes, RpcResponse.class);
                Class<?> dataType = response.getDataType();
                //判断转化后的response对象中的data类型是否正确
                if (!dataType.isAssignableFrom(response.getData().getClass())){
                    response.setData(JSONObject.toJavaObject((JSONObject)response.getData(),dataType));
                }
                obj=response;
                break;
            default:
                System.out.println("暂不支持此种消息");
                throw new RuntimeException();
        }
        return obj;
    }

    //1.代表json序列化方式
    @Override
    public int getType() {
        return 1;
    }
}
