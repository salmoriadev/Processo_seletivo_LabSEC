package br.ufsc.labsec.pbad.hiring.criptografia.certificado;

import br.ufsc.labsec.pbad.hiring.Constantes;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

public class GeradorDeCertificados {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public X509Certificate gerar(PublicKey chavePublicaTitular, PrivateKey chavePrivadaEmissor,
                                 X509Certificate certificadoEmissor, // Necessário para a extensão AuthorityKeyIdentifier
                                 BigInteger numeroDeSerie, String nomeTitular, String nomeEmissor,
                                 int diasDeValidade, boolean ehAC) throws Exception {

        X500Name emissor = new X500Name(nomeEmissor);
        X500Name titular = new X500Name(nomeTitular);
        long agora = System.currentTimeMillis();
        Date dataInicio = new Date(agora);
        Date dataFim = new Date(agora + (diasDeValidade * 24L * 60 * 60 * 1000));

        // Usa o construtor de certificados v3
        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                emissor, numeroDeSerie, dataInicio, dataFim, titular, chavePublicaTitular);

        JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

        // Adiciona extensões padrões para os certificados
        certBuilder.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(chavePublicaTitular));

        // Adiciona extensões específicas
        if (ehAC) {
            certBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
            certBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));
        } else {
            certBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
            certBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature));
            // Cria um link para o certificado da AC que o emitiu
            certBuilder.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(certificadoEmissor));
        }

        // Prepara o assinador
        ContentSigner contentSigner = new JcaContentSignerBuilder(Constantes.algoritmoAssinatura)
                .setProvider("BC").build(chavePrivadaEmissor);

        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBuilder.build(contentSigner));
    }
}