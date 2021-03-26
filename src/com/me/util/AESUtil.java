package com.me.util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AESUtil {

    /**
     * 加密算法AES
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * key的长度，Wrong key size: must be equal to 128, 192 or 256
     * 传入时需要16、24、36
     */
    private static final Integer KEY_LENGTH = 128;//16 * 8

    /**
     * 算法名称/加密模式/数据填充方式
     * 默认：AES/ECB/PKCS5Padding
     */
    private static final String ALGORITHMS = "AES/ECB/PKCS5Padding";

//    /**
//     * AES的key，由静态代码块赋值
//     */
//    public static String key;
//
//    static {
//        key = getKey();
//    }

    /**
     * 获取key
     */
    public static String getKey() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < KEY_LENGTH / 8; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type) {
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char) (rd.nextInt(25) + 65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char) (rd.nextInt(25) + 97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    /**
     * 加密
     *
     * @param content    加密的字符串
     * @param encryptKey key值
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        //设置Cipher对象
        Cipher cipher = Cipher.getInstance(ALGORITHMS,new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM));

        //调用doFinal
        byte[] b = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));

        // 转base64
        byte[] encode = Base64.getEncoder().encode(b);
        return new String(encode);
    }

    /**
     * 解密
     *
     * @param encryptStr 解密的字符串
     * @param decryptKey 解密的key值
     */
    public static String decrypt(String encryptStr, String decryptKey) throws Exception {
        //base64格式的key字符串转byte
        byte[] decodeBase64 = Base64.getDecoder().decode(encryptStr);

        //设置Cipher对象
        Cipher cipher = Cipher.getInstance(ALGORITHMS,new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), KEY_ALGORITHM));

        //调用doFinal解密
        byte[] decryptBytes = cipher.doFinal(decodeBase64);
        return new String(decryptBytes);
    }
    
    public static void main(String[] args) {
        //16位
        String key = "MIGfMA0GCSqGSIb3";

        //字符串
        String str = "huanzi.qch@qq.com:欢子";

//        String str = "424DDE";
        try {
            //加密
            String encrypt = AESUtil.encrypt(str, key);
            //解密
            String decrypt = AESUtil.decrypt(encrypt, key);

            System.out.println("加密前：" + str);
            System.out.println("加密后：" + encrypt);
            System.out.println("解密后：" + decrypt);
            CreateFileUtil.createFile("F:\\ChatRoom的图片文件夹\\111.bmp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
