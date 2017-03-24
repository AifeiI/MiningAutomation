package com.aifeii.scanLanIP.mining;

import com.aifeii.scanLanIP.model.Mineral;
import com.aifeii.scanLanIP.model.MiningFactory;
import com.aifeii.scanLanIP.model.Truck;
import com.aifeii.scanLanIP.model.Tub;
import com.aifeii.scanLanIP.util.HostNumUtil;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 矿厂标准
 *
 * Created by JiaMing.Luo on 2017/3/24.
 */
public abstract class BaseMiningFactory<T extends Mineral> implements MiningFactory {

    private static final int MAX_WORK_THREAD_POOL = 254;

    /**
     * 矿产仓库
     */
    private List<T> warehouse;

    /**
     * 矿车
     */
    private Tub<T> tub;

    /**
     * 矿场 与 仓库 间的运输车
     */
    private Truck<T> truck;

    /**
     * 矿车调度中心
     */
    private ExecutorService tubCenter;

    /**
     * 初始化矿业工厂
     *
     * @param tub 矿车
     */
    public BaseMiningFactory(Tub<T> tub) {
        // 建立仓库
        this.warehouse = new Vector<>();

        // 制造矿车
        this.tub = tub;

        // 建立矿车调度中心
        this.tubCenter = Executors.newWorkStealingPool(MAX_WORK_THREAD_POOL);

        // 选择运输车型号
        this.truck = host -> {
            // 验货
            if (host != null && warehouse != null) {
                // 矿产入仓
                warehouse.add(host);
            }
        };
    }

    @Override
    public void start(String target) throws Exception {

        int type = 0;

        long count = 0;
        int[] ipNum = null;

        if (target.indexOf('/') != -1) {
            String[] sp = target.split("/");
            if (sp.length == 2) {
                String ips = sp[0];
                int mark = Integer.parseInt(sp[1]);

                ipNum = HostNumUtil.getStartHostIP(ips, mark);
                count = HostNumUtil.countHost(ips, mark);
            } else {
                // 参数错误

                return;
            }
        } else {
            type = 1;
        }

        // 计时
        long startTime = System.currentTimeMillis();
        long diffTime;
        long lastSize = warehouse.size();

        /// ---------------- 开始调度 ---------------- ///
        // 生成调度清单
        if (type == 0) {
            String p = ipNum[0] + "." + ipNum[1] + "." + ipNum[2] + ".";

            for (int i = ipNum[3]; i < count + 1; i++) {
                // 提交调度任务
                tubCenter.submit(new AssemblyLine<>(p + i, tub, truck));
            }

            // 监听是否全部矿洞都已挖取
            while (warehouse.size() - lastSize < count) {
                // 数量不对，进行休眠
                Thread.sleep(600);
            }
        } else {
            // 提交调度任务
            tubCenter.submit(new AssemblyLine<>(target, tub, truck));

            // 监听是否全部矿洞都已挖取
            while (warehouse.size() < 1) {
                // 数量不对，进行休眠
                Thread.sleep(600);
            }
        }
        /// ---------------- 结束调度 ---------------- ///

        // 统计耗时
        long endTime = System.currentTimeMillis();
        diffTime = endTime - startTime;

        long nowSize = warehouse.size();
        long diffSize = nowSize - lastSize;

        System.out.println("探测矿场 [" + target +  "] 矿洞总数：" + diffSize + ", 耗时：" + diffTime + " ms");
    }

    @Override
    public List<T> viewWarehouse() {
        return warehouse;
    }

    @Override
    public void shutdown() {
        tubCenter.shutdownNow();
        try {
            tubCenter.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        warehouse.clear();
    }
}
