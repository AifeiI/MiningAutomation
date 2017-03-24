package com.aifeii.scanLanIP.model;

/**
 * 货车
 *
 * Created by JiaMing.Luo on 2017/3/24.
 */
public interface Truck<T> {

    /**
     * 卸货
     * @param t 货物
     */
    void onDischarge(T t);

}
