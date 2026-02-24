package com.pokerhands;

import com.pokerhands.parser.InputParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Poker Hands Evaluator ===");

        boolean running = true;

        while (running) {
            System.out.println("\nChoisissez un mode :");
            System.out.println("1 - Saisie manuelle");
            System.out.println("2 - Lecture depuis un fichier");
            System.out.println("exit - Quitter");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("exit")) {
                System.out.println("Au revoir !");
                break;
            }

            switch (choice) {
                case "1":
                    running = modeSaisieManuelle(scanner);
                    break;
                case "2":
                    System.out.print("Entrez le nom du fichier : ");
                    String filePath = scanner.nextLine().trim();
                    modeLectureFichier(filePath);
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }

        scanner.close();
    }

    private static boolean modeSaisieManuelle(Scanner scanner) {

        System.out.println("\nMode saisie manuelle.");
        System.out.println("Entrez une ligne au format attendu.");

        while (true) {
            System.out.print("\nligne > ");
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) continue;

            String result = InputParser.processLine(line);
            System.out.println(result);

            System.out.println("\nQue voulez-vous faire ?");
            System.out.println("c - Continuer");
            System.out.println("menu - Retour au menu");
            System.out.println("exit - Quitter l'application");
            System.out.print("> ");

            String next = scanner.nextLine().trim();

            if (next.equalsIgnoreCase("c")) {
                continue;
            } else if (next.equalsIgnoreCase("menu")) {
                return true;  // retourne au menu principal
            } else if (next.equalsIgnoreCase("exit")) {
                System.out.println("Au revoir !");
                return false; // arrête complètement le programme
            } else {
                System.out.println("Choix invalide, retour au menu.");
                return true;
            }
        }
    }

    private static void modeLectureFichier(String filePath) {
        System.out.println("\nLecture du fichier : " + filePath);

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            if (lines.isEmpty()) {
                System.out.println("Le fichier est vide.");
                return;
            }

            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String result = InputParser.processLine(line);
                System.out.println(result);
            }

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture : " + e.getMessage());
        }
    }
}