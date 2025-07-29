package br.ufsc.labsec.pbad.hiring.criptografia.assinatura;

import br.ufsc.labsec.pbad.hiring.Constantes;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableFile;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.SignerInfoGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class GeradorDeAssinatura {
    private X509Certificate certificado;
    private PrivateKey chavePrivada;
    private final CMSSignedDataGenerator geradorAssinaturaCms;

    public GeradorDeAssinatura() {
        this.geradorAssinaturaCms = new CMSSignedDataGenerator();
    }

    // Configura quem será o assinante
    public void informaAssinante(X509Certificate certificado, PrivateKey chavePrivada) {
        this.certificado = certificado;
        this.chavePrivada = chavePrivada;
    }

    // Faz a criação da assinatura CMS
    public CMSSignedData assinar(String caminhoDocumento) throws OperatorCreationException, CertificateEncodingException, CMSException {
        CMSTypedData dadosParaAssinar = new CMSProcessableFile(new File(caminhoDocumento));
        SignerInfoGenerator signerInfoGenerator = preparaInformacoesAssinante(this.chavePrivada, this.certificado);

        // Adiciona o assinante à estrutura da assinatura
        geradorAssinaturaCms.addSignerInfoGenerator(signerInfoGenerator);

        // Armazena o certificado do assinante e o anexa à assinatura
        List<X509Certificate> listaDeCertificados = new ArrayList<>();
        listaDeCertificados.add(this.certificado);
        Store<X509Certificate> armazemDeCertificados = new JcaCertStore(listaDeCertificados);
        geradorAssinaturaCms.addCertificates(armazemDeCertificados);

        // Gera a assinatura final. O "true" indica que o documento original
        return geradorAssinaturaCms.generate(dadosParaAssinar, true);
    }

    private SignerInfoGenerator preparaInformacoesAssinante(PrivateKey chavePrivada, Certificate certificado) throws OperatorCreationException, CertificateEncodingException {
        // Cria o assinador
        ContentSigner contentSigner = new JcaContentSignerBuilder(Constantes.algoritmoAssinatura)
                .setProvider("BC").build(chavePrivada);
        JcaSignerInfoGeneratorBuilder signerInfoBuilder = new JcaSignerInfoGeneratorBuilder(
                new JcaDigestCalculatorProviderBuilder().setProvider("BC").build());

        return signerInfoBuilder.build(contentSigner, (X509Certificate) certificado);
    }


    // Escreve os bytes da assinatura
    public void escreveAssinatura(OutputStream arquivo, CMSSignedData assinatura) throws IOException {
        arquivo.write(assinatura.getEncoded());
        arquivo.close();
    }
}
