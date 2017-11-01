package com.aifeii.scanLanIP.model;

import io.reactivex.Flowable;

/**
 * 矿业工厂
 *
 * Created by JiaMing.Luo on 2017/3/24.
 */
public interface MiningFactory<T extends Mineral> {

    /**
     * 开始运作
     *
     * @param target 矿场地址
     * @throws Exception 运作异常
     */
    Flowable<T> start(String... target) throws Exception;

}
