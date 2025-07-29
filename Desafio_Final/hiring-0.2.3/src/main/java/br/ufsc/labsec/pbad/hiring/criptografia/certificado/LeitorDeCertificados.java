package br.ufsc.labsec.pbad.hiring.criptografia.certificado;

import br.ufsc.labsec.pbad.hiring.Constantes;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class LeitorDeCertificados {

     // Lê um certificado X.509
    public static X509Certificate lerCertificadoDoDisco(String caminhoCertificado) {
        Security.addProvider(new BouncyCastleProvider());

        try {
            CertificateFactory cf = CertificateFactory.getInstance(Constantes.formatoCertificado, BouncyCastleProvider.PROVIDER_NAME);

            try (FileInputStream fis = new FileInputStream(caminhoCertificado)) {
                return (X509Certificate) cf.generateCertificate(fis);
            }

        } catch (CertificateException | IOException | NoSuchProviderException e) {
            System.err.println("Erro ao ler o certificado do disco: " + caminhoCertificado);
            e.printStackTrace();
            return null;
        }
    }
}