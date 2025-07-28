package org.example;
import org.bouncycastle.util.encoders.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
// EXERCICIO 07 CRYPTOPALS
public class decryptAES {
    public static void main(String[] args) throws Exception {
        String chaveString = "YELLOW SUBMARINE";
        String arquivoCifra = "textoCifrado.txt"; // Coloquei assim o arquivo por ser um texto muito longo
        String mensagemCifrada = Files.readString(Paths.get(arquivoCifra), StandardCharsets.UTF_8);

        SecretKeySpec chave = new SecretKeySpec(chaveString.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cifra = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cifra.init(Cipher.DECRYPT_MODE, chave);

        byte[] dadosCifrados = Base64.decode(mensagemCifrada);
        byte[] dadosDescriptografados = cifra.doFinal(dadosCifrados);

        System.out.println(new String(dadosDescriptografados, StandardCharsets.UTF_8));
    }
}