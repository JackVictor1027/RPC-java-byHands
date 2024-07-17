package com.example.rpcframework.Client;

import com.example.rpcframework.common.Message.RpcRequest;
import com.example.rpcframework.common.Message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOClient {
    //这里负责底层与服务端的通信，发送request,返回response
    public static RpcResponse sendRequest(String host, int port, RpcRequest request){
        try {
            Socket socket = new Socket(host, port);
            ObjectOutputStream oss=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());

            oss.writeObject(request);
            oss.flush();

            RpcResponse Response=(RpcResponse) ois.readObject();
            return Response;
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
}
