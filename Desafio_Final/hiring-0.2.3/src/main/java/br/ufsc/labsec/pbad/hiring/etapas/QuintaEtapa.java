package br.ufsc.labsec.pbad.hiring.etapas;

import br.ufsc.labsec.pbad.hiring.Constantes;
import br.ufsc.labsec.pbad.hiring.criptografia.assinatura.GeradorDeAssinatura;
import br.ufsc.labsec.pbad.hiring.criptografia.repositorio.RepositorioChaves;
import org.bouncycastle.cms.CMSSignedData;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class QuintaEtapa {

    public static void executarEtapa() {
        System.out.println("Quinta Etapa: Geração de assinatura digital");

        try {
            RepositorioChaves repositorioUsuario = new RepositorioChaves(
                    Constantes.aliasUsuario,
                    Constantes.senhaMestre
            );

            // Abre o arquivo .p12
            repositorioUsuario.abrir(Constantes.caminhoPkcs12Usuario);

            // Extrai a chave privada e o certificado
            PrivateKey chavePrivadaAssinante = repositorioUsuario.pegarChavePrivada();
            X509Certificate certificadoAssinante = repositorioUsuario.pegarCertificado();

            // Instancia o gerador e informa quem será o assinante
            GeradorDeAssinatura geradorAssinatura = new GeradorDeAssinatura();
            geradorAssinatura.informaAssinante(certificadoAssinante, chavePrivadaAssinante);

            // Chama o método para assinar o documento
            CMSSignedData assinatura = geradorAssinatura.assinar(Constantes.caminhoTextoPlano);
            System.out.println("Assinatura digital (CMS) do tipo 'anexada' gerada.");

            // Grava a assinatura em disco
            try (FileOutputStream fos = new FileOutputStream(Constantes.caminhoAssinatura)) {
                geradorAssinatura.escreveAssinatura(fos, assinatura);
            }
            System.out.println("Assinatura salva com sucesso em: " + Constantes.caminhoAssinatura);

            System.out.println("Quinta Etapa concluída.");

        } catch (Exception e) {
            System.err.println("ERRO ao executar a Quinta Etapa: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
