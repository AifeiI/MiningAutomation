package com.aifeii.scanLanIP.mining.local;

import com.aifeii.scanLanIP.model.Mineral;

/**
 * Created by JiaMing.Luo on 2017/3/22.
 */
public class LocalHostInfo implements Mineral {

    private String mac;
    private String name;
    private String ip;

    private boolean isSurvive;

    public LocalHostInfo() {
    }

    public LocalHostInfo(String ip) {
        this.ip = ip;
    }

    public LocalHostInfo(String name, String ip, String mac, boolean isSurvive) {
        this.name = name;
        this.ip = ip;
        this.mac = mac;
        this.isSurvive = isSurvive;
    }

    public String getMac() {
        return mac;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAddress() {
        return ip;
    }

    @Override
    public boolean isNotEmpty() {
        return isSurvive;
    }

    @Override
    public String toString() {
        return "LocalHostInfo{" +
                "ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", isNotEmpty=" + isSurvive +
                '}';
    }
}
