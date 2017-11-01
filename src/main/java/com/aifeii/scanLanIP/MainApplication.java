package com.aifeii.scanLanIP;

import com.aifeii.scanLanIP.mining.remote.HttpsTub;
import com.aifeii.scanLanIP.mining.remote.RemoteHostInfo;
import com.aifeii.scanLanIP.mining.remote.RemoteMiningFactory;
import io.reactivex.FlowableSubscriber;
import org.reactivestreams.Subscription;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 程序入口
 * <p>
 * Created by JiaMing.Luo on 2017/3/22.
 */
public class MainApplication {

    private static String[] mIPs;
    private static HttpsTub mHttpsTub;

    private static String mFilterTarget = "";
    private static String mOutputPath = null;

    private static CountDownLatch mCountDownLatch;
    private static List<RemoteHostInfo> mMineralList;
    private static Subscription mSubscription;

    /**
     * java -jar Mining.jar [COMMAND] value
     * <p>
     * [COMMAND]:
     * <ul>
     * <li>--file    / -f: 从文件中读取矿场地址，格式 {@code ./file} 或者 {@code /path/file}</li>
     * <li>--ip      / -i: 直接输入矿场地址，格式 {@code 0.0.0.0}</li>
     * <li>--ips     / -s: 批量直接输入矿场地址，格式 {@code 0.0.0.0/32}</li>
     * <li>--filter  / -t: 过滤输出，格式 {@code www.google.com}</li>
     * <li>--out     / -o: 输出结果到文件，格式 {@code ./file} 或者 {@code /path/file}</li>
     * <li>--help    / -h: 帮助</li>
     * </ul>
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        int size = args.length;
        if (size == 0) {
            // 打印帮助信息
            System.out.println(printHelp());
            return;
        }
        // 提取参数
        for (int i = 0; i < size; i++) {
            switch (args[i]) {
                case "--file":
                case "-f":
                    File file = new File(args[i + 1]);
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    List<String> ipList = bufferedReader.lines().collect(Collectors.toList());
                    mIPs = ipList.toArray(new String[ipList.size()]);
                    break;

                case "--ip":
                case "-i":
                case "--ips":
                case "-s":
                    mIPs = new String[]{args[i + 1]};
                    break;

                case "--filter":
                case "-t":
                    mFilterTarget = args[i + 1];
                    break;

                case "--out":
                case "-o":
                    mOutputPath = args[i + 1];
                    break;

                case "--help":
                case "-h":
                default:
                    // 打印帮助信息
                    System.out.println(printHelp());
                    return;
            }
            i++;
        }

        mMineralList = new ArrayList<>();
        mHttpsTub = new HttpsTub();

        mCountDownLatch = new CountDownLatch(1);
        // 计时
        long time = System.currentTimeMillis();

        new RemoteMiningFactory(RemoteMiningFactory.TUB_TYPE_HTTPS)
                .start(mIPs)
                .subscribe(new FlowableSubscriber<RemoteHostInfo>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(RemoteHostInfo remoteHostInfo) {
                        System.out.println(remoteHostInfo);
                        mMineralList.add(remoteHostInfo);
                        mSubscription.request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        /// ---------------- 统计区域 ---------------- ///
                        // 统计有效矿洞
                        long surviveCount = mMineralList.parallelStream()
                                .filter(RemoteHostInfo::isNotEmpty)
                                .count();

                        /// ---------------- 输出区域 ---------------- ///
                        // 提取有效，并过滤的矿洞信息
                        List<RemoteHostInfo> effectiveList = mMineralList.parallelStream()
                                .filter(RemoteHostInfo::isNotEmpty)
                                .filter(remoteHostInfo ->
                                        mFilterTarget == null ||
                                                mFilterTarget.isEmpty() ||
                                                remoteHostInfo.getName().equals(mFilterTarget))
                                .collect(Collectors.toList());
                        // 打印清单
                        if (mOutputPath != null && !mOutputPath.isEmpty()) {
                            File outputFile = new File(mOutputPath);
                            try {
                                FileWriter fileWriter = new FileWriter(outputFile);
                                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                                effectiveList.forEach(remoteHostInfo -> {
                                    try {
                                        bufferedWriter.write(remoteHostInfo.toString());
                                        bufferedWriter.newLine();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                                bufferedWriter.flush();
                                bufferedWriter.close();
                                fileWriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (outputFile.exists()) {
                                System.out.println("清单已导出：" + outputFile.getPath());
                            }
                        } else {
                            effectiveList.parallelStream()
                                    .forEach(System.out::println);
                        }

                        // 打印任务统计
                        System.out.println(
                                "矿洞总数：" + mMineralList.size() +
                                        "，有产出矿洞总数：" + surviveCount +
                                        "，过滤 [" + mFilterTarget + "] 后总数：" + effectiveList.size());

                        mCountDownLatch.countDown();
                    }
                }); // 仓库

        mCountDownLatch.await();

        long diffTime = System.currentTimeMillis() - time;
        System.out.println("总耗时：" + diffTime + " ms");
    }

    private static String printHelp() {
        return "HELP: \n" +
                "--file    / -f: 从文件中读取矿场地址，格式 ./file 或者 /path/file\n" +
                "--ip      / -i: 直接输入矿场地址，格式 0.0.0.0\n" +
                "--ips     / -s: 批量直接输入矿场地址，格式 0.0.0.0/32\n" +
                "--filter  / -t: 过滤输出，格式 www.google.com\n" +
                "--out     / -o: 输出结果到文件，格式 ./file 或者 /path/file\n" +
                "--help    / -h: 帮助";
    }
}
