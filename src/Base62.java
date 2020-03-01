
public class Base62 {
    private static final char[] ENCODE_TABLE = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };


    private static final byte[] DECODE_TABLE = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
            -1, -1, -1, -1, -1, -1, -1,
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
            23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
            -1, -1, -1, -1, -1, -1,
            36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48,
            49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61,
    };

    /**
     * 整数转62进制字符串
     *
     * 参数n的最高位可能是0或者1，
     * 在C语言中，我们可以统一用 unsigned long来计算，
     * 但是Java没有unsigned long类型， 为了避免在n < 0时求余(%)出错，
     * 我们通过位移将最高位的5bit映射到一个字母中，剩下的59bit通过求余和除法做进制转换
     *
     * @param n 可以是正数或者负数
     * @return 11字节的字符串，有可能用于对齐(padding)的0
     */
    public static String encode(long n) {
        char[] buf = new char[11];
        long r = (n << 5) >>> 5;
        for (int i = 10; i >= 1; i--) {
            buf[i] = ENCODE_TABLE[(int) (r % 62)];
            r /= 62;
        }
        buf[0] = ENCODE_TABLE[(int) (n >>> 59)];
        return new String(buf);
    }

    /**
     * 解码62进制字符串
     *
     * @param s 62进制字符串
     * @return long类型数值
     */
    public static long decode(String s) {
        if(s == null || s.length() == 0){
            return 0L;
        }
        if (s.length() != 11) {
            throw new IllegalArgumentException("string's length must be 11, s:" + s);
        }
        long n = 0L;
        long m = 1L;
        for (int i = 10; i >= 1; i--) {
            // 为了效率期间, 此处没有对字符的做边界检查
            n += DECODE_TABLE[s.charAt(i)] * m;
            m *= 62;
        }
        n |= (long) DECODE_TABLE[s.charAt(0)] << 59;
        return n;
    }

    /**
     * 正整数转62进制字符串
     *
     * @param n 正整数，若n小于0会抛出 IllegalArgumentException
     * @return 62进制字符串
     */
    public static String encodePositive(long n) {
        if (n < 0) {
            throw new IllegalArgumentException(n + " is negative");
        }
        int max = 11;
        char[] buf = new char[max];
        int i = max;
        do {
            buf[--i] = ENCODE_TABLE[(int) (n % 62)];
            n /= 62;
        } while (n != 0);
        return new String(buf, i, max - i);
    }

    /**
     * 解码62进制字符串
     *
     * @param s 62进制字符串
     * @return long类型数值
     */
    public static long decodePositive(String s){
        if(s == null || s.length() == 0){
            return 0L;
        }
        int len = s.length();
        if (len > 11) {
            throw new IllegalArgumentException("string's length must less than 11, s:" + s);
        }
        long n = 0L;
        long m = 1L;
        for (int i = len-1; i >= 0; i--) {
            n += DECODE_TABLE[s.charAt(i)] * m;
            m *= 62;
        }
        return n;
    }
}