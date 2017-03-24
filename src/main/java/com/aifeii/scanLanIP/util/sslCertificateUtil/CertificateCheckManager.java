package com.aifeii.scanLanIP.util.sslCertificateUtil;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by JiaMing.Luo on 2017/3/24.
 */
public class CertificateCheckManager implements TrustManager, X509TrustManager {
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public boolean isServerTrusted(X509Certificate[] certs) {
        return true;
    }

    public boolean isClientTrusted(X509Certificate[] certs) {
        return true;
    }

    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
    }

    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
    }
}
