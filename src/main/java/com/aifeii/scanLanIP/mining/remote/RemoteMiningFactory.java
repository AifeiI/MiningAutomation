package com.aifeii.scanLanIP.mining.remote;

import com.aifeii.scanLanIP.mining.BaseMiningFactory;
import com.aifeii.scanLanIP.model.Tub;

/**
 * 外网矿厂
 *
 * Created by JiaMing.Luo on 2017/3/24.
 */
public class RemoteMiningFactory extends BaseMiningFactory<RemoteHostInfo> {

    /**
     * 矿车类型：HTTPs
     */
    public static final int TUB_TYPE_HTTPS = 1;

    /**
     * 矿车类型：SSL Socket
     */
    public static final int TUB_TYPE_SSL_SOCKET = 2;

    /**
     * 默认以 {@link #TUB_TYPE_HTTPS} 创建工厂
     */
    public RemoteMiningFactory() {
        this(TUB_TYPE_HTTPS);
    }

    /**
     * 选择 矿车类型 创建工厂
     *
     * @param tubType
     */
    public RemoteMiningFactory(int tubType) {
        this(buildTub(tubType));
    }

    private RemoteMiningFactory(Tub<RemoteHostInfo> tub) {
        super(tub);
    }

    private static Tub<RemoteHostInfo> buildTub(int type) {
        Tub<RemoteHostInfo> tub = null;
        switch (type) {
            case TUB_TYPE_HTTPS:
                tub = new HttpsTub();
                break;
            case TUB_TYPE_SSL_SOCKET:
                tub = new SSLSocketTub();
                break;

            default:
                break;
        }
        return tub;
    }
}
