package com.aifeii.scanLanIP.mining.local;

import com.aifeii.scanLanIP.mining.BaseMiningFactory;
import com.aifeii.scanLanIP.model.Tub;

/**
 * 内网矿厂
 *
 * Created by JiaMing.Luo on 2017/3/23.
 */
public class LocalMiningFactory extends BaseMiningFactory<LocalHostInfo> {

    public LocalMiningFactory() {
        super(new LocalTub());
    }
}
