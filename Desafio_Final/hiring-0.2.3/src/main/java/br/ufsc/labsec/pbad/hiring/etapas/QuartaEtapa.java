package br.ufsc.labsec.pbad.hiring.etapas;

import br.ufsc.labsec.pbad.hiring.Constantes;
import br.ufsc.labsec.pbad.hiring.criptografia.certificado.LeitorDeCertificados;
import br.ufsc.labsec.pbad.hiring.criptografia.chave.LeitorDeChaves;
import br.ufsc.labsec.pbad.hiring.criptografia.repositorio.GeradorDeRepositorios;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class QuartaEtapa {

    public static void executarEtapa() {
        System.out.println("Quarta Etapa: Geração de repositórios PKCS#12");

        try {

            // Gerar repositório para o usuario
            PrivateKey chavePrivadaUsuario = LeitorDeChaves.lerChavePrivadaDoDisco(
                    Constantes.caminhoChavePrivadaUsuario,
                    Constantes.algoritmoChave
            );

            X509Certificate certUsuario = LeitorDeCertificados.lerCertificadoDoDisco(Constantes.caminhoCertificadoUsuario);

            GeradorDeRepositorios.gerarPkcs12(
                    chavePrivadaUsuario,
                    certUsuario,
                    Constantes.caminhoPkcs12Usuario,
                    Constantes.aliasUsuario,
                    Constantes.senhaMestre
            );

            // Gera repositório para a AC-RAIZ
            PrivateKey chavePrivadaAc = LeitorDeChaves.lerChavePrivadaDoDisco(
                    Constantes.caminhoChavePrivadaAc,
                    Constantes.algoritmoChave
            );
            X509Certificate certAc = LeitorDeCertificados.lerCertificadoDoDisco(Constantes.caminhoCertificadoAcRaiz);
            GeradorDeRepositorios.gerarPkcs12(
                    chavePrivadaAc,
                    certAc,
                    Constantes.caminhoPkcs12AcRaiz,
                    Constantes.aliasAc,
                    Constantes.senhaMestre
            );

            System.out.println("Quarta Etapa concluída.");

        } catch (Exception e) {
            System.err.println("Erro ao executar a Quarta Etapa: " + e.getMessage());
            e.printStackTrace();
        }
    }
}