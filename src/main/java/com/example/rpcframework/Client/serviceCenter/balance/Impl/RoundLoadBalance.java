package com.example.rpcframework.Client.serviceCenter.balance.Impl;

import com.example.rpcframework.Client.serviceCenter.balance.LoadBalance;

import java.util.List;

public class RoundLoadBalance implements LoadBalance {
    int choose=-1;
    @Override
    public String balance(List<String> addressList) {
        choose++;
        System.out.println("负载均衡选择了"+choose+"号服务器.");
        choose=choose%addressList.size();
        return addressList.get(choose);
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {

    }
}
