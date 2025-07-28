package org.example;
import org.bouncycastle.util.encoders.Hex;
import java.util.Base64;
// EXERCICIO 01 CRYPTOPALS
public class hexToBase64 {
    public static void main(String[] args) {
        String cifraHex = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        byte[] bytesHex = Hex.decode(cifraHex);
        String cifrabase64 = Base64.getEncoder().encodeToString(bytesHex);

        System.out.println(cifrabase64);
    }
}