package com.aifeii.scanLanIP.mining;

import com.aifeii.scanLanIP.model.Mineral;
import com.aifeii.scanLanIP.model.Truck;
import com.aifeii.scanLanIP.model.Tub;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

/**
 * 矿场 与 仓库 的运输线
 * <p>
 * Created by JiaMing.Luo on 2017/3/22.
 */
public class AssemblyLine<T extends Mineral> implements Function<String[], Publisher<T>> {

    /**
     * 矿洞地址
     */
    private Tub<T> mTub;

    /**
     * 构建运输线
     *
     * @param tub         矿车
     */
    public AssemblyLine(Tub<T> tub) {
        this.mTub = tub;
    }

    @Override
    public Publisher<T> apply(String[] ipArray) throws Exception {
        List<Flowable<T>> flowableList = new ArrayList<>();
        for (String ip : ipArray) {
            flowableList.add(Flowable.just(ip)
                    .observeOn(Schedulers.io())
                    .map(s -> mTub.onDig(s))); // 矿洞挖矿
        }
        return Flowable.merge(flowableList);
    }
}
