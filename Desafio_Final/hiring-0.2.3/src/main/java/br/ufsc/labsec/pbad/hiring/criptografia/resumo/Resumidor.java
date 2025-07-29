package br.ufsc.labsec.pbad.hiring.criptografia.resumo;

import br.ufsc.labsec.pbad.hiring.Constantes;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.util.encoders.Hex;

public class Resumidor {

    private final MessageDigest md;

    public Resumidor() throws NoSuchAlgorithmException {  // Construtor
        this.md = MessageDigest.getInstance(Constantes.algoritmoResumo);
    }

    public byte[] resumir(File arquivoDeEntrada) throws IOException {
        byte[] todosOsBytesDoArquivo = Files.readAllBytes(arquivoDeEntrada.toPath());
        // Calcula o hash
        return md.digest(todosOsBytesDoArquivo);
    }

    public static String bytesToHex(byte[] hash) {
        return Hex.toHexString(hash);
    }
}