package org.example;

import org.bouncycastle.util.encoders.Hex;
import java.util.HashMap;
import java.util.Map;
// EXERCICIO 03 CRYPTOPALS
public class singleByteXor {
    // Fiz um mapa quantizando a frequencia de letras em ingles
    private static final Map<Character, Integer> FREQUENCIA_LETRAS = new HashMap<>();
    static {
        // Coloquei todos os dados que encontrei no site multiplicados por 100
        FREQUENCIA_LETRAS.put(' ', 1800); // Adicionei o espaço sendo o mais utilizado
        FREQUENCIA_LETRAS.put('a', 757); FREQUENCIA_LETRAS.put('b', 184);
        FREQUENCIA_LETRAS.put('c', 409); FREQUENCIA_LETRAS.put('d', 338);
        FREQUENCIA_LETRAS.put('e', 1151); FREQUENCIA_LETRAS.put('f', 123);
        FREQUENCIA_LETRAS.put('g', 270); FREQUENCIA_LETRAS.put('h', 232);
        FREQUENCIA_LETRAS.put('i', 901); FREQUENCIA_LETRAS.put('j', 16);
        FREQUENCIA_LETRAS.put('k', 85); FREQUENCIA_LETRAS.put('l', 531);
        FREQUENCIA_LETRAS.put('m', 284); FREQUENCIA_LETRAS.put('n', 685);
        FREQUENCIA_LETRAS.put('o', 659); FREQUENCIA_LETRAS.put('p', 294);
        FREQUENCIA_LETRAS.put('q', 16); FREQUENCIA_LETRAS.put('r', 707);
        FREQUENCIA_LETRAS.put('s', 952); FREQUENCIA_LETRAS.put('t', 668);
        FREQUENCIA_LETRAS.put('u', 327); FREQUENCIA_LETRAS.put('v', 98);
        FREQUENCIA_LETRAS.put('w', 74); FREQUENCIA_LETRAS.put('x', 29);
        FREQUENCIA_LETRAS.put('y', 163); FREQUENCIA_LETRAS.put('z', 47);
    }

    // Records para guardar os dados
    public record resultadoDecifrado(String texto, int pontuacao, byte chave) {}


    public static void main(String[] args) {
        String hexCifra = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
        // Achar a chave dentro de todas as 256 chaves hexadecimais possiveis
        resultadoDecifrado resultado = acharChave(hexCifra);

        System.out.println(resultado.texto());
    }

    public static resultadoDecifrado acharChave(String entradaHex) {
        byte[] entradaHexBytes = Hex.decode(entradaHex);
        resultadoDecifrado melhorResultado = null;

        for (int chave = 0; chave < 256; chave++) {
            byte[] textoEmBytes = new byte[entradaHexBytes.length];
            for (int i = 0; i < entradaHexBytes.length; i++) {
                textoEmBytes[i] = (byte) (entradaHexBytes[i] ^ chave);
            }

            int currentScore = pontuacaoTexto(textoEmBytes);

            if (melhorResultado == null || currentScore > melhorResultado.pontuacao()) {
                melhorResultado = new resultadoDecifrado(new String(textoEmBytes), currentScore, (byte) chave);
            }
        }
        return melhorResultado;
    }
    // Algoritmo para pontuar o quao parecido com ingles é
    public static int pontuacaoTexto(byte[] textoBytes) {
        int pontuacao = 0;
        for (byte b : textoBytes) {
            char c = (char) b;
            char transformarMinusculo = Character.toLowerCase(c);

            // Pontos são acumulados
            pontuacao += FREQUENCIA_LETRAS.getOrDefault(transformarMinusculo, 0);

            // Tiro pontos de textos com caracteres não usados na lingua inglesa para melhores resultados
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c) && c != '\'' && c != ',' && c != '.') {
                pontuacao -= 5;
            }
        }
        return pontuacao;
    }
}