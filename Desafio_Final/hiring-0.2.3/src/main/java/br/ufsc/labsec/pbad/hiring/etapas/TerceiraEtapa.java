package br.ufsc.labsec.pbad.hiring.etapas;

import br.ufsc.labsec.pbad.hiring.Constantes;
import br.ufsc.labsec.pbad.hiring.criptografia.certificado.EscritorDeCertificados;
import br.ufsc.labsec.pbad.hiring.criptografia.certificado.GeradorDeCertificados;
import br.ufsc.labsec.pbad.hiring.criptografia.chave.LeitorDeChaves;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class TerceiraEtapa {

    public static void executarEtapa() {
        System.out.println("Terceira etapa: geração de certificados digitais");

        try {
            GeradorDeCertificados geradorDeCertificados = new GeradorDeCertificados();

            // Carregar as chaves
            PublicKey chavePublicaUsuario = LeitorDeChaves.lerChavePublicaDoDisco(Constantes.caminhoChavePublicaUsuario, Constantes.algoritmoChave);
            PrivateKey chavePrivadaRaiz = LeitorDeChaves.lerChavePrivadaDoDisco(Constantes.caminhoChavePrivadaAc, Constantes.algoritmoChave);
            PublicKey chavePublicaRaiz = LeitorDeChaves.lerChavePublicaDoDisco(Constantes.caminhoChavePublicaAc, Constantes.algoritmoChave);

            // Gerando o certificado autoassinado da AC-Raiz
            X509Certificate certificadoRaiz = geradorDeCertificados.gerar(
                    chavePublicaRaiz,
                    chavePrivadaRaiz,
                    null, // Certificado do emissor é nulo, pois é autoassinado
                    new BigInteger(String.valueOf(Constantes.numeroSerieAc)),
                    Constantes.nomeAcRaiz,
                    Constantes.nomeAcRaiz,
                    3650,
                    true // certificado AC
            );
            EscritorDeCertificados.escreveCertificado(Constantes.caminhoCertificadoAcRaiz, certificadoRaiz);
            System.out.println("Certificado da AC-Raiz salvo em: " + Constantes.caminhoCertificadoAcRaiz);

            // Gerando o certificado do usuário assinado pela AC-Raiz
            X509Certificate certificadoUsuario = geradorDeCertificados.gerar(
                    chavePublicaUsuario,
                    chavePrivadaRaiz,
                    certificadoRaiz, // Passa o certificado da AC
                    new BigInteger(String.valueOf(Constantes.numeroDeSerie)),
                    Constantes.nomeUsuario,
                    Constantes.nomeAcRaiz,
                    365,
                    false // Não é um certificado de AC
            );
            EscritorDeCertificados.escreveCertificado(Constantes.caminhoCertificadoUsuario, certificadoUsuario);
            System.out.println("Certificado do usuário salvo em: " + Constantes.caminhoCertificadoUsuario);

        } catch (Exception e) {
            System.err.println("Ocorreu um erro durante a geração dos certificados.");
            e.printStackTrace();
        }
        System.out.println("Terceira etapa concluída.");
    }
}