package com.aifeii.scanLanIP.mining.remote;

import com.aifeii.scanLanIP.model.Tub;
import com.aifeii.scanLanIP.util.sslCertificateUtil.GetCAMsgUtil;

/**
 * Created by JiaMing.Luo on 2017/3/24.
 */
public class SSLSocketTub implements Tub<RemoteHostInfo> {

    @Override
    public RemoteHostInfo onDig(String ip) {
        String ca = GetCAMsgUtil.getInstance().getMsgFromSSLSocket(ip);
        if (ca != null) {
            return new RemoteHostInfo(ip, ca, true);
        }
        return new RemoteHostInfo(ip);
    }

}
