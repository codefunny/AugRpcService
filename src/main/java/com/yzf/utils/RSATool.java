package com.yzf.utils;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSATool {
    /**
     * 指定key的大小
     */
    private static      int    KEYSIZE       = 1024;
    public static final String RSA_ALGORITHM = "RSA";

    /**
     * 生成密钥对
     */
    public static void generateKeyPair(String pubkeyfile, String privatekeyfile) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom sr = new SecureRandom();
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        // 初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(KEYSIZE, sr);
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 生成私钥
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(new FileOutputStream(privatekeyfile));
        oos.writeObject(privateKey);
        oos.flush();
        oos.close();

        oos = new ObjectOutputStream(new FileOutputStream(pubkeyfile));
        oos.writeObject(publicKey);
        oos.flush();
        oos.close();
    }

    /**
     * 加密
     *
     * @param source
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] source, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] resultBytes = cipher.doFinal(source);
        return resultBytes;
    }

    /**
     * 解密
     *
     * @param source
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] source, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] resultBytes = cipher.doFinal(source);
        return resultBytes;
    }

    public static void main(String[] args) throws Exception {
        String pem = "D://dumps//1020160419686875-rsa-private.pem";
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pem));
        RSAPrivateKey prikey = (RSAPrivateKey) ois.readObject();
        ois.close();
        String key = "!1231231sdaf3ra中文gt23*(&(*^";
        byte[] aes = RSATool.encrypt(key.getBytes(), prikey);
    }
}
