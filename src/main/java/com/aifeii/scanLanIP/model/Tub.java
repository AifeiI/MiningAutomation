package com.aifeii.scanLanIP.model;

/**
 * 挖矿车
 *
 * Created by JiaMing.Luo on 2017/3/24.
 */
public interface Tub<T> {

    /**
     * 前往矿洞挖矿
     *
     * @param address 矿洞地址
     * @return 矿物
     */
    T onDig(String address);

}
