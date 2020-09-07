package com.carl.carlLib.utils;

import java.math.BigDecimal;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2020/05/18
 * desc   : Double的加减法
 * version: 1.0
 * ==============================================
 */
public class DoubleUtils {
    /**
     * 两数相加
     *
     * @param v1 double
     * @param v2 double
     * @return double
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 两数相减，如果得数为负数，返回0
     *
     * @param v1 double
     * @param v2 double
     * @return double
     */
    public static double subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = b1.subtract(b2);
        if (b3.signum() == -1) {
            return 0;
        } else {
            return b3.doubleValue();
        }
    }
}
