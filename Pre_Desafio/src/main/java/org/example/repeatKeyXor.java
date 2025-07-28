package org.example;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
// EXERCICIO 05 CRYPTOPALS
public class repeatKeyXor {
    public static void main(String[] args) {
        String texto = "Burning 'em, if you ain't quick and nimble I go crazy when I hear a cymbal";
        String chave = "ICE";

        byte[] bytesTexto = texto.getBytes(StandardCharsets.UTF_8);
        byte[] bytesChave = chave.getBytes(StandardCharsets.UTF_8);
        int tamanho = bytesTexto.length;
        byte[] textoCifradoBytes = new byte[tamanho];
        for (int i = 0; i < bytesTexto.length; i++) {
            textoCifradoBytes[i] = (byte) (bytesChave[i % 3] ^ bytesTexto[i]);
        }
        String textoCifradoHex = HexFormat.of().formatHex(textoCifradoBytes);

        System.out.println(textoCifradoHex);
    }
}
