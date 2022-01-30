package cn.qssq666;

//cn.qssq666.insertqqmodule.qssqproguard.keepnotpro.AllEncrypt
public class AllEncrypt {


    public static String decodeSimple(byte[] bytes, String key) {
        int len = bytes.length;
        int keyLen = key.length();
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (bytes[i] ^ key.charAt(i % keyLen));
        }

        return new String(bytes);
    }
}
