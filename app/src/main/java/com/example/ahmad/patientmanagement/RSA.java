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

    private Key privateKey;
    public Key publicKey;
    private Cipher cipher;
    private SecureRandom random;

    public RSA(){

        try {
            random = new SecureRandom();
            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

            generator.initialize(1024, random);

            KeyPair pair = generator.generateKeyPair();
            this.publicKey = pair.getPublic();
            this.privateKey = pair.getPrivate();
        }
        catch(NoSuchAlgorithmException e){e.printStackTrace();}
        catch (NoSuchPaddingException e) {e.printStackTrace();}

    }

    public byte[] encrypt(String plainText){
        byte[] input = plainText.getBytes();
        try{
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherText = cipher.doFinal(input);
            return cipherText;
        }
        catch (InvalidKeyException e){} catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] encrypt(String plainText, Key pubKey){
        byte[] input = plainText.getBytes();
        try{
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] cipherText = cipher.doFinal(input);
            return cipherText;
        }
        catch (InvalidKeyException e){} catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(byte[] cipherText){
        //byte[] cipherText = input.getBytes();
        try{
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] plainText = cipher.doFinal(cipherText);
            return Base64.encodeToString(plainText, Base64.DEFAULT);
        }
        catch (InvalidKeyException e){} catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(byte[] cipherText, Key key){
        //byte[] cipherText = input.getBytes();
        try{
            cipher.init(Cipher.DECRYPT_MODE, key);
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

    public Key getKey(BigInteger modulus, BigInteger exponent){
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


    public Key getPrivateKey(BigInteger modulus, BigInteger exponent){
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