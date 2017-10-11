package com.aifeii.scanLanIP.mining;

import com.aifeii.scanLanIP.model.Truck;
import com.aifeii.scanLanIP.model.Tub;

/**
 * 矿场 与 仓库 的运输线
 * <p>
 * Created by JiaMing.Luo on 2017/3/22.
 */
public class AssemblyLine<T> implements Runnable {

    /**
     * 矿洞地址
     */
    private String address;
    private Tub<T> tub;
    private Truck<T> truck;

    /**
     * 构建运输线
     *
     * @param address     矿洞地址
     * @param tub         矿车
     * @param truck 运输车
     */
    public AssemblyLine(String address, Tub<T> tub, Truck<T> truck) {
        this.address = address;
        this.tub = tub;
        this.truck = truck;
    }

    @Override
    public void run() {
        // 挖矿
        T host = tub.onDig(address);

        // 加工
        if (truck != null) {
            // 运输
            if (host == null) {
                System.out.println("空矿洞 [" + address + "]");
                return;
            }
            truck.onDischarge(host);
        }
    }
}
