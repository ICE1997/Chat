package com.chzu.ice.chat;

import com.chzu.ice.chat.utils.RSAUtil;

import org.junit.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testRSAUtil() {
        try {
            KeyPair keyPair = RSAUtil.getKeyPair();
            String publicKeyStr = RSAUtil.getPublicKey(keyPair);
            String privateKeyStr = RSAUtil.getPrivateKey(keyPair);
            System.out.println("RSA公钥BASE64编码:" + publicKeyStr);
            System.out.println("RSA密钥BASE64编码:" + privateKeyStr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}