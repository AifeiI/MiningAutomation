package com.aifeii.scanLanIP.mining.remote;

import com.aifeii.scanLanIP.model.Mineral;

/**
 * Created by JiaMing.Luo on 2017/3/24.
 */
public class RemoteHostInfo implements Mineral {

    private String ip;
    private String ca;
    private boolean isSurvive;

    public RemoteHostInfo() {
    }

    public RemoteHostInfo(String ip) {
        this.ip = ip;
    }

    public RemoteHostInfo(String ip, String ca, boolean isSurvive) {
        this.ip = ip;
        this.ca = ca;
        this.isSurvive = isSurvive;
    }


    @Override
    public String getName() {
        return ca;
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
        return "RemoteHostInfo{" +
                "ip='" + ip + '\'' +
                ", ca='" + ca + '\'' +
                ", isNotEmpty=" + isSurvive +
                '}';
    }
}
