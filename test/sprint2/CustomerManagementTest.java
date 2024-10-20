package sprint2;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

import static org.junit.Assert.*;

public class CustomerManagementTest {
    private static final String CUSTOMER_FILE = "src/customer_test.txt";
    private static final String VISIT_FILE = "src/visit_test.txt";

    // Denna metod körs före varje test och skapar testdata
    @Before
    public void setup() throws IOException {
        // Skapa en testkundfil
        try (PrintWriter writer = new PrintWriter(CUSTOMER_FILE)) {
            writer.println("7911061234,Fritjoff Flacon");
            writer.println("2024-10-15");
            writer.println("8204021234,Bear Belle");
            writer.println("2019-12-02");
        }
    }

    @Test
    public void testCorrectDataReading() throws FileNotFoundException {
        // Testa att läsa kunddata
        try (Scanner fileScanner = new Scanner(new File(CUSTOMER_FILE))) {
            assertTrue(fileScanner.hasNextLine()); // Kontrollera att det finns en rad
            String line = fileScanner.nextLine(); // Läs raden
            String[] data = line.split(","); // Dela upp raden med kommatecken
            assertEquals("7911061234", data[0].trim()); // Trimma för att ta bort eventuella mellanslag
            assertEquals("Fritjoff Flacon", data[1].trim());
            assertNotEquals("1234567890", data[0].trim()); // Kontrollerar att ett felaktigt personnummer inte matchar
            assertNotEquals("Fel Namn", data[1].trim()); // Kontrollerar att ett felaktigt namn inte matchar
        }
    }

    @Test
    public void testMemberStatus() {
        LocalDate lastPayment = LocalDate.parse("2019-12-02");
        LocalDate simulatedCurrentDate = LocalDate.parse("2024-10-15"); // Simulerat nuvarande datum
        assertTrue(Period.between(lastPayment, simulatedCurrentDate).getYears() > 1);

        // Lägg till test med felaktigt datum
        LocalDate invalidDate = LocalDate.parse("2020-11-01"); // Mindre än 1 år från 2019-12-02
        assertFalse(Period.between(lastPayment, invalidDate).getYears() > 1); // Ska vara falskt
    }

    @Test
    public void testFileOutput() throws IOException {
        // Testa att skriva till fil
        try (PrintWriter visitWriter = new PrintWriter(VISIT_FILE)) {
            visitWriter.println("7911061234,Fritjoff Flacon, +2024-10-15 ");
        }

        // Verifiera att filen har skapats och innehåller korrekt data
        try (Scanner fileScanner = new Scanner(new File(VISIT_FILE))) {
            assertTrue(fileScanner.hasNextLine());
            String line = fileScanner.nextLine();
            assertTrue(line.contains("7911061234"));
            assertTrue(line.contains("Fritjoff Flacon"));
            // Lägg till felaktiga kontroller
            assertFalse(line.contains("invalid_data")); // Ska inte innehålla ogiltiga data
            assertFalse(line.isEmpty()); // Ska inte vara tom
        }
    }
}