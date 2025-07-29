package br.ufsc.labsec.pbad.hiring.criptografia.repositorio;

import br.ufsc.labsec.pbad.hiring.Constantes;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class GeradorDeRepositorios {
    public static void gerarPkcs12(PrivateKey chavePrivada, X509Certificate certificado,
                                   String caminhoPkcs12, String alias, char[] senha) {
        Security.addProvider(new BouncyCastleProvider());

        try {
            KeyStore keyStore = KeyStore.getInstance(Constantes.formatoRepositorio, BouncyCastleProvider.PROVIDER_NAME);
            keyStore.load(null, null);
            keyStore.setKeyEntry(alias, chavePrivada, senha, new Certificate[]{certificado}); //senha de acesso ao PKCS#12

            // Configurações de segurança para o PKCS#12
            Security.setProperty("org.bouncycastle.asn1.pkcs.PBES2Parameters.Cipher", "AES256");
            Security.setProperty("org.bouncycastle.asn1.pkcs.PBES2Parameters.KDF", "PBKDF2");
            Security.setProperty("org.bouncycastle.asn1.pkcs.PBKDF2Params.IterationCount", "100000");

            try (FileOutputStream fos = new FileOutputStream(caminhoPkcs12)) {
                keyStore.store(fos, senha);
            }

            System.out.println("Repositório PKCS#12 gerado com sucesso em: " + caminhoPkcs12);

        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException |
                 NoSuchProviderException e) {
            System.err.println("Erro ao gerar o repositório PKCS#12: " + e.getMessage());
            e.printStackTrace();
        }
    }
}