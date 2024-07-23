package com.example.rpcframework.common.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RpcResponse implements Serializable {
    //状态信息
    private int code;
    private String msg;
    //加入传输数据的类型，以便在自定义序列化器中解析
    private Class<?> dataType;
    //具体数据
    private Object data;
    public static RpcResponse susssess(Object data){
        return RpcResponse.builder().code(200).data(data).build();
    }
    //构造失败信息
    public static RpcResponse fail(){
        return RpcResponse.builder().code(500).msg("服务器发生错误").build();
    }
}
