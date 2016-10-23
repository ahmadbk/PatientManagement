package com.example.ahmad.patientmanagement;

import android.util.Base64;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Ahmad on 2016/10/22.
 */

public class RSA {

    static BigInteger myModulus = new BigInteger("152481331966768199373425934608648903602478812673665816188998849849776173710832336632893264313603006726654330489328199210106130513893830006229428483983727497292286521840642613862839092262823392533600799632706652948212750489179032345707339931817251466638636191041008428664370095455122614532875975730472076484681");
    static BigInteger yourModulus = new BigInteger("151844520527794925307687652339970974960073202322215756421636249876327588287234032993142349622570891483578519231038843984890528287019653333597725292041740257940253493103405751374030718458938940446924594760509874696716530872252367404801110109342310431756326210867036706581854841510325178503029648727799628161989");
    static BigInteger exponent = new BigInteger("65537");
    static BigInteger privateExponent = new BigInteger("107404912150112461474822793526390578406109974967643240806456717665062750919528711353651399369956751140921065327660064698355574757966963311985124541955548946661723911920016600615948106844472995856980486243651252423794139548958173837312474073861248397332748949588160153210917259236320993938802074702687720058061");

    private static Key privateKey = getPrivateKey(myModulus, privateExponent);
    private static Key publicKey = getKey(yourModulus, exponent);
    private static Cipher cipher;


    public static String encrypt(String plainText){

        byte[] input = plainText.getBytes();
        System.out.println(publicKey.getFormat());
        try{
            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherText = cipher.doFinal(input);
            return Base64.encodeToString(cipherText, Base64.DEFAULT);
        }
        catch (InvalidKeyException e){} catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String decrypt(String input){
        byte[] cipherText = Base64.decode(input, Base64.DEFAULT);
        try{
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText);
        }
        catch (InvalidKeyException e){} catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Key getKey(BigInteger modulus, BigInteger exponent){
        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = null;
        try {
            factory = KeyFactory.getInstance("RSA");
            PublicKey pub = factory.generatePublic(spec);
            return pub;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;

    }


    public static Key getPrivateKey(BigInteger modulus, BigInteger exponent){
        RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus, exponent);
        KeyFactory factory = null;
        try {
            factory = KeyFactory.getInstance("RSA");
            Key pub = factory.generatePrivate(spec);
            return pub;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;

    }
}