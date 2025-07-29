package br.ufsc.labsec.pbad.hiring.criptografia.repositorio;

import br.ufsc.labsec.pbad.hiring.Constantes;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.X509Certificate;

public class RepositorioChaves {
    private KeyStore repositorio;
    private final char[] senha;
    private final String alias;

    public RepositorioChaves(String alias, char[] senha) {
        Security.addProvider(new BouncyCastleProvider());
        this.alias = alias;
        this.senha = senha;
    }

    // Abre o repositório e o carrega em memória
    public void abrir(String caminhoRepositorio) throws Exception {
        this.repositorio = KeyStore.getInstance(Constantes.formatoRepositorio, BouncyCastleProvider.PROVIDER_NAME);
        try (FileInputStream fis = new FileInputStream(caminhoRepositorio)) {
            this.repositorio.load(fis, this.senha);
        }
    }

    // Obtém a chave privada
    public PrivateKey pegarChavePrivada() throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        if (this.repositorio == null) {
            throw new KeyStoreException("O repositório não foi aberto. Chame o método 'abrir()' primeiro.");
        }
        return (PrivateKey) this.repositorio.getKey(this.alias, this.senha);
    }

    // Obtém o certificado
    public X509Certificate pegarCertificado() throws KeyStoreException {
        if (this.repositorio == null) {
            throw new KeyStoreException("O repositório não foi aberto. Chame o método 'abrir()' primeiro.");
        }
        return (X509Certificate) this.repositorio.getCertificate(this.alias);
    }
}