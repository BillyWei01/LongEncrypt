import java.util.Random;

public class Main {
    private static void testEncodeLong64() {
        Random r = new Random();
        boolean success = true;
        for (int i = 0; i < 100; i++) {
            long x = r.nextLong();
            long y = LongEncoder.encode64(x);
            long z = LongEncoder.decode64(y);
            if (x != z) {
                success = false;
                break;
            }
        }
        if (success) {
            System.out.println("testEncodeLong64 success");
        } else {
            System.out.println("testEncodeLong64 failed");
        }
    }

    private static void testEncodeLong48() {
        Random r = new Random();
        boolean success = true;
        for (int i = 0; i < 100; i++) {
            long x = r.nextLong() >>> 16;
            long y = LongEncoder.encode48(x);
            long z = LongEncoder.decode48(y);
            if (x != z) {
                success = false;
                break;
            }
        }
        if (success) {
            System.out.println("testEncodeLong48 success");
        } else {
            System.out.println("testEncodeLong48 failed");
        }
    }

    private static void testBase62() {
        Random r = new Random();
        boolean success = true;
        for (int i = 0; i < 100; i++) {
            long x = r.nextLong();
            String y = Base62.encode(LongEncoder.encode64(x));
            long z = LongEncoder.decode64(Base62.decode(y));
            if (x != z) {
                success = false;
                break;
            }
        }
        if (success) {
            System.out.println("testBase62 success");
        } else {
            System.out.println("testBase62 failed");
        }
    }

    private static void testBase62ForShortLink(){
        Random r = new Random();
        boolean success = true;

        for (int i = 0; i < 100; i++) {
            long x = r.nextLong() >>> 16;
            // 48bit的x, y的长度在1~9之间，长度为8居多
            // 如果需要恒定8字节，则可以用Base64, Base64时6bit一个字节，正好8字节
            // 如果又不想引入特殊字符('-','_'), 则x需要需要小于等于47bit，构造一个47bit的x(比较复杂)
            String y = Base62.encodePositive(LongEncoder.encode48(x));
            long z = LongEncoder.decode48(Base62.decodePositive(y));
            if (x != z) {
                success = false;
                break;
            }
        }
        if (success) {
            System.out.println("testBase62ForShortLink success");
        } else {
            System.out.println("testBase62ForShortLink failed");
        }
    }

    private static void testLong48ToHex() {
        Random r = new Random();
        long x = r.nextLong() >>> 16;
        String y = HexUtil.long48ToHex(LongEncoder.encode48(x));
        long z = LongEncoder.decode48(HexUtil.hexToLong48(y));
        if (x == z) {
            System.out.println("testLong48ToHex success");
        } else {
            System.out.println("testLong48ToHex failed");
        }
        System.out.println("y = " + y);
    }

    private static void testNumberCipher() {
        byte[] key = new byte[24];
        Random r = new Random();
        r.nextBytes(key);
        NumberCipher cipher = new NumberCipher(key);

        boolean success = true;
        for (int i = 0; i < 100; i++) {
            long x = r.nextLong();
            long y = cipher.encryptLong(x);
            long z = cipher.decryptLong(y);
            if (x != z) {
                success = false;
                break;
            }
        }
        if (success) {
            System.out.println("Test encode long success");
        } else {
            System.out.println("Test encode long failed");
        }

        if(!success){
            return;
        }
        for (int i = 0; i < 100; i++) {
            int x = r.nextInt();
            int y = cipher.encryptInt(x);
            int z = cipher.decryptInt(y);
            if (x != z) {
                success = false;
                break;
            }
        }
        if (success) {
            System.out.println("Test encode int success");
        } else {
            System.out.println("Test encode int failed");
        }
    }

    public static void main(String[] args) {
        testEncodeLong64();
        testEncodeLong48();
        testNumberCipher();

//        testBase62();
//        testBase62ForShortLink();
//        testLong48ToHex();
    }
}
