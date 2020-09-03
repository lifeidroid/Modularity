package cn.carl.communicationLib.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/05
 * desc   :
 * version: 1.0
 * ==============================================
 */

public class DataUtils {
    private static final byte[] chartobyte = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, 0, 10, 11, 12, 13, 14, 15};
    private static final char[] bytetochar = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public DataUtils() {
    }

    public static boolean bytesEquals(byte[] d1, byte[] d2) {
        if (d1 == null && d2 == null) {
            return true;
        } else if (d1 != null && d2 != null) {
            if (d1.length != d2.length) {
                return false;
            } else {
                for(int i = 0; i < d1.length; ++i) {
                    if (d1[i] != d2[i]) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean bytesEquals(byte[] d1, int offset1, byte[] d2, int offset2, int length) {
        if (d1 != null && d2 != null) {
            if (offset1 + length <= d1.length && offset2 + length <= d2.length) {
                for(int i = 0; i < length; ++i) {
                    if (d1[i + offset1] != d2[i + offset2]) {
                        return false;
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static char[] bytestochars(byte[] data) {
        char[] cdata = new char[data.length];

        for(int i = 0; i < cdata.length; ++i) {
            cdata[i] = (char)(data[i] & 255);
        }

        return cdata;
    }

    public static byte[] getRandomByteArray(int nlength) {
        byte[] data = new byte[nlength];
        Random rmByte = new Random(System.currentTimeMillis());

        for(int i = 0; i < nlength; ++i) {
            data[i] = (byte)rmByte.nextInt(256);
        }

        return data;
    }

    public static void blackWhiteReverse(byte[] data) {
        for(int i = 0; i < data.length; ++i) {
            data[i] = (byte)(~(data[i] & 255));
        }

    }

    public static byte[] getSubBytes(byte[] org, int start, int length) {
        byte[] ret = new byte[length];

        for(int i = 0; i < length; ++i) {
            ret[i] = org[i + start];
        }

        return ret;
    }

    public static String bytesToStr(byte[] rcs) {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < rcs.length; ++i) {
            String tmp = Integer.toHexString(rcs[i] & 255);
            tmp = tmp.toUpperCase(Locale.getDefault());
            if (tmp.length() == 1) {
                stringBuilder.append("0x0" + tmp);
            } else {
                stringBuilder.append("0x" + tmp);
            }

            if (i % 16 != 15) {
                stringBuilder.append(" ");
            } else {
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    public static byte[] cloneBytes(byte[] data) {
        byte[] ret = new byte[data.length];

        for(int i = 0; i < data.length; ++i) {
            ret[i] = data[i];
        }

        return ret;
    }

    public static byte bytesToXor(byte[] data, int start, int length) {
        if (length == 0) {
            return 0;
        } else if (length == 1) {
            return data[start];
        } else {
            int result = data[start] ^ data[start + 1];

            for(int i = start + 2; i < start + length; ++i) {
                result ^= data[i];
            }

            return (byte)result;
        }
    }

    public static byte[] byteArraysToBytes(byte[][] data) {
        int length = 0;

        for(int i = 0; i < data.length; ++i) {
            length += data[i].length;
        }

        byte[] send = new byte[length];
        int k = 0;

        for(int i = 0; i < data.length; ++i) {
            for(int j = 0; j < data[i].length; ++j) {
                send[k++] = data[i][j];
            }
        }

        return send;
    }

    public static void copyBytes(byte[] orgdata, int orgstart, byte[] desdata, int desstart, int copylen) {
        for(int i = 0; i < copylen; ++i) {
            desdata[desstart + i] = orgdata[orgstart + i];
        }

    }

    public static String bytesToStr(byte[] rcs, int offset, int count) {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < count; ++i) {
            String tmp = Integer.toHexString(rcs[i + offset] & 255);
            tmp = tmp.toUpperCase(Locale.getDefault());
            if (tmp.length() == 1) {
                stringBuilder.append("0x0" + tmp);
            } else {
                stringBuilder.append("0x" + tmp);
            }

            if (i % 16 != 15) {
                stringBuilder.append(" ");
            } else {
                stringBuilder.append("\r\n");
            }
        }

        return stringBuilder.toString();
    }

    public static byte HexCharsToByte(char ch, char cl) {
        byte b = (byte)(chartobyte[ch - 48] << 4 & 240 | chartobyte[cl - 48] & 15);
        return b;
    }

    public static char[] ByteToHexChars(byte b) {
        char[] chs = new char[]{'0', '0'};
        chs[0] = bytetochar[b >> 4 & 15];
        chs[1] = bytetochar[b & 15];
        return chs;
    }

    public static boolean IsHexChar(char c) {
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] str2Bytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    public static byte getcheckSum(byte[] data,int lenght){
        byte byCheckSum = 0;
        for (int i = 0;i < lenght;i++){
            byCheckSum += data[i];
        }
        byCheckSum = (byte) ((~byCheckSum) + 1);
        return byCheckSum;
    }

    public static byte[] HexStringToBytes(String str) {
        str = str.replace(" ", "");
        int count = str.length();
        byte[] data = (byte[])null;
        if (count % 2 == 0) {
            data = new byte[count / 2];

            for(int i = 0; i < count; i += 2) {
                char ch = str.charAt(i);
                char cl = str.charAt(i + 1);
                if (!IsHexChar(ch) || !IsHexChar(cl)) {
                    data = (byte[])null;
                    break;
                }

                if (ch >= 'a') {
                    ch = (char)(ch - 32);
                }

                if (cl >= 'a') {
                    cl = (char)(cl - 32);
                }

                data[i / 2] = HexCharsToByte(ch, cl);
            }
        }

        return data;
    }

    public static StringBuilder BytesToHexStr(byte[] data, int offset, int count) {
        StringBuilder str = new StringBuilder();
        for(int i = offset; i < offset + count; ++i) {
            str.append(ByteToHexChars(data[i]));
//            str.append(String.format("%02X ", buffer[i]));
        }
        return str;
    }

//    public static byte[] getBytes(byte[] resource,int start,int lenght){
//        System
//    }

    public static StringBuilder BytesToHexStr(byte[] data) {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < data.length; ++i) {
            str.append(ByteToHexChars(data[i]));
        }
        return str;
    }

    public static StringBuilder RemoveChar(String str, char c) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();

        for(int i = 0; i < length; ++i) {
            char tmp = str.charAt(i);
            if (tmp != c) {
                sb.append(tmp);
            }
        }

        return sb;
    }

    public static String byteToStr(byte rc) {
        String tmp = Integer.toHexString(rc & 255);
        tmp = tmp.toUpperCase(Locale.getDefault());
        String rec;
        if (tmp.length() == 1) {
            rec = "0x0" + tmp;
        } else {
            rec = "0x" + tmp;
        }

        return rec;
    }

    public static final int getBit(byte ch, int idx) {
        return 1 & ch >> idx;
    }

    public static void BytesArrayFill(byte[] data, int offset, int count, byte value) {
        for(int i = offset; i < offset + count; ++i) {
            data[i] = value;
        }

    }

    public static List<Integer> bytesToShi(byte[] src, int len) {
        List<Integer> to = new ArrayList();
        if (src != null && src.length > 0) {
            for(int i = 0; i < len; ++i) {
                to.add(i, src[i] & 255);
            }

            return to;
        } else {
            return null;
        }
    }

    public static List<Integer> bytesToShi(String src, int len) {
        List<Integer> to = new ArrayList();
        if (src != null && src.length() > 0) {
            for(int i = 0; i < len; ++i) {
                to.add(i, Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16) & 255);
            }

            return to;
        } else {
            return null;
        }
    }

    public static List<Integer> strToShi(String src) {
        List<Integer> to = new ArrayList();
        if (src != null && src.length() > 0) {
            int len = src.length() / 2;

            for(int i = 0; i < len; ++i) {
                to.add(i, Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16) & 255);
            }

            return to;
        } else {
            return null;
        }
    }
}
