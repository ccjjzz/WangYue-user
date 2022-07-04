package com.jiuyue.user.utils.glide;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class TestAesUtils {
    public static final String AES = "AES";
    public static final String charset = "UTF-8"; // 编码格式；默认null为GBK
    public static final int keySizeAES = 128;
    public static final String AES_SECRET_KEY = "2016@wsy7173.com";
    public static final String OFFSET = "0123456789abcdef";
    public static final String SCHEMA = "AES/CBC/PKCS5Padding";

    private static TestAesUtils instance;

    private TestAesUtils() {
    }

    // 单例
    public static TestAesUtils getInstance() {
        if (instance == null) {
            synchronized (TestAesUtils.class) {
                if (instance == null) {
                    instance = new TestAesUtils();
                }
            }
        }
        return instance;
    }


    /**
     * 使用 AES 进行加密
     *
     * @param content 待加密的字符串
     * @param key     密钥
     * @param offset  偏移量
     * @return
     */
    public String encode2HexStr(String content, String key, String offset, String schema) {

        byte[] res = null;
        try {
            res = charset == null ? content.getBytes() : content.getBytes(charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res != null && res.length > 0) {
            return parseByte2HexStr(keyGeneratorES(res, AES, schema, key, offset, keySizeAES, true, true));
        }
        return null;
    }

    /**
     * 使用 AES 进行解密
     *
     * @param content 密文
     * @param key     密钥
     * @param offset  偏移量
     * @return
     */
    public String HexStrDecode(String content, String key, String offset, String schema) {
        byte[] res = parseHexStr2Byte(content);
        if (res != null && res.length > 0) {
            return new String(keyGeneratorES(res, AES, schema, key, offset, keySizeAES, true, false));
        }
        return null;
    }

    /**
     * 使用 AES 进行加密
     *
     * @param content 待加密的字符串
     * @return
     */
    public String encode2HexStr(String content) {
        byte[] res = null;
        try {
            res = charset == null ? content.getBytes() : content.getBytes(charset);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res != null && res.length > 0) {
            return parseByte2HexStr(keyGeneratorES(res, AES, SCHEMA, AES_SECRET_KEY, OFFSET, keySizeAES, true, true));
        }
        return null;
    }

    /**
     * 使用 AES 进行解密
     *
     * @param content 密文
     * @return
     */
    public String HexStrDecode(String content) {
        byte[] res = parseHexStr2Byte(content);
        if (res != null && res.length > 0) {
            return new String(keyGeneratorES(res, AES, SCHEMA, AES_SECRET_KEY, OFFSET, keySizeAES, true, false));
        }
        return null;
    }

    /**
     * base 64 加密
     *
     * @param content
     * @return
     */
    public String encryptWithBase64(String content) {
        byte[] res = null;
        try {
            res = charset == null ? content.getBytes() : content.getBytes(charset);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res != null && res.length > 0) {
            return base64Encode(keyGeneratorES(res, AES, SCHEMA, AES_SECRET_KEY, OFFSET, keySizeAES, false, true));
        }
        return null;


    }

    /**
     * base 64 解密
     *
     * @param content
     * @return
     */
    public String decryptWithBase64(String content) {
        byte[] res = null;
        try {
            res = base64Decode(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res != null && res.length > 0) {
            return new String(keyGeneratorES(res, AES, SCHEMA, AES_SECRET_KEY, OFFSET, keySizeAES, false, false));
        }
        return null;

    }


    /**
     * 使用KeyGenerator双向加密，DES/AES，注意这里转化为字符串的时候是将2进制转为16进制格式的字符串，不是直接转，因为会出错
     *
     * @param res
     * @param algorithm
     * @param schema
     * @param key
     * @param offset
     * @param keysize
     * @param isKeyEncode
     * @param isEncode
     * @return
     */
    private byte[] keyGeneratorES(byte[] res, String algorithm, String schema, String key, String offset, int keysize, boolean isKeyEncode, boolean isEncode) {
        try {
            SecretKey sk = getKey(key, algorithm);
            if (isKeyEncode) {
                sk = getKeyByEncode(key, keysize, algorithm);
            }
            SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            IvParameterSpec iv = null;
            if (schema != null && !"".equals(schema) && schema.split("/").length > 1 && offset != null && !"".equals(offset)) {
                cipher = Cipher.getInstance(schema);
                iv = new IvParameterSpec(offset.getBytes());
            }
            if (isEncode) {
                if (iv != null) {
                    cipher.init(Cipher.ENCRYPT_MODE, sks, iv);
                } else {
                    cipher.init(Cipher.ENCRYPT_MODE, sks);
                }

            } else {
                if (iv != null) {
                    cipher.init(Cipher.DECRYPT_MODE, sks, iv);
                } else {
                    cipher.init(Cipher.DECRYPT_MODE, sks);
                }
            }
            return cipher.doFinal(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * base 64 encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public String base64Encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public byte[] base64Decode(String base64Code) throws Exception {
        return base64Code.isEmpty() ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }


    private SecretKeySpec getKey(String strKey, String algorithm) throws Exception {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        SecretKeySpec skeySpec = new SecretKeySpec(arrB, algorithm);
        return skeySpec;
    }

    private SecretKeySpec getKeyByEncode(String key, int keysize, String algorithm) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        if (keysize == 0) {
            byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
            kg.init(new SecureRandom(keyBytes));
        } else if (key == null) {
            kg.init(keysize);
        } else {
            byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
            kg.init(keysize, new SecureRandom(keyBytes));
        }
        SecretKey sk = kg.generateKey();
        return new SecretKeySpec(sk.getEncoded(), algorithm);
    }

    /**
     * @param content 解密内容
     * @return
     * @throws Exception
     */
    public static byte[] desEncrypt(byte[] content) throws Exception {
        try {
            byte[] encrypted1 = content;
            Cipher cipher = Cipher.getInstance(SCHEMA);
            SecretKeySpec keyspec = new SecretKeySpec(AES_SECRET_KEY.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(OFFSET.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            return original;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
