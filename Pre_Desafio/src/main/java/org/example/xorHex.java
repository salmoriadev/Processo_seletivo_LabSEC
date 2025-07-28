package org.example;
import org.bouncycastle.util.encoders.Hex;
// EXERCICIO 02 CRYPTOPALS
public class xorHex {
    public static void main(String[] args) {
        String hex01 = "1c0111001f010100061a024b53535009181c";
        String hex02 = "686974207468652062756c6c277320657965";

        byte[] bytes1 = Hex.decode(hex01);
        byte[] bytes2 = Hex.decode(hex02);
        int tamanho = Math.min(bytes1.length, bytes2.length);
        byte[] resultadoBytes = new byte[tamanho];
        for (int i = 0; i < tamanho; i++) {
            resultadoBytes[i] = (byte) (bytes1[i] ^ bytes2[i]);
        }

        System.out.println(Hex.toHexString(resultadoBytes));
    }
}
