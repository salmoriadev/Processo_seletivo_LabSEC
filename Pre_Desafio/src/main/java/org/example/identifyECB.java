package org.example;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
// EXERCICIO 08 CRYPTOPALS
public class identifyECB {
    private static final int TAMANHO_BLOCO = 16; // AES usa blocos de 16 bytes como dito no exercicio 7

    public static void main(String[] args) throws Exception {
        String nomeDoArquivo = "CifraHex.txt";
        List<String> linhasHex = Files.readAllLines(Paths.get(nomeDoArquivo)); // Leitura e cria uma lista

        //Numeros para serem "superados"
        int linhaComMaisRepeticoes = -1;
        int maxRepeticoes = 0;
        String melhorCandidato = "";
        int numeroLinha = 0;

        // loop para cada linha
        for (String linhaHex : linhasHex) {
            numeroLinha++;
            byte[] dadosCifrados = Hex.decode(linhaHex);

            List<String> blocos = new ArrayList<>();
            for (int i = 0; i < dadosCifrados.length; i += TAMANHO_BLOCO) {
                byte[] blocoBytes = new byte[TAMANHO_BLOCO];
                System.arraycopy(dadosCifrados, i, blocoBytes, 0, TAMANHO_BLOCO);
                // Converti o bloco de bytes para uma string para comparar e ver o quanto que esse bloco se repete por linha
                blocos.add(new String(blocoBytes, StandardCharsets.ISO_8859_1));
            }

            Set<String> blocosUnicos = new HashSet<>(blocos);   // Converti em um conjunto para ver a repeticao
            int numRepeticoes = blocos.size() - blocosUnicos.size();

            if (numRepeticoes > maxRepeticoes) {
                maxRepeticoes = numRepeticoes;
                linhaComMaisRepeticoes = numeroLinha;
                melhorCandidato = linhaHex;
            }
        }

        if (linhaComMaisRepeticoes != -1) {
            System.out.println("Linha: " + linhaComMaisRepeticoes);
            System.out.println("Blocos repetidos: " + maxRepeticoes);
            System.out.println("Cifra: " + melhorCandidato);
        }
    }
}
