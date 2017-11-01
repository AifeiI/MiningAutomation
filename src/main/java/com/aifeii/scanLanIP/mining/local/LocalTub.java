package com.aifeii.scanLanIP.mining.local;

import com.aifeii.scanLanIP.model.Tub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by JiaMing.Luo on 2017/3/22.
 */
public class LocalTub implements Tub<LocalHostInfo> {

    @NotNull
    @Override
    public LocalHostInfo onDig(String ip) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);

            boolean isReachable = inetAddress.isReachable(3000);
            if (isReachable) {
                return new LocalHostInfo(inetAddress.getHostName(), ip, "", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LocalHostInfo(ip);
    }

    private String decodeMac(byte[] mac) {
        StringBuilder macBuilder = new StringBuilder();
        String temp;
        for (byte aMac : mac) {
            temp = Integer.toHexString(aMac & 0xFF);
            if (temp.length() == 1) {
                temp = '0' + temp;
            }
            macBuilder.append(temp).append("-");
        }

        return macBuilder.toString();
    }


}
