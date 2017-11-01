package com.aifeii.scanLanIP.mining;

import com.aifeii.scanLanIP.model.Mineral;
import com.aifeii.scanLanIP.model.MiningFactory;
import com.aifeii.scanLanIP.model.Tub;
import com.aifeii.scanLanIP.util.HostNumUtil;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

/**
 * 矿厂标准
 * <p>
 * Created by JiaMing.Luo on 2017/3/24.
 */
public abstract class BaseMiningFactory<T extends Mineral> implements MiningFactory {

    /**
     * 矿车
     */
    private Tub<T> mTub;

    /**
     * 初始化矿业工厂
     *
     * @param tub 矿车
     */
    public BaseMiningFactory(Tub<T> tub) {
        // 制造矿车
        this.mTub = tub;
    }

    @Override
    public Flowable<T> start(String... targets) throws Exception {
        return Flowable.fromArray(targets) // 工厂
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(target -> {
                    if (target.indexOf('/') != -1) {
                        String[] sp = target.split("/");
                        if (sp.length != 2) {
                            // 参数错误
                            throw new IllegalArgumentException("IP format is error");
                        }
                        String ips = sp[0];
                        int mark = Integer.parseInt(sp[1]);
                        // 矿场编号
                        int[] ipNum = HostNumUtil.getStartHostIP(ips, mark);
                        // 判断矿场有几个矿洞
                        long count = HostNumUtil.countHost(ips, mark);

                        /// ---------------- 开始调度 ---------------- ///
                        String[] ipArray = new String[(int) count];
                        ipNum[3]++;
                        for (int i = 0; i < count; i++) {
                            ipArray[i] = ipNum[0] + "." + ipNum[1] + "." + ipNum[2] + "." + ipNum[3];

                            ipNum[3]++;
                            if (ipNum[3] == 256) {
                                ipNum[3] = 0;
                                ipNum[2]++;
                                if (ipNum[2] == 256) {
                                    break;
                                }
                            }
                        }

                        return ipArray;
                    } else {
                        return new String[]{target};
                    }
                }) // 建立矿场
                .flatMap(new AssemblyLine<>(mTub)) // 运输线
                .observeOn(Schedulers.io());
    }

}
