package com.aifeii.scanLanIP.model;

/**
 * 主机
 *
 * Created by JiaMing.Luo on 2017/3/24.
 */
public interface Mineral {

    /**
     * 获取矿物名称
     *
     * @return
     */
    String getName();

    /**
     * 获取矿洞地址
     *
     * @return
     */
    String getAddress();

    /**
     * 是否空洞
     *
     * @return 是则返回 false ，否则返回 true
     */
    boolean isNotEmpty();

}
