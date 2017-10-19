package com.compass.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @ClassName: DesEncrypt
 * @Description: 使用DES加密与解密,可对byte[],String类型进行加密与解密
 * @author Lance.Wu
 * 
 *         使用DES加密与解密,可对byte[],String类型进行加密与解密 密文可使用String,byte[]存储.
 * 
 *         方法: void getKey(String strKey)从strKey的字条生成一个Key
 * 
 *         String getEncString(String strMing)对strMing进行加密,返回String密文 String
 *         getDesString(String strMi)对strMin进行解密,返回String明文
 * 
 *         byte[] getEncCode(byte[] byteS)byte[]型的加密 byte[] getDesCode(byte[]
 *         byteD)byte[]型的解密
 *
 *         Copyright: Copyright (c) 2014 Company:www.imobpay.com
 */
public class DesEncrypt {

    /** 密钥 */
    private SecretKey securekey;
    private static final Logger LogPay = LoggerFactory.getLogger(DesEncrypt.class);
    /**
     * setKey(设置byte[] KEY值) (算法中需要通过key来得到加密用的key)
     *
     * @Title: setKey
     * @Description: 设置byte[] KEY值,算法中需要通过key来得到加密用的key
     * @Date May 3, 2014 210:22:54 AM
     * @modifyDate May 3, 2014 210:22:54 AM
     * @param key
     *            KEY值 byte[]
     */
    public void setKey(byte[] key) {
        try {
            DESedeKeySpec dks = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            securekey = keyFactory.generateSecret(dks);
        } catch (Exception e) {
            LogPay.error(e.getMessage(), e);
        }
    }

