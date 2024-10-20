package sprint2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

public class CustomerManagement {
    public static void main(String[] args) throws FileNotFoundException {

        // Öppna scanner för användarinmatning och PrintWriter för att skriva till besöksfil
        try (Scanner scanner = new Scanner(System.in);
             PrintWriter visitWriter = new PrintWriter(new File("src/sprint2/visit.txt"))) {

            // Huvudloop för att hantera flera inmatningar
            while (true) {
                System.out.print("Ange personnummer eller namn: ");
                String input = scanner.nextLine();

                // Avbryt om inmatningen är tom
                if (input.isEmpty()) {
                    break;
                }

                boolean found = false;

                // Läs kundfilen och leta efter matchande personnummer eller namn
                try (Scanner fileScanner = new Scanner(new File("src/sprint2/customer.txt"))) {
                    while (fileScanner.hasNextLine()) {
                        // Läs personnummer och namn från första raden
                        String line1 = fileScanner.nextLine();
                        String[] data = line1.split(", ");
                        String personnummer = data[0];
                        String name = data[1];

                        // Läs betalningsdatum från nästa rad
                        if (!fileScanner.hasNextLine()) {
                            break; // Hantera fel om filen inte har en andra rad
                        }
                        String line2 = fileScanner.nextLine();
                        LocalDate lastPayment = LocalDate.parse(line2.trim());

                        // Kontrollera om inmatat personnummer eller namn matchar
                        if (personnummer.equals(input) || name.equalsIgnoreCase(input)) {
                            found = true;
                            // Kontrollera om betalningen är inom senaste året
                            if (Period.between(lastPayment, LocalDate.now()).getYears() < 1) {
                                System.out.println("Kunden är en nuvarande medlem.");
                                // Skriv till besöksfilen
                                visitWriter.println(personnummer + "," + name + "," + LocalDate.now());
                                visitWriter.flush();
                            } else {
                                System.out.println("Kunden är en före detta medlem.");
                            }
                            break;
                        }
                    }
                }

                // Om kunden inte hittades
                if (!found) {
                    System.out.println("Kunden kunde inte hittas.");
                }
            }
        } catch (IOException e) {
            System.out.println("Ett fel uppstod: " + e.getMessage());
        }
    }
}