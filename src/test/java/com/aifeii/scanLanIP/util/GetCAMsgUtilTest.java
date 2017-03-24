package com.aifeii.scanLanIP.util;

import com.aifeii.scanLanIP.util.sslCertificateUtil.GetCAMsgUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by JiaMing.Luo on 2017/3/24.
 */
public class GetCAMsgUtilTest {
    @Test
    public void getMsgFromSSLSocket() throws Exception {
        String ca = GetCAMsgUtil.getInstance().getMsgFromSSLSocket("www.google.com");
        System.out.println("ca: " + ca);
        assertNotNull(ca);
    }

    @Test
    public void getMsgFromHttps() throws Exception {
        String ca = GetCAMsgUtil.getInstance().getMsgFromHttps("www.google.com");
        System.out.println("ca: " + ca);
        assertNotNull(ca);
    }

}