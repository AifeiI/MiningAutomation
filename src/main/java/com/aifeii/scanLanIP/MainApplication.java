package com.aifeii.scanLanIP;

import com.aifeii.scanLanIP.mining.remote.RemoteHostInfo;
import com.aifeii.scanLanIP.mining.remote.RemoteMiningFactory;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 程序入口
 * <p>
 * Created by JiaMing.Luo on 2017/3/22.
 */
public class MainApplication {

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

        String[] ips = new String[0];
        String filterTarget = "";
        String outputPath = null;

        int size = args.length;
        if (size == 0) {
            // 打印帮助信息
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
                    ips = ipList.toArray(new String[ipList.size()]);
                    break;

                case "--ip":
                case "-i":
                case "--ips":
                case "-s":
                    ips = new String[]{args[i + 1]};
                    break;

                case "--filter":
                case "-t":
                    filterTarget = args[i + 1];
                    break;

                case "--out":
                case "-o":
                    outputPath = args[i + 1];
                    break;

                case "--help":
                case "-h":
                default:
                    // 打印帮助信息
                    return;
            }
            i++;
        }

        // 创建工厂
        RemoteMiningFactory factory = new RemoteMiningFactory(RemoteMiningFactory.TUB_TYPE_HTTPS);

        // 计时
        long time = System.currentTimeMillis();

        // 启动工厂
        for (String ip : ips) {
            factory.start(ip);
        }

        long diffTime = System.currentTimeMillis() - time;
        System.out.println("总耗时：" + diffTime + " ms");

        // 读取仓存
        List<RemoteHostInfo> list = factory.viewWarehouse();
        /// ---------------- 统计区域 ---------------- ///
        // 统计有效矿洞
        long surviveCount = list.parallelStream()
                .filter(RemoteHostInfo::isNotEmpty)
                .count();

        /// ---------------- 输出区域 ---------------- ///
        // 提取有效，并过滤的矿洞信息
        String finalFilterTarget = filterTarget;
        List<RemoteHostInfo> effectiveList = list.parallelStream()
                .filter(RemoteHostInfo::isNotEmpty)
                .filter(remoteHostInfo -> finalFilterTarget == null || finalFilterTarget.equals("") ||
                        remoteHostInfo.getName().equals(finalFilterTarget))
                .collect(Collectors.toList());
        // 打印清单
        if (outputPath != null) {
            File outputFile = new File(outputPath);
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
        } else {
            effectiveList.parallelStream()
                    .forEach(System.out::println);
        }

        // 打印任务统计
        System.out.println("矿洞总数：" + list.size() +
                "，有产出矿洞总数：" + surviveCount +
                "，过滤[" + filterTarget + "]后总数：" + effectiveList.size());

        // 关闭工厂
        factory.shutdown();
    }

}
