package br.ufsc.labsec.pbad.hiring.criptografia.chave;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.security.PublicKey;

public class EscritorDeChaves {

    public static void escreveChaveEmDisco(Key chave, String nomeDoArquivo) {
        File arquivo = new File(nomeDoArquivo);

        // Utiliza o PemWriter do Bouncy Castle para escrever no formato PEM
        try (PemWriter pemWriter = new PemWriter(new FileWriter(arquivo))) {
            // Verifica se a chave é uma instância de PublicKey ou PrivateKey
            String description = (chave instanceof PublicKey) ? "PUBLIC KEY" : "PRIVATE KEY";

            // Cria o objeto PEM e escreve no arquivo
            PemObject pemObject = new PemObject(description, chave.getEncoded());
            pemWriter.writeObject(pemObject);

        } catch (IOException e) {
            System.err.println("Falha ao escrever a chave no arquivo: " + nomeDoArquivo);
            e.printStackTrace();
        }
    }
}
