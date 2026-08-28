package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    private static final String OUTPUT_FILE_NAME = "extracted-text.txt";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter website URL:");
        String url = scanner.nextLine();

        WebsiteTextExtractor extractor = new WebsiteTextExtractor();

        System.out.println();
        System.out.println("Extracting text...");
        System.out.println();

        try {
            String extractedText = extractor.extractText(url);

            System.out.println("Text extracted successfully!");
            System.out.println();
            System.out.println("----- EXTRACTED TEXT -----");
            System.out.println(extractedText);
            System.out.println("---------------------------");

            saveToFile(extractedText);

            System.out.println();
            System.out.println("Output saved to:");
            System.out.println(Path.of(OUTPUT_FILE_NAME).toAbsolutePath());

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not extract text from the website.");
            System.out.println("Reason: " + e.getMessage());
        } catch (Exception e) {
            // Safety net for anything unexpected, without showing a raw stack trace.
            System.out.println("Something went wrong: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    /**
     * Writes the extracted text to extracted-text.txt in the project's working directory.
     */
    private static void saveToFile(String text) throws IOException {
        try (PrintWriter writer = new PrintWriter(OUTPUT_FILE_NAME, StandardCharsets.UTF_8)) {
            writer.print(text);
        }
    }
}