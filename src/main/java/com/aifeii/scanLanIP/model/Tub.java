package com.aifeii.scanLanIP.model;

import org.jetbrains.annotations.NotNull;

/**
 * 挖矿车
 *
 * Created by JiaMing.Luo on 2017/3/24.
 */
public interface Tub<T extends Mineral> {

    /**
     * 前往矿洞挖矿
     *
     * @param address 矿洞地址
     * @return 矿物
     */
    @NotNull
    T onDig(String address);

}