    /**
     * get3DesEncCode(3DesECB加密byte[]明文)
     *
     * @Title: get3DesEncCode
     * @Description: 3DesECB加密byte[]明文
     * @Date May 3, 2014 210:30:37 AM
     * @modifyDate May 3, 2014 210:30:37 AM
     * @param byteS
     *            需要加密的明文 byte[]
     * @return byte[] 加密后的密文 byte[]
     */
    public byte[] get3DesEncCode(byte[] byteS) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            // IvParameterSpec iv = new IvParameterSpec(biv);
            cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            LogPay.error(e.getMessage(), e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * get3DesDesCode(3DesECB解密byte[]密文)
     * 
     * @Title: get3DesDesCode
     * @Description: 3DesECB解密byte[]密文
     * @Date May 3, 2014 210:17:18 AM
     * @modifyDate May 3, 2014 210:17:18 AM
     * @param byteD
     *            需要解密的密文内容 byte[]
     * @return byte[] 解密后的明文 byte[]
     */
    public byte[] get3DesDesCode(byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            // IvParameterSpec iv = new IvParameterSpec(biv);
            cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, securekey);
            byteFina = cipher.doFinal(byteD);

        } catch (Exception e) {
            LogPay.error(e.getMessage(), e);
        } finally {
            cipher = null;
        }
        return byteFina;

    }

    /**
     * get3DesCBCEncCode(3DesCBC加密byte[]明文)
     *
     * @Title: get3DesCBCEncCode
     * @Description: 3DesCBC加密byte[]明文
     * @Date May 3, 2014 210:35:11 AM
     * @modifyDate May 3, 2014 210:35:11 AM
     * @param byteS
     *            需要加密的明文 byte[]
     * @param iv
     *            加密参数中的IV值 byte[]
     * @return byte[] 加密后的密文 byte[]
     */
    public byte[] get3DesCBCEncCode(byte[] byteS, byte[] iv) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            IvParameterSpec ivp = new IvParameterSpec(iv);
            cipher = Cipher.getInstance("DESede/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, ivp);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            LogPay.error(e.getMessage(), e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * get3DesCBCDesCode(3DesCBC解密byte[]密文)
     * 
     * @Title: get3DesCBCDesCode
     * @Description: 3DesCBC解密byte[]密文
     * @Date May 3, 2014 210:37:24 AM
     * @modifyDate May 3, 2014 210:37:24 AM
     * @param byteD
     *            需要解密的密文内容 byte[]
     * @param iv
     *            解密参数中的IV值 byte[]
     * @return byte[] 解密后的明文 byte[]
     */
    public byte[] get3DesCBCDesCode(byte[] byteD, byte[] iv) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            IvParameterSpec ivp = new IvParameterSpec(iv);
            cipher = Cipher.getInstance("DESede/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, securekey, ivp);
            byteFina = cipher.doFinal(byteD);

        } catch (Exception e) {
            LogPay.error(e.getMessage(), e);
        } finally {
            cipher = null;
        }
        return byteFina;

    }

    /**
     * getDesCBCEncCode(DesCBC加密byte[]明文)
     *
     * @Title: getDesCBCEncCode
     * @Description: DesCBC加密byte[]明文
     * @Date May 3, 2014 210:39:25 AM
     * @modifyDate May 3, 2014 210:39:25 AM
     * @param key
     *            KEY值，加密需要的参数 byte[]
     * @param byteS
     *            需要加密的明文 byte[]
     * @param iv
     *            加密参数中的IV值 byte[]
     * @return byte[] 加密后的密文 byte[]
     */
    public byte[] getDesCBCEncCode(byte[] key, byte[] byteS, byte[] iv) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            SecretKey k = null;
            k = new SecretKeySpec(key, "DES");
            IvParameterSpec ivp = new IvParameterSpec(iv);
            cipher = Cipher.getInstance("DES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, k, ivp);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            LogPay.error(e.getMessage(), e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * getDesCBCDesCode(DesCBC解密byte[]密文)
     * 
     * @Title: getDesCBCDesCode
     * @Description: DesCBC解密byte[]密文
     * @Date May 3, 2014 210:41:36 AM
     * @modifyDate May 3, 2014 210:41:36 AM
     * @param key
     *            KEY值，解密需要的参数 byte[]
     * @param byteD
     *            需要解密的密文 byte[]
     * @param iv
     *            解密参数中的IV值 byte[]
     * @return byte[] 解密后的明文 byte[]
     */
    public byte[] getDesCBCDesCode(byte[] key, byte[] byteD, byte[] iv) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            SecretKey k = null;
            k = new SecretKeySpec(key, "DES");
            IvParameterSpec ivp = new IvParameterSpec(iv);
            cipher = Cipher.getInstance("DES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, k, ivp);
            byteFina = cipher.doFinal(byteD);

        } catch (Exception e) {
            LogPay.error(e.getMessage(), e);
        } finally {
            cipher = null;
        }
        return byteFina;

    }

    /**
     * getDesECBEncCode(DesECB加密byte[]明文)
     *
     * @Title: getDesECBEncCode
     * @Description: DesECB加密byte[]明文
     * @Date May 3, 2014 210:44:51 AM
     * @modifyDate May 3, 2014 210:44:51 AM
     * @param key
     *            KEY值，加密需要的参数 byte[]
     * @param byteS
     *            需要加密的明文 byte[]
     * @return byte[] 加密后的明文 byte[]
     */
    public byte[] getDesECBEncCode(byte[] key, byte[] byteS) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            SecretKey k = null;
            k = new SecretKeySpec(key, "DES");
            // IvParameterSpec ivp = new IvParameterSpec(iv);
            cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, k);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            e.printStackTrace();
            LogPay.error(e.getMessage(), e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * getDesECBDesCode(DesECB解密byte[]密文)
     * 
     * @Title: getDesECBDesCode
     * @Description: DesECB解密byte[]密文
     * @Date May 3, 2014 210:46:55 AM
     * @modifyDate May 3, 2014 210:46:55 AM
     * @param key
     *            KEY值，解密需要的参数 byte[]
     * @param byteD
     *            需要解密的密文内容 byte[]
     * @return byte[] 解密后的明文 byte[]
     */
    public byte[] getDesECBDesCode(byte[] key, byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            SecretKey k = null;
            k = new SecretKeySpec(key, "DES");
            // IvParameterSpec ivp = new IvParameterSpec(iv);
            cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, k);
            byteFina = cipher.doFinal(byteD);

        } catch (Exception e) {
            LogPay.error(e.getMessage(), e);
        } finally {
            cipher = null;
        }
        return byteFina;

    }

}
