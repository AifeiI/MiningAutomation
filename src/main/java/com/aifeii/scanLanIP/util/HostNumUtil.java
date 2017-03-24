package com.aifeii.scanLanIP.util;

import com.sun.istack.internal.NotNull;

/**
 * Created by JiaMing.Luo on 2017/3/24.
 */
public class HostNumUtil {

    public static int[] getStartHostIP(@NotNull String networkSegment, int subnetMask) {
        // 转换格式
        int[] num = formatNetworkSegment(networkSegment);

        if (!checkMark(subnetMask)) {
            throw new IllegalArgumentException("掩码格式不正确：" + subnetMask);
        }

        if (num[3] == 0) {
            num[3] = 1;
        }
        return num;
    }

    public static int[] formatNetworkSegment(@NotNull String networkSegment) {
        // 参数校验
        if (networkSegment.equals("")) {
            throw new IllegalArgumentException("网段格式不正确：" + networkSegment);
        }
        String[] nsSplits = networkSegment.split("\\.");
        if (nsSplits.length < 4 || nsSplits.length > 4) {
            throw new IllegalArgumentException("网段格式不正确：" + networkSegment);
        }
        int[] result = new int[4];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(nsSplits[i]);
            if (result[i] < 0 || result[i] > 255) {
                throw new IllegalArgumentException("网段格式不正确：" + networkSegment);
            }
        }
        return result;
    }

    public static long countHost(@NotNull String networkSegment, int subnetMask) {
        // 转换格式
        int[] num = formatNetworkSegment(networkSegment);

        if (!checkMark(subnetMask)) {
            throw new IllegalArgumentException("掩码格式不正确：" + subnetMask);
        }

        // 计算可分配主机数

        StringBuilder biys = new StringBuilder();
        for (int i : num) {
            String iString = convertBinary(i);
            if (iString.length() < 8) {
                int diff = 8 - iString.length();
                for (int j = 0; j < diff; j++) {
                    biys.append('0');
                }
            }
            biys.append(iString);
        }
        String biyString = biys.toString();
        String min = biyString.substring(subnetMask, biys.length());

        StringBuilder maxSB = new StringBuilder();
        for (int i = 0; i < min.length(); i++) {
            maxSB.append('1');
        }
        String max = maxSB.toString();

        long count = convertAlgorithm(max) - convertAlgorithm(min) - 1;

        return count;
    }

    public static boolean checkMark(int subnetMask) {
        return !(subnetMask < 1 || subnetMask > 32);
    }

    /**
     * 十进制转换二进制
     *
     * @param sum
     * @return
     */
    public static String convertBinary(int sum) {
        return Integer.toBinaryString(sum);
    }

    /**
     * 二进制转十进制
     *
     * @param input
     * @return
     */
    public static long convertAlgorithm(String input) {
        return Long.parseLong(input, 2);
    }
}
