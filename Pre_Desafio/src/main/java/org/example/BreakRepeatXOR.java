package org.example;
import org.bouncycastle.util.encoders.Base64; // Importa a classe Base64 do Bouncy Castle para decodificação

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BreakRepeatXOR {


    private static final Map<Character, Integer> ENGLISH_FREQUENCIES = new HashMap<>();
    static {
        ENGLISH_FREQUENCIES.put(' ', 1800);
        ENGLISH_FREQUENCIES.put('a', 757); ENGLISH_FREQUENCIES.put('b', 184);
        ENGLISH_FREQUENCIES.put('c', 409); ENGLISH_FREQUENCIES.put('d', 338);
        ENGLISH_FREQUENCIES.put('e', 1151); ENGLISH_FREQUENCIES.put('f', 123);
        ENGLISH_FREQUENCIES.put('g', 270); ENGLISH_FREQUENCIES.put('h', 232);
        ENGLISH_FREQUENCIES.put('i', 901); ENGLISH_FREQUENCIES.put('j', 16);
        ENGLISH_FREQUENCIES.put('k', 85); ENGLISH_FREQUENCIES.put('l', 531);
        ENGLISH_FREQUENCIES.put('m', 284); ENGLISH_FREQUENCIES.put('n', 685);
        ENGLISH_FREQUENCIES.put('o', 659); ENGLISH_FREQUENCIES.put('p', 294);
        ENGLISH_FREQUENCIES.put('q', 16); ENGLISH_FREQUENCIES.put('r', 707);
        ENGLISH_FREQUENCIES.put('s', 952); ENGLISH_FREQUENCIES.put('t', 668);
        ENGLISH_FREQUENCIES.put('u', 327); ENGLISH_FREQUENCIES.put('v', 98);
        ENGLISH_FREQUENCIES.put('w', 74); ENGLISH_FREQUENCIES.put('x', 29);
        ENGLISH_FREQUENCIES.put('y', 163); ENGLISH_FREQUENCIES.put('z', 47);
    }

    public static int hammingDistance(byte[] bytes1, byte[] bytes2) {
        if (bytes1.length != bytes2.length) {
            throw new IllegalArgumentException("Os arrays de bytes devem ter o mesmo comprimento para calcular a distância de Hamming.");
        }

        int distance = 0;
        for (int i = 0; i < bytes1.length; i++) {
            int xorResult = bytes1[i] ^ bytes2[i];
            distance += Integer.bitCount(xorResult & 0xFF);
        }
        return distance;
    }


    public static byte[] singleByteXORDecrypt(byte[] ciphertext, byte keyByte) {
        byte[] plaintext = new byte[ciphertext.length];
        for (int i = 0; i < ciphertext.length; i++) {
            plaintext[i] = (byte) (ciphertext[i] ^ keyByte);
        }
        return plaintext;
    }

    public static int scorePlaintext(byte[] plaintextBytes) {
        int score = 0;
        for (byte b : plaintextBytes) {
            char c = (char) (b & 0xFF); // Trata como unsigned
            char transformarMinusculo = Character.toLowerCase(c);

            score += ENGLISH_FREQUENCIES.getOrDefault(transformarMinusculo, 0);

            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c) && c != '\'' && c != ',' && c != '.') {
                score -= 5;
            }
        }
        return score;
    }

    public static byte findSingleByteXORKey(byte[] ciphertextBytes) {
        byte bestKey = 0;
        int maxScore = Integer.MIN_VALUE;

        for (int key = 0; key <= 255; key++) {
            byte[] decrypted = singleByteXORDecrypt(ciphertextBytes, (byte) key);
            int currentScore = scorePlaintext(decrypted);

            if (currentScore > maxScore) {
                maxScore = currentScore;
                bestKey = (byte) key;
            }
        }
        return bestKey;
    }

    private static String readFileContent(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .collect(Collectors.joining(""));
    }

    public static void main(String[] args) {
        byte[] ciphertextBytes;
        try {

            String fileContent = readFileContent("texto6.txt");
            ciphertextBytes = Base64.decode(fileContent);
            System.out.println("Texto cifrado lido do arquivo e decodificado Base64. Tamanho: " + ciphertextBytes.length + " bytes.");
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo texto6: " + e.getMessage());
            return;
        }

        String test1 = "this is a test";
        String test2 = "wokka wokka!!!";
        byte[] testBytes1 = test1.getBytes(StandardCharsets.US_ASCII);
        byte[] testBytes2 = test2.getBytes(StandardCharsets.US_ASCII);
        int testHammingDistance = hammingDistance(testBytes1, testBytes2);
        System.out.println("\nDistância de Hamming entre \"" + test1 + "\" e \"" + test2 + "\": " + testHammingDistance);
        System.out.println("Esperado: 37. Resultado: " + (testHammingDistance == 37 ? "Correto!" : "Incorreto!"));

        List<Map.Entry<Integer, Double>> keySizeScores = new ArrayList<>();


        for (int keySize = 2; keySize <= 40; keySize++) {

            int numBlocksToCompare = Math.min(4, ciphertextBytes.length / keySize);
            if (numBlocksToCompare < 2) {
                continue;
            }

            double totalNormalizedDistance = 0;
            int comparisonsMade = 0;

            for (int i = 0; i < numBlocksToCompare - 1; i++) {
                byte[] block1 = Arrays.copyOfRange(ciphertextBytes, i * keySize, (i + 1) * keySize);
                byte[] block2 = Arrays.copyOfRange(ciphertextBytes, (i + 1) * keySize, (i + 2) * keySize);

                if (block1.length == keySize && block2.length == keySize) {
                    totalNormalizedDistance += (double) hammingDistance(block1, block2) / keySize;
                    comparisonsMade++;
                }
            }

            if (comparisonsMade > 0) {
                double averageNormalizedDistance = totalNormalizedDistance / comparisonsMade;
                keySizeScores.add(new HashMap.SimpleEntry<>(keySize, averageNormalizedDistance));
            }
        }

        Collections.sort(keySizeScores, Comparator.comparingDouble(Map.Entry::getValue));

        System.out.println("\nTop 5 KEYSIZEs mais prováveis (distância normalizada média):");
        for (int i = 0; i < Math.min(5, keySizeScores.size()); i++) {
            Map.Entry<Integer, Double> entry = keySizeScores.get(i);
            System.out.printf("KEYSIZE: %d, Distância Normalizada Média: %.4f%n", entry.getKey(), entry.getValue());
        }

        int probableKeySize = keySizeScores.get(0).getKey();
        System.out.println("\nKEYSIZE mais provável: " + probableKeySize);

        List<byte[]> blocks = new ArrayList<>();
        for (int i = 0; i < ciphertextBytes.length; i += probableKeySize) {
            blocks.add(Arrays.copyOfRange(ciphertextBytes, i, Math.min(i + probableKeySize, ciphertextBytes.length)));
        }

        List<byte[]> transposedBlocks = new ArrayList<>();
        for (int i = 0; i < probableKeySize; i++) {
            List<Byte> currentTransposedBlock = new ArrayList<>();
            for (byte[] block : blocks) {
                if (i < block.length) {
                    currentTransposedBlock.add(block[i]);
                }
            }
            byte[] transposedBlockArray = new byte[currentTransposedBlock.size()];
            for (int j = 0; j < currentTransposedBlock.size(); j++) {
                transposedBlockArray[j] = currentTransposedBlock.get(j);
            }
            transposedBlocks.add(transposedBlockArray);
        }


        byte[] repeatingKey = new byte[probableKeySize];
        for (int i = 0; i < transposedBlocks.size(); i++) {
            byte[] block = transposedBlocks.get(i);
            byte keyByte = findSingleByteXORKey(block);
            repeatingKey[i] = keyByte;
        }

        String discoveredKey = new String(repeatingKey, StandardCharsets.US_ASCII);
        System.out.println("Chave descoberta: \"" + discoveredKey + "\"");


        byte[] decryptedBytes = new byte[ciphertextBytes.length];
        for (int i = 0; i < ciphertextBytes.length; i++) {
            decryptedBytes[i] = (byte) (ciphertextBytes[i] ^ repeatingKey[i % repeatingKey.length]);
        }

        String plaintext = new String(decryptedBytes, StandardCharsets.US_ASCII);
        System.out.println("\nTexto decifrado:");
        System.out.println(plaintext);
    }
}