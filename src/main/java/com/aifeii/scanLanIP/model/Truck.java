package com.aifeii.scanLanIP.model;

/**
 * 货车
 *
 * Created by JiaMing.Luo on 2017/3/24.
 */
public interface Truck<T extends Mineral> {

    /**
     * 卸货
     * @param mineral 货物
     */
    void onDischarge(T mineral);

}
