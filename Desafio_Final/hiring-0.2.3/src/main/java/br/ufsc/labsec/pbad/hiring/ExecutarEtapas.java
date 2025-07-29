package br.ufsc.labsec.pbad.hiring;

import br.ufsc.labsec.pbad.hiring.etapas.*;
import java.util.Scanner;

public class ExecutarEtapas {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=========== DESAFIO FINAL LABSEC ===========");
        System.out.println("Sistema de Certificação e Assinatura Digital");
        System.out.println("============================================");

        while (true) {
            Menu();
            int choice = -1; // Valor para opção inválida

            try {
                String input = scanner.nextLine();
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {}

            try {
                switch (choice) {
                    case 1:
                        PrimeiraEtapa.executarEtapa();
                        break;
                    case 2:
                        SegundaEtapa.executarEtapa();
                        break;
                    case 3:
                        TerceiraEtapa.executarEtapa();
                        break;
                    case 4:
                        QuartaEtapa.executarEtapa();
                        break;
                    case 5:
                        QuintaEtapa.executarEtapa();
                        break;
                    case 6:
                        SextaEtapa.executarEtapa();
                        break;
                    case 7:
                        System.out.println("Encerrando o sistema...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.err.println("Ocorreu um erro durante a execução da etapa");
                e.printStackTrace();
            }

            // Pausa para o usuario ler a saída
            if (choice != 7) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    private static void Menu() {
        System.out.println("\n============== MENU PRINCIPAL ==============");
        System.out.println("1. Primeira Etapa: Calcular resumo criptográfico (hash)");
        System.out.println("2. Segunda Etapa: Gerar par de chaves assimétricas");
        System.out.println("3. Terceira Etapa: Gerar certificados digitais");
        System.out.println("4. Quarta Etapa: Gerar repositório de chaves seguro");
        System.out.println("5. Quinta Etapa: Gerar uma assinatura digital");
        System.out.println("6. Sexta Etapa: Verificar uma assinatura digital");
        System.out.println("7. Sair do programa");
        System.out.print("Escolha uma opção: ");
    }
}