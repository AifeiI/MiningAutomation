package com.aifeii.scanLanIP.mining.remote;

import com.aifeii.scanLanIP.model.Tub;
import com.aifeii.scanLanIP.util.sslCertificateUtil.GetCAMsgUtil;

/**
 * Created by JiaMing.Luo on 2017/3/24.
 */
public class HttpsTub implements Tub<RemoteHostInfo> {

    @Override
    public RemoteHostInfo onDig(String address) {
        String ca = GetCAMsgUtil.getInstance().getMsgFromHttps(address);
        if (ca != null) {
            return new RemoteHostInfo(address, ca, true);
        }
        return new RemoteHostInfo(address);
    }

}
