package com.aifeii.scanLanIP.model;

import java.util.List;

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
    void start(String target) throws Exception;

    /**
     * 查看仓库
     *
     * @return
     */
    List<T> viewWarehouse();

    /**
     * 停止运作
     *
     * @throws Exception 停止异常
     */
    void shutdown() throws Exception;
}
