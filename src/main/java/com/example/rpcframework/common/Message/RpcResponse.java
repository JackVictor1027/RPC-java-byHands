package com.example.rpcframework.common.Message;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RpcResponse implements Serializable {
    private int code;
    private String msg;
    private Object data;
    //构造成功信息
    public static RpcResponse susssess(Object data){
        return RpcResponse.builder().code(200).data(data).build();
    }
    //构造失败信息
    public static RpcResponse fail(){
        return RpcResponse.builder().code(500).msg("服务器发生错误").build();
    }
}
