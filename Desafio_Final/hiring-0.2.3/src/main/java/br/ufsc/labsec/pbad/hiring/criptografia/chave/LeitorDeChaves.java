package br.ufsc.labsec.pbad.hiring.criptografia.chave;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class LeitorDeChaves {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static PrivateKey lerChavePrivadaDoDisco(String caminhoChave, String algoritmo)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {

        // Lê o conteúdo do arquivo PEM
        PemObject pemObject;
        try (PemReader pemReader = new PemReader(new FileReader(caminhoChave))) {
            pemObject = pemReader.readPemObject();
        }

        byte[] chaveEmBytes = pemObject.getContent();
        // Converte os bytes em um objeto PrivateKey
        KeyFactory keyFactory = KeyFactory.getInstance(algoritmo, "BC");
        KeySpec keySpec = new PKCS8EncodedKeySpec(chaveEmBytes);

        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey lerChavePublicaDoDisco(String caminhoChave, String algoritmo)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {

        PemObject pemObject;
        try (PemReader pemReader = new PemReader(new FileReader(caminhoChave))) {
            pemObject = pemReader.readPemObject();
        }

        byte[] chaveEmBytes = pemObject.getContent();

        // Converte os bytes em um objeto PublicKey
        KeyFactory keyFactory = KeyFactory.getInstance(algoritmo, "BC");
        KeySpec keySpec = new X509EncodedKeySpec(chaveEmBytes);

        return keyFactory.generatePublic(keySpec);
    }
}
