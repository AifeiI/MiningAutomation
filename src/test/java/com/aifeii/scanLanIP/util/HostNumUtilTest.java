package com.aifeii.scanLanIP.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by JiaMing.Luo on 2017/3/24.
 */
public class HostNumUtilTest {

    @Test
    public void getStartHostIP() throws Exception {
        int[] ipNums = HostNumUtil.getStartHostIP("192.168.1.0", 24);
        assertNotNull(ipNums);
    }

    @Test
    public void formatNetworkSegment() throws Exception {
        int[] s1 = new int[]{192, 168, 1, 1};

        int[] ipNums1 = HostNumUtil.formatNetworkSegmentByIPv4("192.168.1.1");
        assertNotNull(ipNums1);
        assertArrayEquals(s1, ipNums1);

        int[] s2 = new int[]{192, 168, 0, 0};

        int[] ipNums2 = HostNumUtil.formatNetworkSegmentByIPv4("192.168.0.0");
        assertNotNull(ipNums2);
        assertArrayEquals(s2, ipNums2);
    }

    @Test
    public void convertBinary() throws Exception {
        String result = HostNumUtil.convertBinary(192);
        assertNotNull(result);
        assertEquals("11000000", result);
    }

    @Test
    public void convertAlgorithm() throws Exception {
        String input = "11111111";
        long result = HostNumUtil.convertAlgorithm(input);
        assertEquals(255, result);
    }

    @Test
    public void countHost() throws Exception {

        String w = "192.168.1.0";
        int m = 24;

        long result = HostNumUtil.countHost(w, m);

        assertEquals(254, result);

        String w2 = "192.168.1.56";
        int m2 = 24;

        long result2 = HostNumUtil.countHost(w2, m2);

        assertEquals(198, result2);

        String w3 = "192.168.0.0";
        int m3 = 16;

        long result3 = HostNumUtil.countHost(w3, m3);

        assertEquals(65534, result3);
    }


}