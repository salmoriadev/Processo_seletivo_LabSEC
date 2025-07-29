package br.ufsc.labsec.pbad.hiring.criptografia.chave;

import br.ufsc.labsec.pbad.hiring.Constantes;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class GeradorDeChaves {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private final KeyPairGenerator generator;

    public GeradorDeChaves() throws NoSuchProviderException, NoSuchAlgorithmException {
        this.generator = KeyPairGenerator.getInstance(Constantes.algoritmoChave, "BC");
    }

    public KeyPair gerarParDeChaves(int tamanhoDaChave) throws InvalidAlgorithmParameterException {
        String nomeDaCurva = switch (tamanhoDaChave) {
            case 256 -> "secp256r1"; // Curva padrão para 256 bits
            case 521 -> "secp521r1"; // Curva padrão para 521 bits
            default -> throw new InvalidAlgorithmParameterException("Tamanho de chave não suportado para EC: " + tamanhoDaChave);
        };

        ECGenParameterSpec ecSpec = new ECGenParameterSpec(nomeDaCurva);
        generator.initialize(ecSpec, new SecureRandom());
        return generator.generateKeyPair();
    }
}