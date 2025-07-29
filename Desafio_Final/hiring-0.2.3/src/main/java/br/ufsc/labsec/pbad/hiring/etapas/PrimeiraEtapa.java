package br.ufsc.labsec.pbad.hiring.etapas;

import br.ufsc.labsec.pbad.hiring.Constantes;
import br.ufsc.labsec.pbad.hiring.criptografia.resumo.Resumidor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;


public class PrimeiraEtapa {

    public static void executarEtapa() {
        System.out.println("Primeira etapa: cálculo de resumo criptográfico");

        try {
            File arquivoDeEntrada = new File(Constantes.caminhoTextoPlano);
            String caminhoArquivoSaida = Constantes.caminhoResumoCriptografico;

            Resumidor resumidor = new Resumidor();

            // Calcula hash do arquivo
            byte[] resumoEmBytes = resumidor.resumir(arquivoDeEntrada);

            // Converte hash de bytes para formato hexadecimal
            String resumoHexadecimal = Resumidor.bytesToHex(resumoEmBytes);

            // Armazenar em disco
            File arquivoSaida = new File(caminhoArquivoSaida);
            arquivoSaida.getParentFile().mkdirs();

            try (FileOutputStream fos = new FileOutputStream(arquivoSaida)) {
                fos.write(resumoHexadecimal.getBytes(StandardCharsets.UTF_8));
            }

            System.out.println("Resumo criptográfico (hash) calculado com sucesso.");
            System.out.println("Hash em hexadecimal: " + resumoHexadecimal);
            System.out.println("Resultado salvo em: " + caminhoArquivoSaida);

        } catch (IOException e) {
            System.err.println("Ocorreu um erro de leitura ou escrita durante a execução da primeira etapa.");
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Ocorreu um erro: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Primeira etapa concluída.");
    }
}