package br.ufsc.labsec.pbad.hiring.criptografia.certificado;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public class EscritorDeCertificados {
    public static void escreveCertificado(String nomeArquivo,
                                          X509Certificate certificado) {
        File arquivo = new File(nomeArquivo);
        arquivo.getParentFile().mkdirs();

        try (PemWriter pemWriter = new PemWriter(new FileWriter(arquivo))) {
            // Botei a descrição padrão para um certificado X.509
            PemObject pemObject = new PemObject("CERTIFICATE", certificado.getEncoded());
            pemWriter.writeObject(pemObject);

        } catch (IOException | CertificateEncodingException e) {
            System.err.println("ERRO: Falha ao escrever o certificado no arquivo: " + nomeArquivo);
            e.printStackTrace();
        }
    }
}
