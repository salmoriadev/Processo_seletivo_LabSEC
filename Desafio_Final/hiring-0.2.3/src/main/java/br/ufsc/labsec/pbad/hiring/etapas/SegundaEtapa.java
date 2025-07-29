package br.ufsc.labsec.pbad.hiring.etapas;

import br.ufsc.labsec.pbad.hiring.Constantes;
import br.ufsc.labsec.pbad.hiring.criptografia.chave.EscritorDeChaves;
import br.ufsc.labsec.pbad.hiring.criptografia.chave.GeradorDeChaves;
import java.security.KeyPair;

public class SegundaEtapa {
    public static void executarEtapa() {
        System.out.println("Segunda etapa: geração de pares de chaves");
        try {

            GeradorDeChaves gerador = new GeradorDeChaves();

            // Gerar e armazenar o par de chaves do usuário (256 bits)
            KeyPair parDeChavesUsuario = gerador.gerarParDeChaves(256);
            EscritorDeChaves.escreveChaveEmDisco(parDeChavesUsuario.getPrivate(), Constantes.caminhoChavePrivadaUsuario);
            EscritorDeChaves.escreveChaveEmDisco(parDeChavesUsuario.getPublic(), Constantes.caminhoChavePublicaUsuario);
            System.out.println("Chaves do usuário salvas em formato PEM.");
            System.out.println("Chave privada salva em: " + Constantes.caminhoChavePrivadaUsuario);
            System.out.println("Chave pública salva em: " + Constantes.caminhoChavePublicaUsuario);

            // Gerar e armazenar o par de chaves da AC-Raiz (521 bits)
            KeyPair parDeChavesRaiz = gerador.gerarParDeChaves(521);
            EscritorDeChaves.escreveChaveEmDisco(parDeChavesRaiz.getPrivate(), Constantes.caminhoChavePrivadaAc);
            EscritorDeChaves.escreveChaveEmDisco(parDeChavesRaiz.getPublic(), Constantes.caminhoChavePublicaAc);
            System.out.println("Chaves da AC-Raiz salvas em formato PEM.");
            System.out.println("Chave privada salva em: " + Constantes.caminhoChavePrivadaAc);
            System.out.println("Chave pública salva em: " + Constantes.caminhoChavePublicaAc);

        } catch (Exception e) {
            System.err.println("Ocorreu um erro durante a geração ou escrita das chaves.");
            e.printStackTrace();
        }
        System.out.println("Segunda etapa concluída.");
    }
}
