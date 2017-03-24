package com.aifeii.scanLanIP.util.sslCertificateUtil;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;

/**
 * Created by JiaMing.Luo on 2017/3/24.
 */
public class GetCAMsgUtil {
    public static final String CA_TYPE = "X.509";
    public static final String HTTPS = "https://";
    public static final int TIMEOUT_CONNECTION = 2000;
    public static final int TIMEOUT_READ = 2000;
    private static GetCAMsgUtil mCaMsgUtil;

    private GetCAMsgUtil() {
        trustAllHttpsCertificates();

        HostnameVerifier hv = getHostnameVerifier();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    public static GetCAMsgUtil getInstance() {
        if (mCaMsgUtil == null) {
            mCaMsgUtil = new GetCAMsgUtil();
        }
        return mCaMsgUtil;
    }

    public String getMsgFromSSLSocket(String domain) {
        return getMsgFromSSLSocket(domain, 0);
    }

    public String getMsgFromSSLSocket(String domain, int port) {
        if (port == 0) {
            port = 443;
        }
        String msg = null;
        SSLSocket sslSocket = null;
        try {
            SSLSocketFactory sslSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
            sslSocket = (SSLSocket) sslSocketFactory.createSocket(domain, port);
            sslSocket.setSoTimeout(TIMEOUT_CONNECTION);
            sslSocket.startHandshake();
            SSLSession sslSession = sslSocket.getSession();
            msg = subString(caToString(sslSession.getPeerCertificates()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sslSocket != null) {
                    sslSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    public String getMsgFromHttps(String domain) {
        return getMsgFromHttps(domain, 0);
    }

    public String getMsgFromHttps(String domain, int port) {
        HttpsURLConnection httpsURLConnection = null;
        String msg = null;
        try {
            URL url;
            if (port == 0) {
                url = new URL(HTTPS + domain);
            } else {
                url = new URL(HTTPS + domain + ":" + port);
            }
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
            httpsURLConnection.setConnectTimeout(TIMEOUT_CONNECTION);
            httpsURLConnection.setReadTimeout(TIMEOUT_READ);
            httpsURLConnection.connect();
            msg = subString(caToString(httpsURLConnection.getServerCertificates()));
        } catch (SSLException e) {
            System.out.println("error: " + e.getMessage() + ", domain: " + domain + ":" + port);
        } catch (SocketTimeoutException e) {
        } catch (MalformedURLException e) {
            System.out.println("error: " + e.getMessage() + ", domain: " + domain + ":" + port);
        } catch (ConnectException e) {
            System.out.println("error: " + e.getMessage() + ", domain: " + domain + ":" + port);
        } catch (SocketException e) {
            System.out.println("error: " + e.getMessage() + ", domain: " + domain + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }
        return msg;
    }

    private String subString(String caMsg) {
        int startIndex = caMsg.indexOf("=") + 1;
        int ensIndex = caMsg.indexOf(",", startIndex);
        return caMsg.substring(startIndex, ensIndex);
    }

    private String caToString(Certificate[] certificate) {
        StringBuffer tmpBuffer = null;
        ByteArrayInputStream inputStream = null;
        try {
            tmpBuffer = new StringBuffer();
            for (int i = 0; i < certificate.length; i++) {
                inputStream = new ByteArrayInputStream(certificate[i].getEncoded());
                CertificateFactory certificateFactory = CertificateFactory.getInstance(CA_TYPE);
                Collection<? extends Certificate> collection = certificateFactory.generateCertificates(inputStream);
                tmpBuffer.append(collection.toString());
            }
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tmpBuffer.toString();
    }

    private static HostnameVerifier getHostnameVerifier() {
        return (urlHostName, session) -> true;
    }

    private void trustAllHttpsCertificates() {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new CertificateCheckManager();

        trustAllCerts[0] = tm;
        SSLContext sc;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
