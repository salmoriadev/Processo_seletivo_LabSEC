package br.ufsc.labsec.pbad.hiring.etapas;

import br.ufsc.labsec.pbad.hiring.Constantes;
import br.ufsc.labsec.pbad.hiring.criptografia.assinatura.VerificadorDeAssinatura;
import br.ufsc.labsec.pbad.hiring.criptografia.repositorio.RepositorioChaves;
import org.bouncycastle.cms.CMSSignedData;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;

public class SextaEtapa {

    public static void executarEtapa() {
        System.out.println("Sexta Etapa: Verificação completa da assinatura");

        try {
            // Carrega o arquivo
            byte[] bytesDaAssinatura = Files.readAllBytes(Paths.get(Constantes.caminhoAssinatura));
            CMSSignedData assinatura = new CMSSignedData(bytesDaAssinatura);
            System.out.println("Arquivo de assinatura lido: " + Constantes.caminhoAssinatura);

            // Carrega o certificado da AC-Raiz
            RepositorioChaves repositorioAcRaiz = new RepositorioChaves(
                    Constantes.aliasAc,
                    Constantes.senhaMestre
            );
            repositorioAcRaiz.abrir(Constantes.caminhoPkcs12AcRaiz);
            X509Certificate certificadoAcRaiz = repositorioAcRaiz.pegarCertificado();
            System.out.println("Âncora de confiança carregada: " + certificadoAcRaiz.getSubjectX500Principal());

            // Faz a verificação
            VerificadorDeAssinatura verificador = new VerificadorDeAssinatura();
            boolean ehValida = verificador.verificarAssinatura(assinatura, certificadoAcRaiz);

            System.out.println("\n--- RESULTADO FINAL DA VERIFICAÇÃO ---");
            if (ehValida) {
                System.out.println(">>> ASSINATURA VÁLIDA E CONFIÁVEL <<<");
            } else {
                System.out.println(">>> ASSINATURA INVÁLIDA OU NÃO CONFIÁVEL <<<");
            }

            System.out.println("Sexta Etapa concluída.");

        } catch (Exception e) {
            System.err.println("ERRO ao executar a Sexta Etapa: " + e.getMessage());
            e.printStackTrace();
        }
    }
}