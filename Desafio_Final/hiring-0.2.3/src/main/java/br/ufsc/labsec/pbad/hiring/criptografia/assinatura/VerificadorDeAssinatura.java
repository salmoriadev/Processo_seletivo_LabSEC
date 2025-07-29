package br.ufsc.labsec.pbad.hiring.criptografia.assinatura;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class VerificadorDeAssinatura {

    public boolean verificarAssinatura(CMSSignedData assinatura, X509Certificate certificadoAcRaiz) {
        try {
            // Pega as informações e o certificado do assinante de dentro do CMS
            SignerInformation informacoesDoAssinante = this.pegaInformacoesAssinatura(assinatura);
            X509Certificate certificadoAssinante = this.pegaCertificadoDoAssinante(assinatura);

            boolean confiancaEstabelecida = validarCaminhoDeCertificacao(certificadoAssinante, certificadoAcRaiz);
            if (!confiancaEstabelecida) {
                System.err.println("O caminho de certificação do assinante não é válido.");
                return false;
            }
            System.out.println("  - SUCESSO: Confiança no certificado estabelecida.");

            // Executa se a confiança foi estabelecida
            SignerInformationVerifier verificadorCriptografico = this.geraVerificadorInformacoesAssinatura(certificadoAssinante);
            boolean assinaturaIntegra = informacoesDoAssinante.verify(verificadorCriptografico);
            if (!assinaturaIntegra) {
                System.err.println("A assinatura não corresponde ao documento.");
            }

            return assinaturaIntegra;

        } catch (Exception e) {
            System.err.println("Erro fatal durante a verificação: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Valida a cadeia de certificados
    private boolean validarCaminhoDeCertificacao(X509Certificate certificadoAssinante, X509Certificate certificadoAcRaiz) {
        try {
            // Cria a âncora de confiança (Trust Anchor)
            TrustAnchor ancoraDeConfianca = new TrustAnchor(certificadoAcRaiz, null);
            Set<TrustAnchor> ancoras = Set.of(ancoraDeConfianca);

            // Cria o caminho a ser validado
            List<Certificate> cadeiaDeCertificados = new ArrayList<>();
            cadeiaDeCertificados.add(certificadoAssinante);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            CertPath caminhoDeCertificacao = cf.generateCertPath(cadeiaDeCertificados);

            PKIXParameters parametros = new PKIXParameters(ancoras);
            parametros.setRevocationEnabled(false);

            // Validação
            CertPathValidator validador = CertPathValidator.getInstance("PKIX");
            validador.validate(caminhoDeCertificacao, parametros);
            return true;
        } catch (CertPathValidatorException e) {
            System.err.println("Erro de validação do caminho: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Erro ao construir o caminho de certificação: " + e.getMessage());
            return false;
        }
    }
    // Extrai o certificado do assinante de dentro da estrutura CMS
    private X509Certificate pegaCertificadoDoAssinante(CMSSignedData assinatura) throws CertificateException {
        Store<X509CertificateHolder> certStore = assinatura.getCertificates();
        Collection<X509CertificateHolder> certs = certStore.getMatches(null);
        X509CertificateHolder holder = certs.iterator().next();
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(holder);
    }

    private SignerInformationVerifier geraVerificadorInformacoesAssinatura(X509Certificate certificado) throws OperatorCreationException {
        return new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(certificado);
    }


     // Pega as informações da assinatura dentro do CMS
    private SignerInformation pegaInformacoesAssinatura(CMSSignedData assinatura) {
        return assinatura.getSignerInfos().iterator().next();
    }
}