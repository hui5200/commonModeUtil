package com.ailin.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * File: SM3Util.java
 * Description: 描述信息
 * Company: 南威软件股份有限公司
 * CreateTime: 2020/3/26
 *
 * @author wgaohua
 */
public class SM3Util {

    private final static Logger logger = LoggerFactory.getLogger(SM3Util.class);

    private static byte[] hash(byte[] srcData) {
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    private static boolean verify(byte[] srcData, byte[] sm3Hash) {
        byte[] newHash = hash(srcData);
        return Arrays.equals(newHash, sm3Hash);
    }

    private static byte[] hmac(byte[] key, byte[] srcData) {
        KeyParameter keyParameter = new KeyParameter(key);
        SM3Digest digest = new SM3Digest();
        HMac mac = new HMac(digest);
        mac.init(keyParameter);
        mac.update(srcData, 0, srcData.length);
        byte[] result = new byte[mac.getMacSize()];
        mac.doFinal(result, 0);
        return result;
    }

    private static byte[] encrypt(String paramStr) {
        byte[] srcData = paramStr.getBytes(StandardCharsets.UTF_8);
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    public static String encode(String paramStr) {
        return ByteUtils.toHexString(encrypt(paramStr));
    }

    public static String encode(String pwd, String salt) {
        return ByteUtils.toHexString(encrypt(pwd + salt));
    }

    public static String createSalt(){
        byte[] salt = new byte[4];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }

    public static void main(String[] args) {
        String salt = createSalt();
        String password = SM3Util.encode("Zw@123456");
        System.out.println(password);
        password = SM3Util.encode(password,salt);
        System.out.println(password);
    }


}
