package com.example.rpcframework.Client.retry;

import com.example.rpcframework.Client.rpcClient.RpcClient;
import com.example.rpcframework.common.Message.RpcRequest;
import com.example.rpcframework.common.Message.RpcResponse;
import com.github.rholder.retry.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GuavaRetry {
    private RpcClient rpcClient;
    public RpcResponse sendServiceWithRetry(RpcRequest request,RpcClient rpcClient){
        this.rpcClient=rpcClient;
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                //无论出现什么异常，都进行重试
                .retryIfException()
                //返回结果为error时进行重试
                .retryIfResult(response -> Objects.equals(response.getCode(),500))
                //重试等待策略：等待2s后进行重试
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
                //重试停止策略：重试达到3次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        System.out.println("RetryListener:第"+attempt.getAttemptNumber()+"次调用");
                    }
                }).build();

        try {
            return retryer.call(()->rpcClient.sendRequest(request));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RpcResponse.fail();
    }
}
